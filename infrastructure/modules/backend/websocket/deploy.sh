#!/usr/bin/env bash

## Function to prompt for input until a non-empty value is provided
#prompt_for_input() {
#    local var_name=$1
#    local prompt_message=$2
#    local is_secret=$3
#
#    while true; do
#        if [ "$is_secret" = true ]; then
#            read -r -s -p "$prompt_message: " input_value
#            echo ""
#        else
#            read -r -p "$prompt_message: " input_value
#        fi
#
#        if [ -z "$input_value" ]; then
#            echo "[-] $var_name cannot be empty"
#        else
#            eval "$var_name='$input_value'"
#            break
#        fi
#    done
#}
#
#[ -z "$GCP_PROJECT_ID" ] && prompt_for_input GCP_PROJECT_ID "Enter your GCP_PROJECT_ID" false
#
## Default to us-east1 if GCP_REGION is not set
#[ -z "$GCP_REGION" ] && read -r -p "Enter the GCP region (default: us-east1): " GCP_REGION && GCP_REGION=${GCP_REGION:-us-east1}

echo "[+] Building front end"
docker run -v ./:/root/source/ -ti --rm --name build-front-end node:23-alpine3.20 sh -c "cd /root/source/frontend && npm ci && npm run build"
if [ $? -ne 0 ]; then
    echo "[-] Failed to build front end"
    exit 1
fi
echo "[+] Front end built successfully"

#gcloud run deploy quickstart-gcp-mongo --source . --region us-central1 --allow-unauthenticated
#gcloud run services delete quickstart-gcp-mongo --region us-central1 --quiet
