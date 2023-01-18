CREATE TABLE IF NOT EXISTS read_side.buc_param_plan (
    bpp_rdl_id text,
    bpp_fpm_id text,
    bpp_cant_max_cuotas int,
    bpp_cant_min_cuotas int,
    bpp_decreto text,
    bpp_dias_vto_cuotas int,
    bpp_fecha_desde_deuda date,
    bpp_fecha_fin date,
    bpp_fecha_hasta_deuda date,
    bpp_fecha_inicio date,
    bpp_fpm_descripcion text,
    bpp_indice_int_financ text,
    bpp_indice_int_punit text,
    bpp_indice_int_resar text,
    bpp_monto_max_deuda float,
    bpp_monto_min_anticipo float,
    bpp_monto_min_cuota float,
    bpp_monto_min_deuda float,
    bpp_porcentaje_anticipo float,
    PRIMARY KEY (
    bpp_rdl_id,
    bpp_fpm_id,
    bpp_cant_max_cuotas,
    bpp_cant_min_cuotas,
    bpp_dias_vto_cuotas,
    bpp_fecha_desde_deuda,
    bpp_fecha_fin,
    bpp_fecha_hasta_deuda,
    bpp_fecha_inicio,
    bpp_fpm_descripcion,
    bpp_indice_int_financ,
    bpp_indice_int_punit,
    bpp_indice_int_resar,
    bpp_monto_max_deuda,
    bpp_monto_min_anticipo,
    bpp_monto_min_cuota,
    bpp_monto_min_deuda,
    bpp_porcentaje_anticipo
    )
) WITH CLUSTERING ORDER BY (bpp_fpm_id ASC)
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
