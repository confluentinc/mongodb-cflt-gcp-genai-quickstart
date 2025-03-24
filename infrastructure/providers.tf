terraform {
  required_providers {
    confluent = {
      source  = "confluentinc/confluent"
      version = "~> 2.11.0"
    }
    gcp = {
      source  = "hashicorp/google"
      version = "~> 6.25.0"
    }
    mongodbatlas = {
      source  = "mongodb/mongodbatlas"
      version = "1.21.4"
    }
  }
}

provider "confluent" {
  cloud_api_key    = var.confluent_cloud_api_key
  cloud_api_secret = var.confluent_cloud_api_secret
}

provider "gcp" {
  region  = var.gcp_region
  project = var.gcp_project_id
}

provider "mongodbatlas" {
  public_key  = var.mongodbatlas_public_key
  private_key = var.mongodbatlas_private_key
}
