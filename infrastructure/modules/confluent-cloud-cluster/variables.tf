variable "confluent_cloud_region" {
  description = "The region of Confluent Cloud Network"
  type        = string
}

variable "confluent_cloud_service_provider" {
  description = "The cloud provider of Confluent Cloud Network"
  type        = string
}

variable "env_display_id_postfix" {
  description = "A random string we will be appending to resources like environment, api keys, etc. to make them unique"
  type        = string
}

variable "create_table_sql_files" {
  description = "The set of SQL files that contain the create table statements"
  type = set(string)
  default = []
}

variable "create_model_sql_files" {
  description = "The set of SQL files that contain the create model statements"
  type = set(string)
  default = []
}

variable "insert_data_sql_files" {
  description = "The set of SQL files that contain the insert data statements"
  type = set(string)
  default = []
}

variable "confluent_cloud_environment" {
  description = "The environment configuration for Confluent Cloud"
  type = object({
    name = string
  })
}

variable "mongodb_user" {
  description = "MongoDB Atlas connection user."
  type        = string
}
variable "mongodb_password" {
  description = "The MongoDB host. Use a hostname address and not a full URL. For example: cluster4-r5q3r7.gcp.mongodb.net. The hostname address must provide a service record (SRV). A standard connection string does not work."
  type        = string
}
variable "mongodb_host" {
  description = "The mongodb cluster host"
  type        = string
}

variable "mongodbatlas_database" {
  description = "MongoDB Atlas database name"
  type        = string
}

variable "mongodbatlas_collection" {
  description = "Collection name to write to. If the connector is sinking data from multiple topics, this is the default collection the topics are mapped to."
  type        = string
}


variable "gcp_region" {
  description = "The GCP region to deploy the infrastructure"
  type        = string
}

variable "gcp_service_account_key_file" {
  description = "GCP service account json key file"
  type        = string
}

variable "gcp_gemini_api_key" {
  description = "GCP Gemini API Key"
  type        = string
}

variable "gcp_project_id" {
  description = "GCP project ID"
  type        = string
}

variable "gcp_service_account_email" {
  description = "GCP service account email"
  type        = string
}

variable "gcp_bucket_name" {
  description = "GCS bucket name"
  type        = string
}

variable "gcs_bucket_d" {
  description = "GCS Bucket Id"
  type        = string
}