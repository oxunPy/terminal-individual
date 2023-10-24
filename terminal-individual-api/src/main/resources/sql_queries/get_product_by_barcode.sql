DROP FUNCTION IF EXISTS get_product_by_barcode(TEXT);
CREATE OR REPLACE FUNCTION get_product_by_barcode(bar TEXT)
    RETURNS TABLE (id integer, rate numeric,  currency character varying, product_name varchar, barcode varchar, group_name varchar) AS $$
BEGIN
    RETURN QUERY (
                    SELECT p.id, pr.rate, pr.code, p.name, p.barcode, pg.name from product p
                    LEFT JOIN product_barcode pb on pb.product_id = p.id
                    LEFT JOIN product_group pg on pg.id = p.group_id
                    LEFT JOIN (SELECT p.id, pr.rate, c.code FROM product p
                               LEFT JOIN product_rate pr on pr.product_id = p.id
                               LEFT JOIN currency c on c.id = pr.currency_id
                               WHERE pr.status != 3 order by pr.date desc) pr on pr.id = p.id
                    WHERE (p.barcode = $1 or pb.barcode = $1)  and p.status != 3 limit 1);
END;
$$ LANGUAGE plpgsql;

select * from get_product_by_barcode('45667123556')