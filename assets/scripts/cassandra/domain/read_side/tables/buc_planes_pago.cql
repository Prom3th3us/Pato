
CREATE TABLE IF NOT EXISTS read_side.buc_planes_pago (
    bpl_suj_identificador text,
    bpl_soj_tipo_objeto text,
    bpl_soj_identificador text,
    bpl_pln_id int,
    bpl_cantidad_cuotas int,
    bpl_estado text,
    bpl_fecha_act_deuda date,
    bpl_fecha_emision date,
    bpl_importe_a_financiar float,
    bpl_importe_anticipo float,
    bpl_importe_financiado float,
    bpl_nro_referencia text,
    bpl_tipo text,
    bpl_otros_atributos map<text,text>,
    PRIMARY KEY (bpl_suj_identificador, bpl_soj_tipo_objeto, bpl_soj_identificador, bpl_pln_id)
) WITH CLUSTERING ORDER BY (bpl_soj_tipo_objeto ASC, bpl_soj_identificador ASC, bpl_pln_id ASC)
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

