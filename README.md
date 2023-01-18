# How to run  
## in development mode  *
**Run the following commands on different consoles**


Console [1] Start infrastructure (Cassandra, Kafka)

`sh assets/docker-compose/dev-lite/infrastructure.sh `

Console [2] Start first node of writeside

`sh assets/docker-compose/dev-lite/seed.sh`

Console [3] Start second node of writeside

`sh assets/docker-compose/dev-lite/node1.sh`

Console [4] Start third node of writeside

`sh assets/docker-compose/dev-lite/node2.sh`

Console [5] Start first node of readside

`sh assets/docker-compose/dev-lite/readside.sh `

Console [6] After all nodes have been started, start consumer

`sh assets/scripts/start_consumers.sh `

Console [7] To stop the local environment

`sh assets/docker-compose/vm/stop_all.sh` 

## in docker-compose
sh assets/docker-compose/vm/stop_all.sh 
sh assets/docker-compose/vm/start_all.sh 

## in kubernetes  
sh assets/k8s/stop_all.sh 
sh assets/k8s/start_all.sh 

# How to use  
## How to use: 1. publish a message to Kafka
This will publish a message to the **DGR-COP-OBLIGACIONES-TRI** topic, 

using the JSON file at **assets/examples/DGR-COP-OBLIGACIONES-TRI.json**.
```bash
kafkacat -P -b 0.0.0.0:9092 -t DGR-COP-OBLIGACIONES-TRI assets/examples/DGR-COP-OBLIGACIONES-TRI.json 
```
## How to use: 2. query the actors using HTTP
```bash
curl 0.0.0.0:8081/state/sujeto/1
```
```bash
curl 0.0.0.0:8081/state/sujeto/1/objeto/1/tipo/I
```
```bash
curl 0.0.0.0:8081/state/sujeto/1/objeto/1/tipo/I/obligacion/1
```
This would hit the seed node, which is exposed at the port 8081.

Other nodes like node1 and node2, you can find them at 8082 and 8082, respectively.
![enter image description here](https://i.imgur.com/sNi7miF.png)
## How to use: 3. explore the readside projections at Cassandra
#### on Docker
``
docker exec -it cassandra bash -c 'cqlsh -u cassandra -p cassandra'
``
#### on Kubernetes
``
kubectl exec -i $pod_name bash -c 'cqlsh -u cassandra -p cassandra'
``

This will get you _inside_ Cassandra, where you can run queries like the followings:
```bash
select * from read_side.buc_sujeto;
```
```bash
select * from read_side.buc_sujeto_objeto;
```
```bash
select * from read_side.buc_obligaciones;
```
![enter image description here](https://i.imgur.com/jaiksfn.png)
## How to use: 4. explore the dashboards at Grafana
### [0.0.0.0:3000](0.0.0.0:3000)  || user: _admin_ || password: _admin_


![enter image description here](https://i.imgur.com/W4E5dMj.png)


# Testing  
_writeside, readside, and integration_
```bash
sbt test
```

_writeside only_
```bash
sbt pcs/test
```
_readside only_
```bash
sbt readside/test
```
_integration only_
```bash
sbt it/test
```

