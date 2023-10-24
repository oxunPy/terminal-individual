create table as_file(
    id serial primary key,
    created timestamp,
    status integer,
    updated timestamp,
    file_name varchar(255),
    org_file_name varchar(255)
)