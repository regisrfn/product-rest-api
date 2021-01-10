CREATE TABLE IF NOT EXISTS products
(
 product_id          uuid NOT NULL,
 product_name        varchar(50) NOT NULL,
 product_category    int NOT NULL,
 product_description text NULL,
 product_price       numeric NOT NULL,
 product_size        varchar(50) NULL,
 product_color       varchar(50) NULL,
 product_available   boolean NOT NULL,
 product_created_at  timestamp with time zone NOT NULL,
 product_brand       varchar(50) NOT NULL,
 CONSTRAINT PK_products PRIMARY KEY ( product_id )
);
