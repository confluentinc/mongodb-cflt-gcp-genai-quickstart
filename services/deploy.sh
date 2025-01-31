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
mandatory_vars=("GCP_REGION" "GCP_PROJECT_ID" "BOOTSTRAP_SERVER" "KAFKA_API_KEY" "KAFKA_API_SECRET" "SR_API_KEY" "SR_API_SECRET" "SR_URL" "UNIQUE_ID" "CLIENT_ID" "MONGODB_HOST" "MONGODB_USER" "MONGODB_PASSWORD" "MONGODB_CLUSTER" "MONGODB_DATABASE" "MONGODB_COLLECTION")

# Check each mandatory environment variable
for var in "${mandatory_vars[@]}"; do
    check_env_var "$var"
done


IMAGE_ARCH=$(uname -m | grep -qE 'arm64|aarch64' && echo 'arm64' || echo 'x86_64')
CURRENT_DIR=$(dirname "$0")
SCRIPT_FOLDER=$(get_full_path "$CURRENT_DIR")
CONFIG_FOLDER="$SCRIPT_FOLDER"/.config

echo "[+] SCRIPT_FOLDER: $SCRIPT_FOLDER"

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

LOWER_UNIQUE_ID=$(to_lowercase "$UNIQUE_ID")


#Deploying Search
SERVICE_PATH="$SCRIPT_FOLDER/search"
SVC_NAME="quickstart-gcp-mongo-search-"$LOWER_UNIQUE_ID

echo "[+] Building and Deploying search"
IMAGE_ARCH=$IMAGE_ARCH docker run -v "$CONFIG_FOLDER":/root/.config/  -v "$SERVICE_PATH":/root/source -ti --rm --name quickstart-deploy-search gcr.io/google.com/cloudsdktool/google-cloud-cli:stable gcloud run deploy "$SVC_NAME" --source "/root/source/" --region "$GCP_REGION" --allow-unauthenticated --project "$GCP_PROJECT_ID" \
--set-env-vars MONGODB_HOST="$MONGODB_HOST",MONGODB_CLUSTER="$MONGODB_CLUSTER",MONGODB_DATABASE="$MONGODB_DATABASE",MONGODB_COLLECTION="$MONGODB_COLLECTION",MONGODB_USER="$MONGODB_USER",MONGODB_PWD="$MONGODB_PASSWORD",BOOTSTRAP_SERVER="$BOOTSTRAP_SERVER",KAFKA_API_KEY="$KAFKA_API_KEY",KAFKA_API_SECRET="$KAFKA_API_SECRET",SR_API_KEY="$SR_API_KEY",SR_API_SECRET="$SR_API_SECRET",SR_URL="$SR_URL",CLIENT_ID="$CLIENT_ID"
if [ $? -ne 0 ]; then
    echo "[-] Failed to deploy back end"
    exit 1
fi
echo "[+] Search deployed successfully"

SEARCH_URL=$(IMAGE_ARCH=$IMAGE_ARCH docker run -v "$CONFIG_FOLDER":/root/.config/  -ti --rm --name quickstart-deploy-search gcr.io/google.com/cloudsdktool/google-cloud-cli:stable gcloud run services describe "$SVC_NAME" --region "$GCP_REGION" --project "$GCP_PROJECT_ID" --format="value(status.url)")
SEARCH_URL=${SEARCH_URL//$'\r'/}
echo "[+] Search URL: $SEARCH_URL"

##Deploying Websocket
#SERVICE_PATH="$SCRIPT_FOLDER/websocket"
#SVC_NAME="quickstart-gcp-mongo-"$LOWER_UNIQUE_ID
#
#echo "[+] Building WebSocket Frontend"
#IMAGE_ARCH=$IMAGE_ARCH docker run -v "$SERVICE_PATH":/root/source/ -ti --rm --name build-frontend node:current-alpine3.20 sh -c "cd /root/source/frontend && npm i && npm run build"
#if [ $? -ne 0 ]; then
#    echo "[-] Failed to build WebSocket ui"
#    exit 1
#fi
#echo "[+] WebSocket Frontend built successfully"
#
#echo "[+] Building and Deploying WebSocket backend"
#IMAGE_ARCH=$IMAGE_ARCH docker run -v "$CONFIG_FOLDER":/root/.config/  -v "$SERVICE_PATH":/root/source -ti --rm --name quickstart-deploy-websocket gcr.io/google.com/cloudsdktool/google-cloud-cli:stable gcloud run deploy "$SVC_NAME" --source "/root/source/" --region "$GCP_REGION" --allow-unauthenticated --project "$GCP_PROJECT_ID" \
#--set-env-vars MONITOR_SERVICES="$SEARCH_URL",BOOTSTRAP_SERVER="$BOOTSTRAP_SERVER",KAFKA_API_KEY="$KAFKA_API_KEY",KAFKA_API_SECRET="$KAFKA_API_SECRET",SR_API_KEY="$SR_API_KEY",SR_API_SECRET="$SR_API_SECRET",SR_URL="$SR_URL",CLIENT_ID="$CLIENT_ID"
#if [ $? -ne 0 ]; then
#    echo "[-] Failed to deploy back end"
#    exit 1
#fi
#echo "[+] WebSocket deployed successfully"
