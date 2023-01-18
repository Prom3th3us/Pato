
CREATE TABLE read_side.buc_declaraciones_juradas (
    bdj_suj_identificador text,
    bdj_soj_tipo_objeto text,
    bdj_soj_identificador text,
    bdj_ddj_id text,
    bdj_cuota text,
    bdj_estado text,
    bdj_fiscalizada text,
    bdj_impuesto_determinado float,
    bdj_obn_id text,
    bdj_otros_atributos map<text, text>,
    bdj_percepciones float,
    bdj_periodo text,
    bdj_prorroga date,
    bdj_recaudaciones float,
    bdj_retenciones float,
    bdj_tipo text,
    bdj_total float,
    bdj_vencimiento date,
    PRIMARY KEY (bdj_suj_identificador, bdj_soj_tipo_objeto, bdj_soj_identificador, bdj_ddj_id)
) WITH CLUSTERING ORDER BY (bdj_soj_tipo_objeto ASC, bdj_soj_identificador ASC, bdj_ddj_id ASC)
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



