DROP FUNCTION IF EXISTS delete_unit(unit_id bigint);
CREATE OR REPLACE FUNCTION delete_unit(unit_id bigint)
    RETURNS bool LANGUAGE plpgsql AS
$$
DECLARE
    result_id bigint default 0;
BEGIN
    -- Get count from UPDATE
    UPDATE unit
    SET status = 3
    WHERE id = unit_id
    RETURNING id into result_id;
    return result_id > 0;
END;
$$;

select delete_unit(1)