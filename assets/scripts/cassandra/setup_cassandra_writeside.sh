docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/infrastructure/akka/keyspaces/akka.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/infrastructure/akka/keyspaces/akka_snapshot.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/infrastructure/akka/tables/all_persistence_ids.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/infrastructure/akka/tables/messages.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/infrastructure/akka/tables/metadata.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/infrastructure/akka/tables/snapshots.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/infrastructure/akka/tables/tag_scanning.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/infrastructure/akka/tables/tag_views.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/infrastructure/akka/tables/tag_write_progress.cql

