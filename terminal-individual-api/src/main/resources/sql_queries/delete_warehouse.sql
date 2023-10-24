DROP FUNCTION IF EXISTS delete_warehouse(warehouse_id bigint);
CREATE OR REPLACE FUNCTION delete_warehouse(warehouse_id bigint)
RETURNS bool LANGUAGE plpgsql AS
$$
DECLARE
    result_id bigint default 0;
BEGIN
    -- Get count from UPDATE
        UPDATE warehous
        SET status = 3
        WHERE id = warehouse_id
        RETURNING id into result_id;
    return result_id > 0;
END;
$$;