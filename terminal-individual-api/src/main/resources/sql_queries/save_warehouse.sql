DROP FUNCTION IF EXISTS save_warehouse(varchar, bool);
CREATE OR REPLACE FUNCTION save_warehouse(name varchar, is_default bool)
RETURNS BOOL LANGUAGE plpgsql AS
$$
DECLARE
    result_id bigint default 0;
BEGIN
    INSERT INTO warehous(created, status, synced, name)
    VALUES(NOW(), 2, false, $1) RETURNING id INTO result_id;

    IF(result_id > 0 AND $2) THEN UPDATE settings SET default_warehous_id = result_id WHERE 1 = 1;
    END IF;
    return result_id > 0;
END;
$$;

SELECT save_warehouse('SKLAD-3', TRUE);