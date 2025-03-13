provider "google" {
  project = var.gcp_project_id
  region  = var.gcp_region
}

locals {
  userid = lower(var.unique_id)
}

resource "google_service_account" "service_account" {
  account_id   = "cfltquickstart${local.userid}"
  display_name = "Service Account For Confluent Quickstart"
}

resource "google_project_iam_member" "vertex_ai" {
  project = var.gcp_project_id
  role    = "roles/aiplatform.admin"
  member  = "serviceAccount:${google_service_account.service_account.email}"
}

resource "google_project_iam_member" "bigquery" {
  project = var.gcp_project_id
  role    = "roles/bigquery.admin"
  member  = "serviceAccount:${google_service_account.service_account.email}"
}

resource "google_project_iam_member" "storage" {
  project = var.gcp_project_id
  role    = "roles/storage.admin"
  member  = "serviceAccount:${google_service_account.service_account.email}"
}

resource "google_project_iam_member" "ml" {
  project = var.gcp_project_id
  role    = "roles/ml.admin"
  member  = "serviceAccount:${google_service_account.service_account.email}"
}

resource "google_service_account_key" "service_account_key" {
  service_account_id = google_service_account.service_account.name
  public_key_type    = "TYPE_X509_PEM_FILE"
  private_key_type   = "TYPE_GOOGLE_CREDENTIALS_FILE"
  depends_on = [
    google_service_account.service_account,
    google_project_iam_member.bigquery,
    google_project_iam_member.vertex_ai,
    google_project_iam_member.ml,
    google_project_iam_member.storage
  ]
}

resource "google_storage_bucket" "storage_bucket" {
  project                     = var.gcp_project_id
  location                    = var.gcp_region
  name                        = "cfltquickstart-mediccations-${local.userid}"
  force_destroy               = true
  storage_class               = "STANDARD"
  uniform_bucket_level_access = true
}

resource "google_storage_bucket_object" "data_folder" {
  name = "data/" # folder name should end with '/'
  content = " "            # content is ignored but should be non-empty
  bucket = google_storage_bucket.storage_bucket.id
  depends_on = [google_storage_bucket.storage_bucket]
}

# locals {
#   data_files = fileset("${path.module}/data", "**/*")
# }
#
# resource "google_storage_bucket_object" "data" {
#   depends_on = [google_storage_bucket_object.data_folder]
#   for_each = local.data_files
#
#   name         = "data/${each.value}"
#   source       = "${path.module}/data/${each.value}"
#   content_type = endswith(each.value, ".json") ? "application/json" : "text/plain"
#   bucket       = google_storage_bucket.storage_bucket.id
# }

resource "local_file" "service_account_key_file" {
  content = base64decode(google_service_account_key.service_account_key.private_key)
  filename = "${path.root}/service-account-key.json"
}