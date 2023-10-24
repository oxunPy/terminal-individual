DROP FUNCTION IF EXISTS get_product_by_name(character varying, character varying, character varying, character varying);
CREATE OR REPLACE FUNCTION get_product_by_name(param1 character varying, param2 character varying, param3 character varying, param4 character varying)
    RETURNS TABLE (id integer, rate numeric, currency varchar,  product_name text, barcode varchar, group_name  varchar) AS $$
BEGIN
    RETURN QUERY (
        select DISTINCT ON (p.id) p.id,
                                  prate.rate,
                                  prate.code,
                                  p.name,
                                  p.barcode,
                                  pg.name
        from product p
        left join product_group pg on pg.id = p.group_id
        left join (select  p.id, p.name, pr.date, pr.rate, c.code from product p
                   left join product_rate pr on pr.product_id = p.id
                   left join currency c on c.id = pr.currency_id
                   where pr.status != 3 order by  pr.date desc) prate on prate.id = p.id
        where
              (lower(p.name) like lower('%'||$1||'%')) and
              (lower(p.name) like lower('%'||$2||'%')) and
              (lower(p.name) like lower('%'||$3||'%')) and
              (lower(p.name) like lower('%'||$4||'%')) and
              p.status != 3
        order by p.id , prate.date desc limit 100
    );
END;
$$ LANGUAGE plpgsql;

select * from get_product_by_name('alu', '', '', '')
