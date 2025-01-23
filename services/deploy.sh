#!/usr/bin/env bash

echo "[+] Deploying services"

get_full_path() {
    local path=$1
    realpath "$path"
}

# Function to check if an environment variable is set
check_env_var() {
    local var_name=$1
    if [ -z "${!var_name}" ]; then
        echo "[-] Error: Environment variable $var_name is not set."
        exit 1
    fi
}

# Function to convert a string to lowercase
to_lowercase() {
    local input_string=$1
    echo "$input_string" | tr '[:upper:]' '[:lower:]'
}


# List of mandatory environment variables
mandatory_vars=("GCP_REGION" "GCP_PROJECT_ID" "BOOTSTRAP_SERVER" "KAFKA_API_KEY" "KAFKA_API_SECRET" "SR_API_KEY" "SR_API_SECRET" "SR_URL" "UNIQUE_ID")

# Check each mandatory environment variable
for var in "${mandatory_vars[@]}"; do
    check_env_var "$var"
done


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

#Deploying Websocket
SERVICE_PATH="$SCRIPT_FOLDER/websocket"

echo "[+] Building WebSocket Frontend"
IMAGE_ARCH=$IMAGE_ARCH docker run -v "$SERVICE_PATH":/root/source/ -ti --rm --name build-frontend node:23-alpine3.20 sh -c "cd /root/source/frontend && npm ci && npm run build"
if [ $? -ne 0 ]; then
    echo "[-] Failed to build WebSocket ui"
    exit 1
fi
echo "[+] WebSocket Frontend built successfully"

LOWER_UNIQUE_ID=$(to_lowercase "$UNIQUE_ID")
SVC_NAME="quickstart-gcp-mongo-"$LOWER_UNIQUE_ID

echo "[+] Building and Deploying WebSocket backend"
IMAGE_ARCH=$IMAGE_ARCH docker run -v "$CONFIG_FOLDER":/root/.config/  -v "$SERVICE_PATH":/root/source -ti --rm --name quickstart-deploy-backend gcr.io/google.com/cloudsdktool/google-cloud-cli:stable gcloud run deploy "$SVC_NAME" --source "/root/source" --region "$GCP_REGION" --allow-unauthenticated --project "$GCP_PROJECT_ID" \
--set-env-vars BOOTSTRAP_SERVER="$BOOTSTRAP_SERVER",KAFKA_API_KEY="$KAFKA_API_KEY",KAFKA_API_SECRET="$KAFKA_API_SECRET",SR_API_KEY="$SR_API_KEY",SR_API_SECRET="$SR_API_SECRET",SR_URL="$SR_URL"
if [ $? -ne 0 ]; then
    echo "[-] Failed to deploy back end"
    exit 1
fi
echo "[+] WebSocket deployed successfully"

#gcloud run deploy quickstart-gcp-mongo --source . --region us-central1 --allow-unauthenticated
#gcloud run services delete quickstart-gcp-mongo --region us-central1 --quiet
