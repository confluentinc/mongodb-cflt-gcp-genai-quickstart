output "gcp_service_account_key" {
  value     = google_service_account_key.service_account_key.private_key
  sensitive = true
}

output "gcp_service_account_key_file" {
  value = "${path.root}/service-account-key.json"
}

