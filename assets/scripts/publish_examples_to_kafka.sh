#!/usr/bin/env bash
# this forces usage of bash because for loop is bash-only
# in case this script is called using sh, it will re-call itself using bash.
if [ ! "$BASH_VERSION" ] ; then
    exec /bin/bash "$0" "$@"
fi

ls

source aliases.sh;

pcs.infrastructure.publish_to_kafka DGR-COP-ACTIVIDADES
pcs.infrastructure.publish_to_kafka DGR-COP-CALENDARIO
pcs.infrastructure.publish_to_kafka DGR-COP-DECJURADAS
pcs.infrastructure.publish_to_kafka DGR-COP-DOMICILIO-OBJ-ANT
pcs.infrastructure.publish_to_kafka DGR-COP-DOMICILIO-OBJ-TRI
pcs.infrastructure.publish_to_kafka DGR-COP-DOMICILIO-SUJ-ANT
pcs.infrastructure.publish_to_kafka DGR-COP-DOMICILIO-SUJ-TRI
pcs.infrastructure.publish_to_kafka DGR-COP-ETAPROCESALES-ANT
pcs.infrastructure.publish_to_kafka DGR-COP-ETAPROCESALES-TRI
pcs.infrastructure.publish_to_kafka DGR-COP-EXENCIONES
pcs.infrastructure.publish_to_kafka DGR-COP-JUICIOS-ANT
pcs.infrastructure.publish_to_kafka DGR-COP-JUICIOS-TRI
pcs.infrastructure.publish_to_kafka DGR-COP-OBJETOS-ANT
pcs.infrastructure.publish_to_kafka DGR-COP-OBJETOS-TRI
pcs.infrastructure.publish_to_kafka DGR-COP-OBLIGACIONES-ANT
pcs.infrastructure.publish_to_kafka DGR-COP-OBLIGACIONES-TRI
pcs.infrastructure.publish_to_kafka DGR-COP-PARAMPLAN-ANT
pcs.infrastructure.publish_to_kafka DGR-COP-PARAMPLAN-TRI
pcs.infrastructure.publish_to_kafka DGR-COP-PARAMRECARGO-ANT
pcs.infrastructure.publish_to_kafka DGR-COP-PARAMRECARGO-TRI
pcs.infrastructure.publish_to_kafka DGR-COP-PLANES-ANT
pcs.infrastructure.publish_to_kafka DGR-COP-PLANES-TRI
pcs.infrastructure.publish_to_kafka DGR-COP-SUBASTAS
pcs.infrastructure.publish_to_kafka DGR-COP-SUJETO-ANT
pcs.infrastructure.publish_to_kafka DGR-COP-SUJETO-TRI
pcs.infrastructure.publish_to_kafka DGR-COP-TRAMITES