DROP FUNCTION IF EXISTS save_as_file(varchar, varchar);
CREATE OR REPLACE FUNCTION save_as_file(name varchar(255), org_name varchar)
RETURNS BIGINT LANGUAGE plpgsql AS
$$
DECLARE
    result_id bigint;
BEGIN
    INSERT INTO as_file(file_name, org_file_name, created, status)
    VALUES (name, org_name, now(), 2) RETURNING id INTO result_id;
    return result_id;
END
$$;