


eksctl create cluster \
--name prod1  \
--version 1.17  \
--region us-west-2  \
--nodegroup-name linux-nodes  \
--node-type t2.2xlarge  \
--nodes 3  \
--nodes-min 3  \
--nodes-max 3  \
--ssh-access  \
--ssh-public-key my-public-key.pub  \
--managed

kubectl apply -f assets/k8s/namespace.yml
kubectl config set-context --current --namespace=copernico
kubectl apply -f assets/aws/admin
sh assets/aws/label_nodes.sh
