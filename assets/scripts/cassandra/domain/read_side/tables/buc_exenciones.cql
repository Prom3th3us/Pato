CREATE TABLE read_side.buc_exenciones (
    bex_suj_identificador text,
    bex_soj_tipo_objeto text,
    bex_soj_identificador text,
    bex_exe_id text,
    bex_descripcion text,
    bex_fecha_fin date,
    bex_fecha_inicio date,
    bex_periodo text,
    bex_porcentaje float,
    bex_tipo text,
    PRIMARY KEY (bex_suj_identificador, bex_soj_tipo_objeto, bex_soj_identificador, bex_exe_id)
) WITH CLUSTERING ORDER BY (bex_soj_tipo_objeto ASC, bex_soj_identificador ASC, bex_exe_id ASC)
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

