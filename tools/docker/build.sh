#!/usr/bin/env bash
set -euo pipefail

trap 'echo "Error: Failed to tag image. See messages above." >&2' ERR

# Load .env variables via helper
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(git -C "$SCRIPT_DIR" rev-parse --show-toplevel)"
. "$(dirname "$0")/../load-env.sh"
load_env "$ROOT_DIR/.env"
echo "Environment loaded from $ROOT_DIR/.env"

# Ensure VERSION is set
: "${DOCKER_IMAGE_VERSION:?DOCKER_IMAGE_VERSION is not set in .env}"
echo "Using DOCKER_IMAGE_VERSION=${DOCKER_IMAGE_VERSION}"

echo "We are at $(pwd)"

build_status=0
docker buildx build "$ROOT_DIR" --build-arg DOCKER_IMAGE_VERSION="${DOCKER_IMAGE_VERSION}" --tag="todolist-backend:${DOCKER_IMAGE_VERSION}" || build_status=$?
if [ "${build_status}" -ne 0 ]; then
  echo "Error: Failed to build image todolist-backend:${DOCKER_IMAGE_VERSION}" >&2
  exit 1
fi

echo "Success: Built todolist-backend:${DOCKER_IMAGE_VERSION}"