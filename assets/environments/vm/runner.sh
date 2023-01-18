
tmux new-window  -n 'local demo'
tmux send-keys -t 1 'echo "Starting up infrastructure"'  Enter
tmux send-keys -t 1 'source aliases.sh' Enter

tmux send-keys -t 1 "\
    sh assets/environments/vm/infrastructure.sh; \
    sh assets/scripts/wait_ready.sh 8081; \
    curl -X POST http://0.0.0.0:8081/kafka/start; \
    pcs.infrastructure.publish_to_kafka DGR-COP-SUJETO-TRI; \
    pcs.infrastructure.publish_to_kafka DGR-COP-OBLIGACIONES-TRI; \
    pcs.infrastructure.publish_to_kafka DGR-COP-ACTIVIDADES; \
    pcs.infrastructure.publish_to_kafka DGR-COP-DOMICILIO-SUJ-TRI; \
 " Enter

tmux new-window  -n 'writeside' \; split-window -d \;
tmux send-keys -t 1 "\
    sbt 'pcs/docker:publishLocal'; \
    docker-compose -f assets/environments/vm/docker-compose.yml up -d seed  \
" Enter
tmux send-keys -t 2 "\
    sh assets/scripts/query_api.sh \
    0.0.0.0:8081/state/sujeto/1
" Enter

tmux new-window  -n 'readside ' \; split-window -d \;
tmux send-keys -t 1 "\
    sh assets/scripts/wait_ready.sh 8081; \
    sbt 'readside/docker:publishLocal'; \
    docker-compose -f assets/environments/vm/docker-compose.yml up -d readside; \
    curl -X POST http://0.0.0.0:8084/kafka/start; \
" Enter
tmux send-keys -t 2 'sh assets/scripts/query_cassandra_table.sh buc_obligaciones' Enter
