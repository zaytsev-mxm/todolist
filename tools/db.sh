#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(git -C "$SCRIPT_DIR" rev-parse --show-toplevel)"

# Check for arguments
if [[ "$*" == *"--up"* ]]; then
    echo "Starting database..."
    docker compose -f "$ROOT_DIR/docker-compose.yml" up -d db
    echo "Database started successfully!"
elif [[ "$*" == *"--down"* ]]; then
    echo "Stopping database..."
    docker compose -f "$ROOT_DIR/docker-compose.yml" stop db
    echo "Database stopped successfully!"
else
    echo "Usage: sh tools/db.sh [--up|--down]"
    echo "  --up    Start the database"
    echo "  --down  Stop the database"
    exit 1
fi
