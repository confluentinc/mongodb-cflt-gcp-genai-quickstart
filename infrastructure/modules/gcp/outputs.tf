output "gcp_service_account_key" {
  value     = google_service_account_key.service_account_key.private_key
  sensitive = true
}

output "gcp_service_account_key_file" {
  value = "${path.root}/service-account-key.json"
}

output "gcp_service_account_email" {
  value = google_service_account.service_account.email
}

output "gcs_bucket_name" {
  value = google_storage_bucket.storage_bucket.name
}

output "gcs_bucket_id" {
  value = google_storage_bucket.storage_bucket.id
}