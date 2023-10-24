drop function if exists get_warehouses_by_name(varchar, integer, integer);
create or replace function get_warehouses_by_name(name_like varchar, start integer, count integer)
returns table(id integer, name varchar, isDefault boolean) AS $$
BEGIN
    RETURN QUERY(
        SELECT
            w.id,
            w.name,
            (select w.id = s.default_warehous_id from settings s)
        FROM warehous w
        WHERE w.status = any(ARRAY[2, 5]) and LOWER(w.name) like '%%' || LOWER(TRIM($1)) || '%%' OFFSET $2 LIMIT count
    );
END;
$$ LANGUAGE plpgsql;

select * from get_warehouses_by_name('SK  ', 0, 10)

