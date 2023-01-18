
### pcs
  ### pcs.infrastructure
    alias pcs.infrastructure.cqlsh="docker exec -it cassandra bash -c 'cqlsh -u cassandra -p cassandra'"

    pcs.infrastructure.publish_to_kafka() {
        if [ -z "$1" ]
        then
          echo "No argument supplied: Try 'publish_to_kafka DGR-COP-SUJETO-TRI' or even better, use pcs.helper.kafka.publish_sujeto_tri_example"
        else
          kafkacat -P -b 0.0.0.0:9092 -t "$1" assets/examples/$1.json
          echo "Published $1 to kafka"
        fi
    }

  ### pcs.helper
    ### pcs.helper.kafka
    alias pcs.helper.kafka.publish_actividades_example="pcs.infrastructure.publish_to_kafka DGR-COP-ACTIVIDADES"

    alias pcs.helper.kafka.publish_calendario_example="pcs.infrastructure.publish_to_kafka DGR-COP-CALENDARIO"

    alias pcs.helper.kafka.publish_decjuradas_example="pcs.infrastructure.publish_to_kafka DGR-COP-DECJURADAS"

    alias pcs.helper.kafka.publish_domicilio_obj_ant_example="pcs.infrastructure.publish_to_kafka DGR-COP-DOMICILIO-OBJ-ANT"
    alias pcs.helper.kafka.publish_domicilio_obj_tri_example="pcs.infrastructure.publish_to_kafka DGR-COP-DOMICILIO-OBJ-TRI"

    alias pcs.helper.kafka.publish_domicilio_suj_ant_example="pcs.infrastructure.publish_to_kafka DGR-COP-DOMICILIO-SUJ-ANT"
    alias pcs.helper.kafka.publish_domicilio_suj_tri_example="pcs.infrastructure.publish_to_kafka DGR-COP-DOMICILIO-SUJ-TRI"

    alias pcs.helper.kafka.publish_etaprocesales_ant_example="pcs.infrastructure.publish_to_kafka DGR-COP-ETAPROCESALES-ANT"
    alias pcs.helper.kafka.publish_etaprocesales_tri_example="pcs.infrastructure.publish_to_kafka DGR-COP-ETAPROCESALES-TRI"

    alias pcs.helper.kafka.publish_exenciones_example="pcs.infrastructure.publish_to_kafka DGR-COP-EXENCIONES"

    alias pcs.helper.kafka.publish_juicios_ant_example="pcs.infrastructure.publish_to_kafka DGR-COP-JUICIOS-ANT"
    alias pcs.helper.kafka.publish_juicios_tri_example="pcs.infrastructure.publish_to_kafka DGR-COP-JUICIOS-TRI"

    alias pcs.helper.kafka.publish_objetos_ant_example="pcs.infrastructure.publish_to_kafka DGR-COP-OBJETOS-ANT"
    alias pcs.helper.kafka.publish_objetos_tri_example="pcs.infrastructure.publish_to_kafka DGR-COP-OBJETOS-TRI"

    alias pcs.helper.kafka.publish_obligacion_ant_example="pcs.infrastructure.publish_to_kafka DGR-COP-OBLIGACIONES-ANT"
    alias pcs.helper.kafka.publish_obligacion_tri_example="pcs.infrastructure.publish_to_kafka DGR-COP-OBLIGACIONES-TRI"

    alias pcs.helper.kafka.publish_paramplan_ant_example="pcs.infrastructure.publish_to_kafka DGR-COP-PARAMPLAN-ANT"
    alias pcs.helper.kafka.publish_paramplan_tri_example="pcs.infrastructure.publish_to_kafka DGR-COP-PARAMPLAN-TRI"

    alias pcs.helper.kafka.publish_paramrecargo_ant_example="pcs.infrastructure.publish_to_kafka DGR-COP-PARAMRECARGO-ANT"

    alias pcs.helper.kafka.publish_paramrecargo_tri_example="pcs.infrastructure.publish_to_kafka DGR-COP-PARAMRECARGO-TRI"

    alias pcs.helper.kafka.publish_planes_ant_example="pcs.infrastructure.publish_to_kafka DGR-COP-PLANES-ANT"
    alias pcs.helper.kafka.publish_planes_tri_example="pcs.infrastructure.publish_to_kafka DGR-COP-PLANES-TRI"

    alias pcs.helper.kafka.publish_subastas_example="pcs.infrastructure.publish_to_kafka DGR-COP-SUBASTAS"

    alias pcs.helper.kafka.publish_sujeto_ant_example="pcs.infrastructure.publish_to_kafka DGR-COP-SUJETO-ANT"
    alias pcs.helper.kafka.publish_sujeto_ant_example="pcs.infrastructure.publish_to_kafka DGR-COP-SUJETO-TRI"

    alias pcs.helper.kafka.publish_tramites_example="pcs.infrastructure.publish_to_kafka DGR-COP-TRAMITES"

    ### pcs.helper.application
    alias pcs.helper.application.wait_ready="sh assets/scripts/wait_ready.sh"
    alias pcs.helper.application.start_consumers="sh assets/scripts/start_consumers.sh"
    alias pcs.helper.application.stop_consumers="sh assets/scripts/stop_consumers.sh"




  ### pcs.install
  pcs.install.tmux.osx() {
    brew install tmux
  }
  pcs.install.tmux.ubuntu() {
    wget -q -O - https://gist.githubusercontent.com/P7h/91e14096374075f5316e/raw/64b011f1bda145dcafe84b5321255d6ed4609c07/tmux__Ubuntu__build_from_source.sh | bash
    source ~/.bashrc
  }