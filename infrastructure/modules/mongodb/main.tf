resource "mongodbatlas_project" "project" {
  name   = "${var.mongodbatlas_project}-${lower(var.unique_id)}"
  org_id = var.mongodbatlas_org_id
}

resource "mongodbatlas_cluster" "cluster" {
  project_id = mongodbatlas_project.project.id
  name = var.mongodbatlas_cluster

  # Provider Settings for Free tier
  provider_name               = "TENANT"
  backing_provider_name       = var.mongodbatlas_cloud_provider
  provider_region_name        = var.mongodbatlas_cloud_region
  provider_instance_size_name = "M0"
}

resource "random_password" "dbPassword" {
  length  = 16
  special = false
}

locals {
  userid = lower(var.unique_id)
  dbuser = "cflt-quickstart-${local.userid}"
}

resource "mongodbatlas_database_user" "default" {
  project_id         = mongodbatlas_project.project.id
  username           = local.dbuser
  password           = random_password.dbPassword.result
  auth_database_name = "admin"

  roles {
    role_name     = "readWrite"
    database_name = var.mongodbatlas_database
  }
}

resource "mongodbatlas_project_ip_access_list" "ip" {
  project_id = mongodbatlas_project.project.id
  cidr_block = "0.0.0.0/0"
  comment    = "IP Address for accessing the cluster"
}

