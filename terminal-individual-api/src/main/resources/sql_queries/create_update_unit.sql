DROP FUNCTION IF EXISTS create_update_unit(varchar, varchar, bigint);
CREATE OR REPLACE FUNCTION create_update_unit(name varchar, symbol varchar, unit_id bigint)
    RETURNS BIGINT LANGUAGE plpgsql AS
$$
DECLARE
    result_id bigint;
BEGIN
    IF(unit_id IS NOT NULL) THEN
        UPDATE unit
        SET name = $1,
            symbol = $2,
            updated = NOW()
        WHERE id = $3 RETURNING id INTO result_id;
    ELSE
        INSERT INTO unit(created, status, name, symbol, server_id)
        VALUES(NOW(), 2, $1, $2, 0) RETURNING ID INTO result_id;
    end if;

    return result_id;
END;
$$;


-- select create_update_unit('dona3', 'SHT3', null)