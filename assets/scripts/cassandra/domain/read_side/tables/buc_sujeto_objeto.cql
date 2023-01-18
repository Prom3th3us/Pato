CREATE TABLE read_side.buc_sujeto_objeto (
    soj_suj_identificador text,
    soj_tipo_objeto text,
    soj_identificador text,
    soj_cat_soj_id text,
    soj_descripcion text,
    soj_estado text,
    soj_fecha_fin date,
    soj_fecha_inicio date,
    soj_id_externo text,
    soj_otros_atributos map<text, text>,
    soj_saldo float,
    soj_base_imponible float,
    soj_adherido_debito text,
    soj_cant_cuotas_pagadas text,
    soj_canal_origen text,
    soj_subtipo text,
    soj_identificador_2 text,
    PRIMARY KEY (soj_suj_identificador, soj_tipo_objeto, soj_identificador)
) WITH CLUSTERING ORDER BY (soj_tipo_objeto ASC, soj_identificador ASC)
    AND bloom_filter_fp_chance = 0.01
    AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
    AND comment = ''
    AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
    AND compression = {'sstable_compression': 'LZ4Compressor', 'chunk_length_in_kb': '64'}
    AND crc_check_chance = 1.0
    AND dclocal_read_repair_chance = 0.1
    AND default_time_to_live = 0
    AND gc_grace_seconds = 100
    AND max_index_interval = 2048
    AND memtable_flush_period_in_ms = 0
    AND min_index_interval = 128
    AND read_repair_chance = 0.0
    AND speculative_retry = '99PERCENTILE';


