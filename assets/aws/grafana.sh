#!/bin/bash

# this forces usage of bash because $RANDOM is a bash-only variable
# in case this script is called using sh, it will re-call itself using bash.
if [ ! "$BASH_VERSION" ] ; then
    exec /bin/bash "$0" "$@"
fi

createDashboard() {
  curl -X POST -H "Content-Type: application/json" -d '{"admin":"prom-operator"}' http://admin:prom-operator@localhost:3000/api/orgs
  local AUTH22=$(curl -X POST -H "Content-Type: application/json" -d '{"name":"'"$RANDOM"'", "role": "Admin"}' http://admin:prom-operator@localhost:3000/api/auth/keys)
  local KEY22=$(echo $AUTH22 | jq -r '.key')
  curl -X POST --insecure -H "Authorization: Bearer $KEY22" -H "Content-Type: application/json" -d '{
    "dashboard": '"$(cat $1)"',
    "overwrite": true
  }' http://localhost:3000/api/dashboards/db
}

createDashboard "assets/docker-compose/grafana/dashboards/DGR-COP-SUJETO-TRI.json"