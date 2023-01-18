
CREATE TABLE IF NOT EXISTS read_side.buc_domicilios_objeto (
    bdo_suj_identificador text,
    bdo_soj_tipo_objeto text,
    bdo_soj_identificador text,
    bdo_dom_id text,
    bdo_barrio text,
    bdo_calle text,
    bdo_codigo_postal text,
    bdo_dpto text,
    bdo_estado text,
    bdo_kilometro text,
    bdo_localidad text,
    bdo_lote text,
    bdo_manzana text,
    bdo_piso text,
    bdo_provincia text,
    bdo_puerta text,
    bdo_tipo text,
    bdo_torre text,
    bdo_observaciones text,
    PRIMARY KEY (bdo_suj_identificador, bdo_soj_tipo_objeto, bdo_soj_identificador, bdo_dom_id)
) WITH CLUSTERING ORDER BY (bdo_soj_tipo_objeto ASC, bdo_soj_identificador ASC, bdo_dom_id ASC)
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
