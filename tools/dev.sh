#!/bin/bash

set -e

# Check if --load-env flag is provided
if [[ "$*" == *"--load-env"* ]]; then
    # Load .env variables via helper
    SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
    ROOT_DIR="$(git -C "$SCRIPT_DIR" rev-parse --show-toplevel)"
    . "$(dirname "$0")/load-env.sh"
    load_env "$ROOT_DIR/.env"
    echo "Environment loaded from $ROOT_DIR/.env"
fi

./gradlew -t run