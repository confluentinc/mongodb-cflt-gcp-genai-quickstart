#!/usr/bin/env bash

IMAGE_ARCH=$(uname -m | grep -qE 'arm64|aarch64' && echo 'arm64' || echo 'x86_64')
CURRENT_DIR=$(dirname "$0")
SCRIPT_FOLDER=$(get_full_path "$CURRENT_DIR")

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

echo "[+] Destroying back end"
IMAGE_ARCH=$IMAGE_ARCH docker run -v "$GCP_CONFIG_FOLDER":/root/.config/ -ti --rm --name quickstart-deploy-backend gcr.io/google.com/cloudsdktool/google-cloud-cli:stable  gcloud run services delete quickstart-gcp-mongo --region "$GCP_REGION" --project "$GCP_PROJECT_ID" --quiet
if [ $? -ne 0 ]; then
    echo "[-] Failed to destroy back end"
    exit 1
fi
echo "[+] Back end destroyed successfully"

echo "[+] Destroying front end"
rm -rf "$SCRIPT_FOLDER"/frontend/node_modules
rm -rf "$SCRIPT_FOLDER"/src/main/resources/static/
echo "[+] Front end destroyed successfully"