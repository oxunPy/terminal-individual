DROP FUNCTION IF EXISTS create_update_product_group(varchar, bool, varchar, text, varchar, varchar, varchar, varchar);
CREATE OR REPLACE FUNCTION create_update_product_group(product_group_id bigint, name varchar, info varchar, parent_id bigint)
    RETURNS BIGINT LANGUAGE plpgsql AS
$$
DECLARE
    result_id bigint;
BEGIN
    IF(product_group_id IS NOT NULL) THEN
        UPDATE product_group
        SET name = $2,
            info = $3,
            updated = NOW(),
            parent_id = $4
        WHERE id = $1 RETURNING id INTO result_id;
    ELSE
        INSERT INTO product_group(created, status, name, info, parent_id, server_id)
        VALUES(NOW(), 5, $2, $3, $4, 0) RETURNING ID INTO result_id;
    end if;

    return result_id;
END;
$$;

-- select create_update_product_group(null, 'ALU 3417 5', 'ALU INFO', 2)
