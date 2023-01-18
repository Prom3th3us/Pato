 #!/bin/bash
​
for we in `seq $1 $2`
do
    y=`date '+%Y'`
    m=`date '+%m'`
    d=`date '+%d'`
    h=`date '+%H'`
    #h="15"
    mi=`date '+%M'`
    #mi="04"
    s=`date '+%S'`
    n=`date '+%3N'`
    resto="000001084718625"
    #echo "$2 $we $2 $1"
    #`expr $2 - $we * 100 / ($2 - $1) expr: syntax error: unexpected argument «CalculateLag.txt»
    echo "Obligacion $we"
    #sleep $3
    kafkacat -b 0.0.0.0:9092 \
                -t "DGR-COP-OBLIGACIONES-TRI" \
                -P <<EOF
{"EV_ID":"$y$m$d$h$mi$s$n$resto","BOB_SUJ_IDENTIFICADOR": "20-06411831-7","BOB_SOJ_TIPO_OBJETO": "E","BOB_SOJ_IDENTIFICADOR": "210246428","BOB_OBN_ID": "2015000$we","BOB_SALDO": "1","BOB_CUOTA": "5","BOB_ESTADO": "ADMINISTRATIVA","BOB_FISCALIZADA": "N","BOB_INDICE_INT_PUNIT": null,"BOB_INDICE_INT_RESAR": null,"BOB_INTERES_PUNIT": null,"BOB_INTERES_RESAR": null,"BOB_JUI_ID": null,"BOB_PERIODO": "2015","BOB_PLN_ID": null,"BOB_PRORROGA": "2015-05-13 00:00:00.0","BOB_TIPO": "tributaria","BOB_TOTAL": null,"BOB_VENCIMIENTO": "2015-05-13 00:00:00.0","BOB_CAPITAL": "1260","BOB_CONCEPTO": "700","BOB_IMPUESTO": "600","FECHA_BAJA": "2021-11-26 11:44:18.0","BOB_ADHERIDO_DEBITO": "N","BOB_OTROS_ATRIBUTOS": {"BOB_DETALLES": [{"EVO_OBN_PEO_ID_MATERIAL": "APEX","BOB_MUNICIPIO": null,"BOB_INTERES_FINANCIACION": null,"JUICIO_MULTIOBJETO": "N","RULE_NUMBER": "1","EVO_OBN_PEO_ID_FORMAL": "NC","PLAN_MULTIOBJETO": "N"}]}}
EOF
    
done
