cd ../../../..
ls

export SEED_NODES=akka://PersonClassificationService@0.0.0.0:2551; \
    export CLUSTER_PORT=2551; \
    export MANAGEMENT_PORT=8551; \
    export HTTP_PORT=8081; \
    export PROMETHEUS_PORT=5001; \
    export KAMON_STATUS_PAGE=5266; \
    export AKKA_KAFKA_DEBUG_MODE=OFF; \
    export APACHE_ZOOKEEPER_DEBUG_MODE=OFF; \
    export I0ITEC_ZKCLIENT_DEBUG_MODE=OFF; \
    export REFLECTION_DEBUG_MODE=OFF; \
    export APACHE_KAFKA_DEBUG_MODE=OFF; \
    export APACHE_KAFKA_APP_INFO_PARSER_DEBUG_MODE=OFF; \
    export APACHE_KAFKA_CLIENTS_NETWORK_CLIENT_DEBUG_MODE=OFF; \
    export DATASTAX_DEBUG_MODE=OFF; \
    export IO_NETTY_DEBUG_MODE=OFF; \
    export METRICS_JMX_REPORTER_DEBUG_MODE=OFF; \
    export NET_LOGSTASH_DEBUG_MODE=OFF; \
    export LOG_ROOT_LEVEL_DEBUG_MODE=OFF; \
    sbt pcs/run;

VAR SEED_NODES=akka://PersonClassificationService@0.0.0.0:2551;VAR CLUSTER_PORT=2551;VAR MANAGEMENT_PORT=8551;VAR HTTP_PORT=8081;VAR PROMETHEUS_PORT=5001;VAR KAMON_STATUS_PAGE=5266;VAR AKKA_KAFKA_DEBUG_MODE=OFF;VAR APACHE_ZOOKEEPER_DEBUG_MODE=OFF;VAR I0ITEC_ZKCLIENT_DEBUG_MODE=OFF;VAR REFLECTION_DEBUG_MODE=OFF;VAR APACHE_KAFKA_DEBUG_MODE=OFF;VAR APACHE_KAFKA_APP_INFO_PARSER_DEBUG_MODE=OFF;VAR APACHE_KAFKA_CLIENTS_NETWORK_CLIENT_DEBUG_MODE=OFF;VAR DATASTAX_DEBUG_MODE=OFF;VAR IO_NETTY_DEBUG_MODE=OFF;VAR METRICS_JMX_REPORTER_DEBUG_MODE=OFF;VAR NET_LOGSTASH_DEBUG_MODE=OFF;VAR LOG_ROOT_LEVEL_DEBUG_MODE=OFF;