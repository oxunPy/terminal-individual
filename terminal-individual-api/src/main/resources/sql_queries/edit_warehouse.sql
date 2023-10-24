DROP FUNCTION IF EXISTS edit_warehouse(varchar, bool, bigint);
CREATE OR REPLACE FUNCTION edit_warehouse(name varchar, is_default bool, warehouse_id bigint)
RETURNS bool LANGUAGE plpgsql AS
$$
DECLARE
    result_id bigint default 0;
BEGIN
    UPDATE warehous
    SET name = $1
    WHERE id = $3 RETURNING id INTO result_id;

    IF(is_default) THEN UPDATE settings SET default_warehous_id = result_id WHERE 1 = 1;
    END IF;
    return result_id > 0;
END;
$$;

-- select edit_warehouse('SKLAD-1', TRUE, 1)