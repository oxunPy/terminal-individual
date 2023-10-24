DROP FUNCTION IF EXISTS get_product_group(character varying, bigint);
CREATE OR REPLACE FUNCTION get_product_group(name character varying, product_group_id bigint)
    RETURNS TABLE (id integer, groupName varchar, parentId integer, info varchar, parentName varchar) AS $$
BEGIN
    RETURN QUERY (
        select pg.id,
               pg.name,
               pg.parent_id,
               pg.info,
               pg_parent.name
        from product_group pg
        left join product_group pg_parent on pg.parent_id = pg_parent.id
        where pg.status = any(ARRAY[2, 5]) and ($1 is null or pg.name like '%%' || $1 || '%%') and ($2 is null or pg.id = $2)
    );
END;
$$ LANGUAGE plpgsql;

-- select * from get_product_group(null, 1)
