#!/usr/bin/env bash
set -euo pipefail

trap 'echo "Error: Failed to tag image. See messages above." >&2' ERR

# Load .env variables via helper
. "$(dirname "$0")/load-env.sh"
load_env "../../.env"
echo "Environment loaded from ../../.env"

# Ensure VERSION is set
: "${DOCKER_IMAGE_VERSION:?DOCKER_IMAGE_VERSION is not set in .env}"
echo "Using DOCKER_IMAGE_VERSION=${DOCKER_IMAGE_VERSION}"

# Ensure the local source image exists
if ! docker image inspect "todolist-backend:${DOCKER_IMAGE_VERSION}" >/dev/null 2>&1; then
  echo "Error: Source image todolist-backend:${DOCKER_IMAGE_VERSION} not found locally. Build it first." >&2
  exit 1
fi

docker tag "todolist-backend:${DOCKER_IMAGE_VERSION}" "zaytsevmxm/todolist-backend:${DOCKER_IMAGE_VERSION}"
echo "Success: Tagged todolist-backend:${DOCKER_IMAGE_VERSION} -> zaytsevmxm/todolist-backend:${DOCKER_IMAGE_VERSION}"