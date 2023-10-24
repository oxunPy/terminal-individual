create table product_barcode
(
    id          serial primary key,
    created     timestamp,
    status      integer,
    updated     timestamp,
    server_id   integer,
    barcode     varchar(255),
    description varchar(255),
    product_id  integer
);

alter table product_barcode
add constraint fk_product
foreign key(product_id)
references product(id)
on delete cascade

