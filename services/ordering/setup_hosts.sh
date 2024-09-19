#!/bin/sh
# get the IP address and add it to /etc/hosts
ip=$(netstat -nr | grep '^0\.0\.0\.0' | awk '{print $2}')
if [ -w /etc/hosts ]; then
  cp /etc/hosts /etc/hosts.backup
  # remove any existing localhost mappings
  awk '!/localhost/' /etc/hosts.backup > /etc/hosts
  # the new mapping
  echo "$ip localhost" >> /etc/hosts
fi

# continue with the default CMD or entrypoint
exec "$@"