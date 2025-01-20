#!/usr/bin/env bash

echo "[+] Destroying front end"
rm -rf ./frontend/node_modules
rm -rf ./src/main/resources/static/
echo "[+] Front end destroyed successfully"