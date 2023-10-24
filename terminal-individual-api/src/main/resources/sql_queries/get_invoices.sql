drop function if exists get_invoices(operation_type text, type_source character varying, lim integer, offs integer, from_date timestamp without time zone, to_date timestamp without time zone, active integer, updated integer, to_pay integer,  warehous_id integer, client_id bigint);
create or replace function get_invoices(operation_type text, type_source character varying, lim integer, offs integer, from_date timestamp without time zone, to_date timestamp without time zone, active integer, updated integer, to_pay integer,  warehous_id integer, client_id bigint)
    returns TABLE(id bigint, account_id bigint, account_printable_name character varying, date timestamp without time zone, type integer, info text, status integer, warehouseId integer, warehouse varchar, count_of_items bigint, total numeric)
    language plpgsql
as
$$
DECLARE
    operation_types integer[];
BEGIN
    operation_types = string_to_array($1, ',');
    RETURN QUERY (SELECT i.id,
                         i.client_id,
                         dc.printable_name,
                         i.date,
                         i.type,
                         i.info,
                         i.status,
                         i.warehous_id,
                         w.name,
                         count(i.id),
                         sum(iia.amount)
                  FROM invoice i
                           inner join dealer_client dc on dc.id = i.client_id
                           inner join invoice_item ii on i.id = ii.invoice_id
                           inner join invoice_item_amount iia on ii.id = iia.invoice_item and iia.currency_id = 2
                           inner join warehous w on w.id = i.warehous_id
                  WHERE (operation_types is null or i.type = ANY (operation_types))
                    AND ($2 is null OR i.typesource = $2)
                    AND ($10 is null OR i.warehous_id = $10)
                    AND ($11 is null OR i.client_id = $11)
                    AND (i.date >= $5 AND i.date <= $6)
                    AND (i.status = $7 OR i.status = $8 OR i.status = $9)
                    AND ii.status = 2
                  GROUP BY i.id, dc.printable_name, w.name
                  ORDER BY i.id DESC
                  LIMIT $3 OFFSET $4);
END;
$$;

select * from get_invoices(null, null, 100, 0, '1970-01-01', '2023-05-10', 2, 5, 6,1, null);

select *
from invoice_item_amount
where invoice_item = any(ARRAY[27,28,29,30,31,32])

    get_invoices(character varying, unknown, integer, integer, unknown, unknown, integer, integer, integer, bytea, bytea)