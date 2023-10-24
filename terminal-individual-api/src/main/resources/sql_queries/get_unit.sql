DROP FUNCTION IF EXISTS get_unit();
CREATE OR REPLACE FUNCTION get_unit()
    RETURNS TABLE (id integer, name varchar, symbol varchar) AS $$
BEGIN
    RETURN QUERY (
                select u.id,
                       u.name,
                       u.symbol
                from unit u
                where u.status = any(ARRAY[2, 5])
                );
END;
$$ LANGUAGE plpgsql;

-- select get_unit()
