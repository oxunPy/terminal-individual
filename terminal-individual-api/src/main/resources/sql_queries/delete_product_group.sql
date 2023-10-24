DROP FUNCTION IF EXISTS delete_product_group(product_group_id bigint);
CREATE OR REPLACE FUNCTION delete_product_group(product_group_id bigint)
    RETURNS bool LANGUAGE plpgsql AS
$$
DECLARE
    result_id bigint default 0;
BEGIN
    -- Get count from UPDATE
    UPDATE product_group
    SET status = 3
    WHERE id = product_group_id
    RETURNING id into result_id;
    return result_id > 0;
END;
$$;

-- select delete_product_group(1)