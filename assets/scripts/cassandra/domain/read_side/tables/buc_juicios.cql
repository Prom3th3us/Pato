CREATE TABLE IF NOT EXISTS read_side.buc_juicios (
    bju_suj_identificador text,
    bju_soj_tipo_objeto text,
    bju_soj_identificador text,
    bju_jui_id int,
    bju_nro_externo text,
    bju_capital float,
    bju_estado text,
    bju_fiscalizada text,
    bju_gastos float,
    bju_gastos_mart float,
    bju_honorarios float,
    bju_honorarios_mart float,
    bju_inicio_demanda date,
    bju_interes_punit float,
    bju_interes_resar float,
    bju_otros_atributos map<text, text>,
    bju_pcr_id int,
    bju_porcentaje_iva float,
    bju_procurador text,
    bju_tipo text,
    bju_total float,
    PRIMARY KEY (bju_suj_identificador, bju_soj_tipo_objeto, bju_soj_identificador, bju_jui_id)
) WITH CLUSTERING ORDER BY (bju_soj_tipo_objeto ASC, bju_soj_identificador ASC, bju_jui_id ASC)
    AND bloom_filter_fp_chance = 0.01
    AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
    AND comment = ''
    AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
    AND compression = {'sstable_compression': 'LZ4Compressor', 'chunk_length_in_kb': '64'}
    AND crc_check_chance = 1.0
    AND dclocal_read_repair_chance = 0.1
    AND default_time_to_live = 0
    AND gc_grace_seconds = 864000
    AND max_index_interval = 2048
    AND memtable_flush_period_in_ms = 0
    AND min_index_interval = 128
    AND read_repair_chance = 0.0
    AND speculative_retry = '99PERCENTILE';
