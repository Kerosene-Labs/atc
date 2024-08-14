#!/bin/bash

# URL to be requested
url="https://localhost:8443"
atcIdentityToken="N2UgODMgZTggMTMgZDYgZGIgYmQgYzIgMzMgOTMgMzYgMGUgZjMgMDIgNzEgNzI="
hostHeader="api.weather.gov"

# unlimit file descriptors
ulimit -n 10000

# Loop to call curl 10000 times
for i in {1..10000}
do
  curl -s --insecure "$url" -H "X-ATC-IdentityToken: ${atcIdentityToken}" -H "Host: ${hostHeader}" &
done

wait

echo "Completed 10,000 curl requests."
