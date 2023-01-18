
tmux new-window  -n 'local demo'
tmux send-keys -t 1 'echo "Starting up infrastructure"'  Enter
tmux send-keys -t 1 'source aliases.sh' Enter

tmux send-keys -t 1 "\
    sh assets/environments/dev_lite/infrastructure.sh; \
    pcs.helper.application.wait_ready 8081; \
    pcs.helper.application.start_consumers; \
    pcs.infrastructure.publish_to_kafka DGR-COP-SUJETO-TRI; \
    pcs.infrastructure.publish_to_kafka DGR-COP-OBLIGACIONES-TRI; \
    pcs.infrastructure.publish_to_kafka DGR-COP-ACTIVIDADES; \
    pcs.infrastructure.publish_to_kafka DGR-COP-DOMICILIO-SUJ-TRI; \
 " Enter

tmux new-window  -n 'writeside' \; split-window -d \;
tmux send-keys -t 1 "
    export SEED_NODES=akka://PersonClassificationService@0.0.0.0:2551; \
    export CLUSTER_PORT=2551; \
    export MANAGEMENT_PORT=8551; \
    export HTTP_PORT=8081; \
    export PROMETHEUS_PORT=5001; \
    export KAMON_STATUS_PAGE=5266; \
    sbt pcs/run; \
" Enter
tmux send-keys -t 2 "\
    sh assets/scripts/query_api.sh \
    0.0.0.0:8081/state/sujeto/1
" Enter

tmux new-window  -n 'readside ' \; split-window -d \;
tmux send-keys -t 1 "
    export SEED_NODES=akka://PersonClassificationServiceReadSide@0.0.0.0:2554; \
    export CLUSTER_PORT=2554; \
    export MANAGEMENT_PORT=8554; \
    export HTTP_PORT=8084; \
    export PROMETHEUS_PORT=5004; \
    export KAMON_STATUS_PAGE=5269; \
    export PROJECTIONIST_PARALELLISM=1; \
    sbt readside/run; \
" Enter
tmux send-keys -t 2 'sh assets/scripts/query_cassandra_table.sh buc_obligaciones' Enter
