DROP FUNCTION IF EXISTS get_product_rates(product_id bigint, type integer);
CREATE OR REPLACE FUNCTION get_product_rates(product_id bigint, type integer)
    RETURNS TABLE(id integer, rate NUMERIC, date TIMESTAMP WITHOUT TIME ZONE, currency varchar)
AS
$$BEGIN
    RETURN QUERY(
                SELECT
                    pr.id,
                    pr.rate,
                    pr.date,
                    c.code
                FROM product_rate pr
                LEFT JOIN currency c on pr.currency_id = c.id
                WHERE pr.status = any(ARRAY[2, 5]) and pr.product_id = $1 and pr.type = $2);
END$$ language plpgsql;