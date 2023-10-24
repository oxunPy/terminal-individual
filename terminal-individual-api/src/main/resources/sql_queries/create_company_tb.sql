create table company
(
    id          serial primary key,
    created     timestamp without time zone,
    status      integer,
    updated     timestamp without time zone,
    company_name varchar(50),
    logo_id integer,
    favicon_id integer,
    is_main boolean,
    phone_number varchar(50),
    motto text,
    director varchar(50),
    manager varchar(50),
    telegram_contact varchar(50),
    email varchar(50),
    constraint fk_logo foreign key(logo_id)
        references as_file(id),
    constraint fk_favicon foreign key(favicon_id)
        references as_file(id)
);

-- alter table company
-- add constraint fk_logo foreign key(logo_id)
-- references as_file(id)
