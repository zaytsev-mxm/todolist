#!/bin/bash

set -e

openapi-generator generate \
  -i ../documentation.yaml \
  -g kotlin \
  -o ../generated-kotlin-client/
