CREATE TABLE read_side.buc_sujeto (
    suj_identificador text PRIMARY KEY,
    suj_cat_suj_id int,
    suj_denominacion text,
    suj_dfe text,
    suj_direccion text,
    suj_email text,
    suj_id_externo text,
    suj_otros_atributos map<text, text>,
    suj_riesgo_fiscal text,
    suj_saldo float,
    suj_situacion_fiscal text,
    suj_telefono text,
    suj_tipo text,
    suj_canal_origen text
) WITH bloom_filter_fp_chance = 0.01
    AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
    AND comment = ''
    AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
    AND compression = {'sstable_compression': 'LZ4Compressor','chunk_length_in_kb': '64'}
    AND crc_check_chance = 1.0
    AND dclocal_read_repair_chance = 0.1
    AND default_time_to_live = 0
    AND gc_grace_seconds = 100
    AND max_index_interval = 2048
    AND memtable_flush_period_in_ms = 0
    AND min_index_interval = 128
    AND read_repair_chance = 0.0
    AND speculative_retry = '99PERCENTILE';


