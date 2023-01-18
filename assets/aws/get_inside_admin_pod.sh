
kubectl exec -it $(kubectl get pod -l app=ubuntu -o jsonpath='{.items[0].metadata.name}') -- bash