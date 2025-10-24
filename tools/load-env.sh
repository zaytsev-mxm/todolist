#!/usr/bin/env bash

set -e

# load-env.sh
# Usage: source this file and call: load_env [path_to_env_file]
# Example: load_env ".env"

load_env() {
  local env_file="${1:-.env}"
  if [ ! -f "$env_file" ]; then
    echo "Error: ${env_file} not found" >&2
    return 1
  fi

  # Export KEY=VALUE pairs from the env file
  set -a
  # shellcheck disable=SC1090
  . "${env_file}"
  set +a
}
