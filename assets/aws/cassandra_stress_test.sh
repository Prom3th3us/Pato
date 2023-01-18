
echo '

 # Last time the stress test showed this: 11K writes per second on three nodes!
 Count    Latency (p99)  1min (req/s) |   Count  Latency (p99)  1min (req/s) |   Count  Latency (p99)  1min (req/s) |   Count  1min (errors/s)
 1482662          38.38      11726.55 | 1480974          35.19      11696.39 |       0              0             0 |       0                0

'


echo '
apiVersion: batch/v1
kind: Job
metadata:
  name: tlp-stress
spec:
  template:
    spec:
      restartPolicy: OnFailure
      containers:
        - name: tlp-stress
          image: thelastpickle/tlp-stress
          imagePullPolicy: IfNotPresent
          args: ["run", "KeyValue", "--host", "cassandra", "--duration", "2m"]
' | kubectl apply -f -

sleep 30
kubectl logs -f $(kubectl get pods -o name --selector job-name=tlp-stress)