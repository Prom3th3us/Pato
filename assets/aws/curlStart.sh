

tmux new-window  -n 'port forward Grafana'
tmux send-keys -t 1 "\
      kubectl port-forward $(kubectl get pod -l app.kubernetes.io/name=grafana -o jsonpath="{.items[0].metadata.name}")  3000; \
 " Enter

tmux new-window  -n 'port forward akkaPod1' \;
tmux send-keys -t 1 "
      kubectl port-forward $(kubectl get pod -l app=pcs-cluster -o jsonpath='{.items[0].metadata.name}') 8081:8081; \
" Enter

tmux new-window  -n 'port forward akkaPod1 ' \;
tmux send-keys -t 1 "
      kubectl port-forward $(kubectl get pod -l app=pcs-cluster -o jsonpath='{.items[1].metadata.name}') 8082:8081; \
" Enter

tmux new-window  -n 'port forward akkaPod2 ' \;
tmux send-keys -t 1 "
      kubectl port-forward $(kubectl get pod -l app=pcs-cluster -o jsonpath='{.items[2].metadata.name}') 8083:8081; \
" Enter


tmux new-window  -n 'START ' \; split-window -d \;
tmux send-keys -t 1 "
      sleep 10; \
      curl -X POST http://0.0.0.0:8081/kafka/start/DGR-COP-SUJETO-TRI; \
      curl -X POST http://0.0.0.0:8082/kafka/start/DGR-COP-SUJETO-TRI; \
      curl -X POST http://0.0.0.0:8083/kafka/start/DGR-COP-SUJETO-TRI; \
      curl -X POST http://0.0.0.0:8081/kafka/start/DGR-COP-OBLIGACIONES-TRI; \
      curl -X POST http://0.0.0.0:8082/kafka/start/DGR-COP-OBLIGACIONES-TRI; \
      curl -X POST http://0.0.0.0:8083/kafka/start/DGR-COP-OBLIGACIONES-TRI; \
" Enter
tmux send-keys -t 2 'sleep 20; sh assets/aws/grafana.sh' Enter





