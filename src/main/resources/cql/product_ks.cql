DROP KEYSPACE IF EXISTS product_ks;
CREATE KEYSPACE IF NOT EXISTS product_ks WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor' : 1 } ;


USE product_ks;

DROP TABLE IF EXISTS product_price_by_tcin;
CREATE TABLE IF NOT EXISTS product_price_by_tcin (
    tcin bigint,
    price decimal,
    currency_code text,
    status text,
    PRIMARY KEY (tcin)
);
