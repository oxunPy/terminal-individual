drop function if exists get_invoice_item_by_date(timestamp without time zone, timestamp without time zone, integer, integer);
create or replace function get_invoice_item_by_date(from_date timestamp without time zone, to_date timestamp without time zone, items_per_page integer, number_of_page integer)
    returns TABLE(invoice_item_id integer, date_of_sell timestamp without time zone, product_quantity numeric, product_price numeric, type_of_operation integer, product_name varchar, price_currency integer)
    language plpgsql
as
$$
BEGIN
    RETURN QUERY (SELECT ii.id, i.date, ii.qty, pr.rate, i.type, p.name, ii.currency_id
                  FROM invoice_item ii
                  LEFT JOIN product p ON ii.product_id = p.id
                  LEFT JOIN invoice i ON ii.invoice_id = i.id
                  LEFT JOIN (select rate, date,product_id, row_number() over (partition by product_id order by date desc) as rn
                             from product_rate) pr ON pr.rn = 1 AND pr.product_id = ii.product_id
                  WHERE i.date between from_date AND to_date
                  ORDER BY ii.id
                  limit items_per_page offset number_of_page);
END ;
$$;