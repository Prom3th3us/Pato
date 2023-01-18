
echo "== cleaning previous common dependency =="
rm -rf ~/.ivy2/local/weetekio/common_2.13
echo "== common dependency cleaned =="

echo "== creating docker network kafka_copernico_net =="
docker network create kafka_copernico_net --subnet 172.22.0.0/16
echo "== docker network kafka_copernico_net created =="

echo "== starting up cassandra =="
docker-compose -f assets/docker-compose/docker-compose-cassandra.yml up -d
echo "== cassandra started =="

echo "== starting up kafka =="
docker-compose -f assets/docker-compose/docker-compose-kafka.yml up -d
echo "== kafka started =="

docker-compose -f assets/docker-compose/docker-compose-monitoring.yml build grafana
docker-compose -f assets/docker-compose/docker-compose-monitoring.yml --env-file=assets/docker-compose/.env up -d

checkCassandra() {
  docker exec cassandra cqlsh -e 'describe tables' > /dev/null 2>&1
}

while ! checkCassandra; do
    sleep 1
done

sh assets/scripts/cassandra/setup_cassandra.sh


docker-compose -f assets/docker-compose/docker-compose-kafka.yml  exec kafka kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 30 --topic DGR-COP-ACTIVIDADES
docker-compose -f assets/docker-compose/docker-compose-kafka.yml  exec kafka kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 30 --topic DGR-COP-SUJETO-TRI
docker-compose -f assets/docker-compose/docker-compose-kafka.yml  exec kafka kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 30 --topic DGR-COP-OBLIGACIONES-TRI




