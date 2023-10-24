create or replace function get_invoice_by_uid(invoice_uid text)
    returns TABLE(id integer, account_id integer, account_printable_name character varying, date_ timestamp without time zone, type_ integer, info_ text, status integer)
    language plpgsql
as
$$
BEGIN
RETURN QUERY (SELECT i.id,
        i.client_id,
        dc.printable_name,
        i.date,
        i.type,
        i.info,
        i.status
            FROM invoice i
                           inner join dealer_client dc on dc.id = i.client_id
                  where i.uid = $1);
return;
END;
$$;


select * from get_invoice_by_uid('')