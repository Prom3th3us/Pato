
queryCassandraSilently() {
  docker exec cassandra cqlsh -e "EXPAND ON; select * from read_side.$1;" > /dev/null 2>&1
}

queryCassandra() {
  echo "Querying Cassandra..."
  echo "by using 'docker exec cassandra cqlsh -e 'select * from read_side.$1;'"
  docker exec cassandra cqlsh -e "EXPAND ON; select * from read_side.$1;"
}


if [ $# -ne 1 ]; then
  echo 1>&2 "Usage: $0 TABLE"
  echo 1>&2 "example: $0 buc_obligaciones"
  echo 1>&2 "example: $0 buc_sujeto"
  echo 1>&2 "example: $0 buc_sujeto_objeto"
  exit 3
fi

  echo "Querying Cassandra..."
  echo "by using 'docker exec cassandra cqlsh -e 'EXPAND ON; select * from read_side.$1;'"
  while ! queryCassandraSilently $1; do
      sleep 1
  done

  queryCassandra $1

  while true; do
    echo "Press any key to refresh"
    read answer
    queryCassandra $1
  done
