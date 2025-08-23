#!/bin/bash

set -e

openapi-generator generate \
  -i ../documentation.yaml \
  -g typescript \
  -o ../generated-typescript-client/
