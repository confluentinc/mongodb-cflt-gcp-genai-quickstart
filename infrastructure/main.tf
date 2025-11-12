resource "random_string" "unique_id" {
  length  = 8
  special = false
  lower   = true
  upper   = false
}

module "gcp" {
  source         = "./modules/gcp"
  gcp_region     = var.gcp_region
  gcp_project_id = var.gcp_project_id
  unique_id      = var.unique_id
}

module "mongodb" {
  source                      = "./modules/mongodb"
  mongodbatlas_public_key     = var.mongodbatlas_public_key
  mongodbatlas_private_key    = var.mongodbatlas_private_key
  mongodbatlas_database       = var.mongodbatlas_database
  mongodbatlas_collection     = var.mongodbatlas_collection
  mongodbatlas_org_id         = var.mongodbatlas_org_id
  mongodbatlas_cluster        = var.mongodbatlas_cluster
  mongodbatlas_project        = var.mongodbatlas_project
  mongodbatlas_cloud_provider = var.mongodbatlas_cloud_provider
  mongodbatlas_cloud_region   = var.mongodbatlas_cloud_region
  unique_id                   = var.unique_id
  providers = {
    mongodbatlas = mongodbatlas
  }
}

module "confluent_cloud_cluster" {
  source                           = "./modules/confluent-cloud-cluster"
  env_display_id_postfix           = local.env_display_id_postfix
  confluent_cloud_region           = var.confluent_cloud_region
  confluent_cloud_service_provider = var.confluent_cloud_service_provider
  confluent_cloud_environment = {
    name = var.confluent_cloud_environment_name
  }
  create_model_sql_files = local.create_model_sql_files
  insert_data_sql_files  = local.insert_data_sql_files
  create_table_sql_files = local.create_table_sql_files

  mongodb_user            = module.mongodb.connection_user
  mongodb_password        = module.mongodb.connection_password
  mongodb_host            = module.mongodb.host
  mongodbatlas_database   = var.mongodbatlas_database
  mongodbatlas_collection = var.mongodbatlas_collection

  gcp_project_id               = var.gcp_project_id
  gcp_region                   = var.gcp_region
  gcp_service_account_key_file = module.gcp.gcp_service_account_key_file
  gcp_gemini_api_key           = var.gcp_gemini_api_key
  gcp_service_account_email    = module.gcp.gcp_service_account_email
  gcp_bucket_name              = module.gcp.gcs_bucket_name
  gcs_bucket_d                 = module.gcp.gcs_bucket_id

  depends_on = [
    module.mongodb, module.gcp
  ]
}

# Wait for insert-data statements to start and data to flow to MongoDB
# This gives time for the MongoDB sink connector to create the collection
resource "time_sleep" "wait_for_collection" {
  depends_on = [
    module.confluent_cloud_cluster.insert_data_statements
  ]
  
  create_duration = "5m"
}

# Verify network connectivity to MongoDB Atlas API before creating search index
# This helps catch DNS/network issues early
resource "null_resource" "verify_mongodb_connectivity" {
  depends_on = [
    time_sleep.wait_for_collection
  ]

  provisioner "local-exec" {
    command = <<-EOT
      # Retry DNS lookup up to 5 times with 10 second intervals
      for i in 1 2 3 4 5; do
        if nslookup cloud.mongodb.com > /dev/null 2>&1 || getent hosts cloud.mongodb.com > /dev/null 2>&1; then
          echo "DNS resolution successful"
          exit 0
        fi
        echo "DNS lookup attempt $i failed, retrying in 10 seconds..."
        sleep 10
      done
      echo "ERROR: Unable to resolve cloud.mongodb.com after 5 attempts"
      exit 1
    EOT
  }

  triggers = {
    # Re-run if dependencies change
    insert_statements = jsonencode(module.confluent_cloud_cluster.insert_data_statements)
  }
}

resource "mongodbatlas_search_index" "search-vector" {
  name            = "${var.mongodbatlas_collection}-vector"
  project_id      = module.mongodb.project_id
  cluster_name    = var.mongodbatlas_cluster
  collection_name = var.mongodbatlas_collection
  database        = var.mongodbatlas_database
  type            = "vectorSearch"
  depends_on = [
    time_sleep.wait_for_collection,
    null_resource.verify_mongodb_connectivity
  ]
  
  # Retry on transient network errors
  lifecycle {
    create_before_destroy = true
  }
  
  fields = <<-EOF
[{
      "type": "vector",
      "path": "embeddings",
      "numDimensions": 768,
      "similarity": "euclidean"
}]
EOF
}
