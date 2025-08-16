#!/usr/bin/env bash
set -euo pipefail

trap 'echo "Error: Failed to push image. See messages above." >&2' ERR

# Load .env variables via helper
. "$(dirname "$0")/load-env.sh"
load_env "../../.env"
echo "Environment loaded from ../../.env"

# Ensure DOCKER_IMAGE_VERSION is set
: "${DOCKER_IMAGE_VERSION:?DOCKER_IMAGE_VERSION is not set in .env}"
echo "Using DOCKER_IMAGE_VERSION=${DOCKER_IMAGE_VERSION}"

IMAGE="zaytsevmxm/todolist-backend:${DOCKER_IMAGE_VERSION}"

# Ensure the local image with the remote tag exists
if ! docker image inspect "${IMAGE}" >/dev/null 2>&1; then
  echo "Error: ${IMAGE} not found locally. Tag it first (e.g., run the tagging script)." >&2
  exit 1
fi

echo "Pushing ${IMAGE}..."
docker push "${IMAGE}"
echo "Success: Pushed ${IMAGE}"
