#akka.messages

# AND compaction = {
#    'class' : 'SizeTieredCompactionStrategy',
#    'enabled' : true,
#    'tombstone_compaction_interval' : 86400,
#    'tombstone_threshold' : 0.2,
#    'unchecked_tombstone_compaction' : false,
#    'bucket_high' : 1.5,
#    'bucket_low' : 0.5,
#    'max_threshold' : 32,
#    'min_threshold' : 4,
#    'min_sstable_size' : 50
#    };

snapshots
# AND compaction = {
#    'class' : 'SizeTieredCompactionStrategy',
#    'enabled' : true,
#    'tombstone_compaction_interval' : 86400,
#    'tombstone_threshold' : 0.2,
#    'unchecked_tombstone_compaction' : false,
#    'bucket_high' : 1.5,
#    'bucket_low' : 0.5,
#    'max_threshold' : 32,
#    'min_threshold' : 4,
#    'min_sstable_size' : 50
#    };


tag_views
compaction = {
    'class' : 'SizeTieredCompactionStrategy',
    'enabled' : true,
    'tombstone_compaction_interval' : 86400,
    'tombstone_threshold' : 0.2,
    'unchecked_tombstone_compaction' : false,
    'bucket_high' : 1.5,
    'bucket_low' : 0.5,
    'max_threshold' : 32,
    'min_threshold' : 4,
    'min_sstable_size' : 50
    };