
queryApiSilently() {
  local API=$1
  curl $API > /dev/null 2>&1
}

queryApi() {
  local API=$1
  echo "Querying Cassandra..."
  echo "by using 'curl $API'"
  curl $API
}


if [ $# -ne 1 ]; then
  echo 1>&2 "Usage: $0 API"
  echo 1>&2 "example: $0 0.0.0.0:8081/state/sujeto/1"
  echo 1>&2 "example: $0 0.0.0.0:8081/state/sujeto/1/objeto/1/tipo/1"
  echo 1>&2 "example: $0 0.0.0.0:8081/state/sujeto/1/objeto/1/tipo/1/obligacion/1"
  exit 3
fi

  echo "Querying API..."
  echo "by using 'curl $1;'"
  while ! queryApiSilently $1; do
      sleep 1
  done

  queryApi $1

  while true; do
    echo "Press any key to refresh" 
    read answer
    queryApi $1
  done
