drop function if exists get_invoices_go_down(character varying, integer, integer, timestamp without time zone, timestamp without time zone, bigint);
create or replace function get_invoices_go_down(type_source character varying, lim integer, offs integer, from_date timestamp without time zone, to_date timestamp without time zone, warehous_id bigint)
    returns TABLE(id integer, account_id integer, account_printable_name character varying, date timestamp without time zone, type integer, info text, status integer, count_of_items bigint, total numeric)
    language plpgsql
as
$$
DECLARE

BEGIN
    RETURN QUERY (SELECT i.id,
                         i.to_dealer_id,
                         d.name,
                         i.date,
                         i.type,
                         i.info,
                         i.status,
                         count(i.id),
                         sum(iia.amount)
                  FROM invoice i
                           INNER JOIN dealer d on d.id = i.to_dealer_id
                           INNER JOIN invoice_item ii on i.id = ii.invoice_id
                           INNER JOIN invoice_item_amount iia on ii.id = iia.invoice_item and iia.currency_id = 2
                  WHERE  (i.status = any(ARRAY[2, 5, 6])) AND  i.type = 5 AND i.typesource = $1 AND ($6 is null or i.warehous_id = $6) AND (i.date >= $4 AND i.date <= $5) AND ii.status = 2
                  GROUP BY i.id, d.name
                  ORDER BY i.id DESC
                  LIMIT $2 OFFSET $3);
END;
$$;