
CREATE TABLE IF NOT EXISTS read_side.buc_domicilios_sujeto (
    bds_suj_identificador text,
    bds_dom_id text,
    bds_barrio text,
    bds_calle text,
    bds_codigo_postal text,
    bds_dpto text,
    bds_estado text,
    bds_kilometro text,
    bds_localidad text,
    bds_lote text,
    bds_manzana text,
    bds_piso text,
    bds_provincia text,
    bds_puerta text,
    bds_tipo text,
    bds_torre text,
    bds_observaciones text,
    PRIMARY KEY (bds_suj_identificador, bds_dom_id)
) WITH CLUSTERING ORDER BY (bds_dom_id ASC)
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

