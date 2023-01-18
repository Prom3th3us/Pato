
CREATE TABLE IF NOT EXISTS read_side.buc_subastas (
    bsb_suj_identificador_adq text,
    bsb_soj_tipo_objeto text,
    bsb_soj_identificador text,
    bsb_sub_id text,
    bsb_auto text,
    bsb_fecha_fin date,
    bsb_fecha_inicio date,
    bsb_suj_identificador_sub text,
    bsb_tipo text,
    PRIMARY KEY (bsb_suj_identificador_adq, bsb_soj_tipo_objeto, bsb_soj_identificador, bsb_sub_id)
) WITH CLUSTERING ORDER BY (bsb_soj_tipo_objeto ASC, bsb_soj_identificador ASC, bsb_sub_id ASC)
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

