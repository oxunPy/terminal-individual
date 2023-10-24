drop function if exists update_invoice(bigint, bigint, integer, text, character varying, integer, integer, timestamp without time zone);
create or replace function update_invoice(client_id_ bigint, warehous_id_ bigint, type_ integer, info_ text, typesource_ character varying, status_ integer, invoice_id integer, date_ timestamp without time zone) returns text
    language plpgsql
as
$$
DECLARE
    result_id bigint;
BEGIN
    UPDATE invoice
    SET client_id  = client_id_,
        warehous_id = warehous_id_,
        type       = type_,
        info       = info_,
        typesource = typesource_,
        synced     = false,
        status     = status_,
        date       = date_
    WHERE id = invoice_id
    RETURNING id INTO result_id;
    RETURN result_id;
END;
$$;