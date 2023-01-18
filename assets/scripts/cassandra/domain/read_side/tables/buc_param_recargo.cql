CREATE TABLE IF NOT EXISTS read_side.buc_param_recargo (
    bpr_indice text,
    bpr_tipo_indice text,
    bpr_descripcion text,
    bpr_fecha_desde date,
    bpr_fecha_hasta date,
    bpr_valor float,
    bpr_concepto text,
    bpr_impuesto text,
    bpr_periodo text,
    PRIMARY KEY (bpr_indice, bpr_tipo_indice, bpr_fecha_desde, bpr_periodo, bpr_concepto, bpr_impuesto)
) WITH CLUSTERING ORDER BY (bpr_tipo_indice ASC)
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
