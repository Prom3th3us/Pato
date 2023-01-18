
git pull origin master

git commit -m "m"


kubectl delete -f assets/k8s/pcs/pcs-rbac.yml
envsubst < assets/k8s/pcs/pcs-deployment.yml | kubectl delete -f -
kubectl delete -f assets/k8s/pcs/pcs-service.yml
kubectl delete -f assets/k8s/pcs/pcs-service-monitor.yml



sbt pcs/docker:publishLocal
aws ecr get-login-password --region us-west-2 | docker login --username AWS --password-stdin 099925565557.dkr.ecr.us-west-2.amazonaws.com
docker tag pcs/pcs:1.0 099925565557.dkr.ecr.us-west-2.amazonaws.com/pcs-akka:latest
docker push 099925565557.dkr.ecr.us-west-2.amazonaws.com/pcs-akka:latest
export IMAGE=099925565557.dkr.ecr.us-west-2.amazonaws.com/pcs-akka:latest


export REPLICAS=3
export PARALELLISM=1024
export IMAGE=099925565557.dkr.ecr.us-west-2.amazonaws.com/pcs-akka:latest
kubectl apply -f assets/k8s/pcs/pcs-rbac.yml
envsubst < assets/k8s/pcs/pcs-deployment.yml | kubectl apply -f -
kubectl apply -f assets/k8s/pcs/pcs-service.yml
kubectl apply -f assets/k8s/pcs/pcs-service-monitor.yml
