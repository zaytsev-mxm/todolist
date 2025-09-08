#!/bin/sh
REPO_ROOT=$(git rev-parse --show-toplevel)
git config core.hooksPath "$REPO_ROOT/.githooks"
chmod +x "$REPO_ROOT/.githooks/"* 2>/dev/null || true
echo "Hooks installed to $REPO_ROOT/.githooks"