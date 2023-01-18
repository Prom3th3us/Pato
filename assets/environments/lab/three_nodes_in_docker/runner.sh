
tmux new-window  -n 'local demo'
tmux send-keys -t 1 'echo "Starting up infrastructure"'  Enter
tmux send-keys -t 1 'source aliases.sh' Enter

tmux send-keys -t 1 "\
    sh assets/environments/lab/three_nodes_in_docker/infrastructure.sh; \
    sh assets/scripts/wait_ready.sh 8081; \
    sh assets/scripts/wait_ready.sh 8082; \
    sh assets/scripts/wait_ready.sh 8083; \
    pcs.helper.application.start_consumers; \
    pcs.infrastructure.publish_to_kafka DGR-COP-SUJETO-TRI; \
    pcs.infrastructure.publish_to_kafka DGR-COP-OBLIGACIONES-TRI; \
    pcs.infrastructure.publish_to_kafka DGR-COP-ACTIVIDADES; \
    pcs.infrastructure.publish_to_kafka DGR-COP-DOMICILIO-SUJ-TRI; \
 " Enter

tmux new-window  -n 'writeside' \; split-window -d \;
tmux send-keys -t 1 "\
    sbt 'pcs/docker:publishLocal'; \
    docker-compose -f assets/docker-compose/docker-compose.yml up -d seed  node1 node2; \
" Enter
tmux send-keys -t 2 "\
    sh assets/scripts/query_api.sh \
    0.0.0.0:8081/state/sujeto/1
" Enter

tmux new-window  -n 'readside ' \; split-window -d \;
tmux send-keys -t 1 "\
    sbt 'readside/docker:publishLocal'; \
    docker-compose -f assets/environments/one_readside_three_nodes_writeside_in_docker/docker-compose.yml up -d readside1 readside2 readside3; \
" Enter
tmux send-keys -t 2 'sh assets/scripts/query_cassandra_table.sh buc_obligaciones' Enter
