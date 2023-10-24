create or replace function get_invoices_count_and_sum(operation_type text, type_source character varying, from_date timestamp without time zone, to_date timestamp without time zone, active integer, updated integer, to_pay integer)
    returns TABLE(count bigint, sum numeric)
    language plpgsql
as
$$
DECLARE
    operation_types integer[];
BEGIN
    operation_types = string_to_array($1, ',');
    RETURN QUERY (SELECT count(distinct i.id), sum(iia.amount)
                  FROM invoice i
                           inner join invoice_item ii on i.id = ii.invoice_id
                           inner join invoice_item_amount iia on ii.id = iia.invoice_item and iia.currency_id = 2
                  WHERE i.type = ANY (operation_types)
                    AND i.typesource = $2
                    AND ii.status = 2
                    AND (i.date >= $3 AND i.date <= $4)
                    AND (i.status = $5 OR i.status = $6 OR i.status = $7));
END;
$$;