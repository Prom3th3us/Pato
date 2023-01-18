

CREATE KEYSPACE IF NOT EXISTS read_side
WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }
AND durable_writes = true;
