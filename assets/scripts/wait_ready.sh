
if [ $# -ne 1 ]; then
  echo 1>&2 "Usage: $0 PORT"
  echo 1>&2 "example: $0 8081"
  echo 1>&2 "example: $0 8082"
  echo 1>&2 "example: $0 8083"
  exit 3
fi

  echo "Checking that node at $1 is up..."
  echo "by using 'curl 0.0.0.0:$1/api/system/status'"
  while ! curl 0.0.0.0:$1/api/system/status --silent; do
      sleep 1
  done
  echo "Node at $1 is up."