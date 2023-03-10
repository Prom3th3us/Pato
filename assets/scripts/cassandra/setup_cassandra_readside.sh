
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/infrastructure/cqrs/keyspaces/akka_projection.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/infrastructure/cqrs/tables/offset_store.cql

docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/domain/read_side/keyspaces/read_side.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/domain/read_side/tables/buc_actividades_sujeto.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/domain/read_side/tables/buc_contactos.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/domain/read_side/tables/buc_declaraciones_juradas.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/domain/read_side/tables/buc_domicilios_objeto.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/domain/read_side/tables/buc_domicilios_sujeto.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/domain/read_side/tables/buc_exenciones.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/domain/read_side/tables/buc_juicios.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/domain/read_side/tables/buc_obligaciones.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/domain/read_side/tables/buc_planes_pago.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/domain/read_side/tables/buc_subastas.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/domain/read_side/tables/buc_sujeto.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/domain/read_side/tables/buc_sujeto_objeto.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/domain/read_side/tables/buc_tramites.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/domain/read_side/tables/buc_etapas_procesales.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/domain/read_side/tables/buc_param_plan.cql
docker exec -i cassandra bash -c 'cqlsh -u cassandra -p cassandra' < assets/scripts/cassandra/domain/read_side/tables/buc_param_recargo.cql
