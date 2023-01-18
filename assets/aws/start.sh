

git pull origin master

git commit -m "m"


echo "INSTALLING HELM CASSANDRA"
# kubectl apply -f assets/k8s/infra/cassandra.yml
helm repo add incubator https://kubernetes-charts-incubator.storage.googleapis.com
helm install cassandra incubator/cassandra --version 0.15.2 -f assets/k8s/infra/cassandra.values.yml
echo "INSTALLED HELM CASSANDRA"

sbt pcs/docker:publishLocal
aws ecr get-login-password --region us-west-2 | docker login --username AWS --password-stdin 099925565557.dkr.ecr.us-west-2.amazonaws.com
docker tag pcs/pcs:1.0 099925565557.dkr.ecr.us-west-2.amazonaws.com/pcs-akka:latest
docker push 099925565557.dkr.ecr.us-west-2.amazonaws.com/pcs-akka:latest
export IMAGE=099925565557.dkr.ecr.us-west-2.amazonaws.com/pcs-akka:latest

sh assets/aws/setup_cassandra.sh

kubectl apply -f assets/k8s/infra/kafka.yml

curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 | bash
helm repo add stable https://kubernetes-charts.storage.googleapis.com
helm install prometheus stable/prometheus-operator --namespace copernico

sleep 60

export kafkaPod=$(kubectl get pod -l "app=kafka" -o jsonpath='{.items[0].metadata.name}')
export createSujetoTri='kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 30 --topic DGR-COP-SUJETO-TRI'
kubectl exec $kafkaPod -- $createSujetoTri
export createObligacionTri='kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 30 --topic DGR-COP-OBLIGACIONES-TRI'
kubectl exec $kafkaPod -- $createObligacionTri


export REPLICAS=3
export PARALELLISM=1024
export IMAGE=099925565557.dkr.ecr.us-west-2.amazonaws.com/pcs-akka:latest
kubectl apply -f assets/k8s/pcs/pcs-rbac.yml
envsubst < assets/k8s/pcs/pcs-deployment.yml | kubectl apply -f -
kubectl apply -f assets/k8s/pcs/pcs-service.yml
kubectl apply -f assets/k8s/pcs/pcs-service-monitor.yml

export kafka_cluster_ip=$(kubectl get svc kafka-internal -ojsonpath='{.spec.clusterIP}')
sbt 'it/runMain generator.KafkaEventProducer '"$kafka_cluster_ip"':29092 DGR-COP-SUJETO-TRI 1 1000000'
# export kafka_cluster_ip=$(kubectl get svc kafka-internal -ojsonpath='{.spec.clusterIP}')
# sbt 'it/runMain generator.KafkaEventProducer '"$kafka_cluster_ip"':29092 DGR-COP-OBLIGACIONES-TRI 1 1000000'


