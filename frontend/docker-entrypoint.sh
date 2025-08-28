#!/bin/sh
set -e
echo "const backend = \"${API_URL}\";" > /usr/share/nginx/html/public/constants.js
exec "$@"
