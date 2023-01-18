
kubectl delete -f assets/k8s/infra/kafka.yml
kubectl delete -f assets/k8s/infra/cassandra.yml


kubectl delete -f assets/k8s/pcs/pcs-rbac.yml
envsubst < assets/k8s/pcs/pcs-deployment.yml | kubectl delete -f -
kubectl delete -f assets/k8s/pcs/pcs-service.yml
kubectl delete -f assets/k8s/pcs/pcs-service-monitor.yml


helm uninstall prometheus
helm uninstall cassandra
