
ITER=0
for node in $(kubectl get no -o name); do
  ITER=$(expr $ITER + 1)
  kubectl label --overwrite $node index=$ITER
  done
