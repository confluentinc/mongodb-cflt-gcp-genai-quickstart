#!/usr/bin/env bash

get_full_path() {
    local path=$1
    realpath "$path"
}

IMAGE_ARCH=$(uname -m | grep -qE 'arm64|aarch64' && echo 'arm64' || echo 'x86_64')
CURRENT_DIR=$(dirname "$0")
SCRIPT_FOLDER=$(get_full_path "$CURRENT_DIR")
CONFIG_FOLDER="$SCRIPT_FOLDER"/.config

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

deploy() {
  local service_name=$1
  local has_ui=$2

  local service_path="$SCRIPT_FOLDER"/"$service_name"

  echo "[+] Building and Deploying $service_path"

  if [ "$has_ui" = true ]; then
    echo "[+] Building $service_name ui"
    IMAGE_ARCH=$IMAGE_ARCH docker run -v "$service_path"/frontend:/root/source/ -ti --rm --name build-$service_name-cli node:23-alpine3.20 sh -c "cd /root/source/ && npm ci && npm run build"
    if [ $? -ne 0 ]; then
        echo "[-] Failed to build $service_name ui"
        exit 1
    fi
    echo "[+] $service_name ui built successfully"
  fi

  echo "[+] Building and Deploying back end"
  IMAGE_ARCH=$IMAGE_ARCH docker run -v "$CONFIG_FOLDER":/root/.config/  -v "$service_path":/root/source -ti --rm --name quickstart-deploy-backend gcr.io/google.com/cloudsdktool/google-cloud-cli:stable gcloud run deploy quickstart-gcp-mongo --source "/root/source" --region "$GCP_REGION" --allow-unauthenticated --project "$GCP_PROJECT_ID" \
  --set-env-vars BOOTSTRAP_SERVER="$BOOTSTRAP_SERVER",KAFKA_API_KEY="$KAFKA_API_KEY",KAFKA_API_SECRET="$KAFKA_API_SECRET",SR_API_KEY="$SR_API_KEY",SR_API_SECRET="$SR_API_SECRET",SR_URL="$SR_URL"
  if [ $? -ne 0 ]; then
      echo "[-] Failed to deploy back end"
      exit 1
  fi
  echo "[+] Back end deployed successfully"

  echo "[+] $service_name deployed successfully"
}

# Check if the the .config folder does not exists
if [ ! -d "$CONFIG_FOLDER" ]; then
  echo "[+] Authenticating gcloud for cli"
  IMAGE_ARCH=$IMAGE_ARCH docker run -v "$CONFIG_FOLDER":/root/.config/ -ti --rm --name gcloud-config gcr.io/google.com/cloudsdktool/google-cloud-cli:stable gcloud auth login
  if [ $? -ne 0 ]; then
      echo "[-] Failed to authenticate gcloud"
      exit 1
  fi
  echo "[+] gcloud authentication complete"
fi

deploy websocket true
if [ $? -ne 0 ]; then
    echo "[-] Failed to deploy websocket"
    exit 1
fi

#gcloud run deploy quickstart-gcp-mongo --source . --region us-central1 --allow-unauthenticated
#gcloud run services delete quickstart-gcp-mongo --region us-central1 --quiet
