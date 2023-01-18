cd ../../../..
ls


export SEED_NODES=akka://PersonClassificationServiceReadSide@0.0.0.0:2554; \
    export CLUSTER_PORT=2554; \
    export MANAGEMENT_PORT=8554; \
    export HTTP_PORT=8084; \
    export PROMETHEUS_PORT=5004; \
    export KAMON_STATUS_PAGE=5269; \
    export PROJECTIONIST_PARALELLISM=1; \
    sbt readside/run;