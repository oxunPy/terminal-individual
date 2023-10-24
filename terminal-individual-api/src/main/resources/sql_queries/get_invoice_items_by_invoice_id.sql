drop function if exists get_invoice_items_by_invoice_id(bigint, integer, integer, integer);
create or replace function get_invoice_items_by_invoice_id(invoice_id bigint, active integer, updated integer, to_pay integer)
    returns TABLE(id integer, product_id integer, product_name varchar, quantity numeric, selling_rate numeric, total numeric, currencyCode varchar)
    language plpgsql
as
$$

BEGIN
    RETURN QUERY (SELECT
                      distinct on (ii.id)
                      ii.id,
                      ii.product_id,
                      p.name,
                      ii.qty,
                      iia.rate,
                      iia.amount,
                      c.code as currencyCode
                  FROM invoice i
                           INNER JOIN invoice_item ii on i.id = ii.invoice_id
                           INNER JOIN invoice_item_amount iia on ii.id = iia.invoice_item and iia.currency_id = 2
                           INNER JOIN product p on p.id = ii.product_id
                           LEFT JOIN currency c on c.id = ii.currency_id
                  WHERE i.id = $1 AND (i.status = $2 OR i.status = $3 OR i.status = $4));
END;
$$;