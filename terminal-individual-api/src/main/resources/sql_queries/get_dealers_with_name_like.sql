DROP FUNCTION IF EXISTS get_dealers_with_name_like(param character varying);
CREATE OR REPLACE FUNCTION get_dealers_with_name_like(param character varying)
    RETURNS TABLE (id integer, name character varying, dealer_code character varying) AS $$
BEGIN
    RETURN QUERY (
        select d.id,d.name,d.dealer_code
        from dealer d
        where server_id <> 1 and not (d.status = any(ARRAY[3, 4])) and ($1 is null or lower(d.name) like '%%' || lower(coalesce($1, '')) || '%%'));
END;
$$ LANGUAGE plpgsql;


select * from get_dealers_with_name_like('')
