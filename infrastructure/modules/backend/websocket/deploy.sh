#!/usr/bin/env bash

get_full_path() {
    local path=$1
    realpath "$path"
}

IMAGE_ARCH=$(uname -m | grep -qE 'arm64|aarch64' && echo 'arm64' || echo 'x86_64')
CURRENT_DIR=$(dirname "$0")
SCRIPT_FOLDER=$(get_full_path "$CURRENT_DIR")

echo "[+] SCRIPT_FOLDER: $SCRIPT_FOLDER"

# Function to prompt for input until a non-empty value is provided
prompt_for_input() {
    local var_name=$1
    local prompt_message=$2
    local is_secret=$3

    while true; do
        if [ "$is_secret" = true ]; then
            read -r -s -p "$prompt_message: " input_value
            echo ""
        else
            read -r -p "$prompt_message: " input_value
        fi

        if [ -z "$input_value" ]; then
            echo "[-] $var_name cannot be empty"
        else
            eval "$var_name='$input_value'"
            break
        fi
    done
}

[ -z "$GCP_CONFIG_FOLDER" ] && prompt_for_input GCP_CONFIG_FOLDER "Enter your GCP Authentication Config Folder" false
[ -z "$GCP_REGION" ] && read -r -p "Enter the GCP region (default: us-east1): " GCP_REGION && GCP_REGION=${GCP_REGION:-us-east1}
[ -z "$GCP_PROJECT_ID" ] && prompt_for_input GCP_PROJECT_ID "Enter your GCP_PROJECT_ID" false

echo "[+] GCP_CONFIG_FOLDER: $GCP_CONFIG_FOLDER"

echo "[+] Building front end"
IMAGE_ARCH=$IMAGE_ARCH docker run -v "$SCRIPT_FOLDER":/root/source/ -ti --rm --name build-front-end node:23-alpine3.20 sh -c "cd /root/source/frontend && npm ci && npm run build"
if [ $? -ne 0 ]; then
    echo "[-] Failed to build front end"
    exit 1
fi
echo "[+] Front end built successfully"

echo "[+] Building and Deploying back end"
IMAGE_ARCH=$IMAGE_ARCH docker run -v "$GCP_CONFIG_FOLDER":/root/.config/ -v "$SCRIPT_FOLDER":/root/source -ti --rm --name quickstart-deploy-backend gcr.io/google.com/cloudsdktool/google-cloud-cli:stable gcloud run deploy quickstart-gcp-mongo --source "/root/source" --region "$GCP_REGION" --allow-unauthenticated --project "$GCP_PROJECT_ID"
if [ $? -ne 0 ]; then
    echo "[-] Failed to deploy back end"
    exit 1
fi
echo "[+] Back end deployed successfully"

#gcloud run deploy quickstart-gcp-mongo --source . --region us-central1 --allow-unauthenticated
#gcloud run services delete quickstart-gcp-mongo --region us-central1 --quiet
