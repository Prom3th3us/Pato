

kafkacat -P -b 0.0.0.0:9092 -t DGR-COP-ACTIVIDADES assets/examples/DGR-COP-ACTIVIDADES.json

kafkacat -P -b 0.0.0.0:9092 -t DGR-COP-DECJURADAS assets/examples/DGR-COP-DECJURADAS.json

kafkacat -P -b 0.0.0.0:9092 -t DGR-COP-DOMICILIO-OBJ assets/examples/DGR-COP-DOMICILIO-OBJ.json

kafkacat -P -b 0.0.0.0:9092 -t DGR-COP-DOMICILIO-SUJ assets/examples/DGR-COP-DOMICILIO-SUJ.json

kafkacat -P -b 0.0.0.0:9092 -t DGR-COP-EXENCIONES assets/examples/DGR-COP-EXENCIONES.json

kafkacat -P -b 0.0.0.0:9092 -t DGR-COP-OBJETOS-ANT assets/examples/DGR-COP-OBJETOS-ANT.json

kafkacat -P -b 0.0.0.0:9092 -t DGR-COP-OBJETOS-TRI assets/examples/DGR-COP-OBJETOS-TRI.json

kafkacat -P -b 0.0.0.0:9092 -t DGR-COP-OBLIGACIONES-ANT assets/examples/DGR-COP-OBLIGACIONES-ANT.json

kafkacat -P -b 0.0.0.0:9092 -t DGR-COP-OBLIGACIONES-TRI assets/examples/DGR-COP-OBLIGACIONES-TRI.json

kafkacat -P -b 0.0.0.0:9092 -t DGR-COP-SUBASTAS assets/examples/DGR-COP-SUBASTAS.json

kafkacat -P -b 0.0.0.0:9092 -t DGR-COP-SUJETO-ANT assets/examples/DGR-COP-SUJETO-ANT.json

kafkacat -P -b 0.0.0.0:9092 -t DGR-COP-SUJETO-TRI assets/examples/DGR-COP-SUJETO-TRI.json


curl -X POST http://0.0.0.0:8081/kafka/start

