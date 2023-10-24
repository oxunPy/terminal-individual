create or replace function create_invoice_item(invoice_id bigint, product_id bigint, qty numeric, action_type integer, status integer) returns bigint
    language plpgsql
as
$$
DECLARE
    result_id bigint;
BEGIN
    INSERT INTO invoice_item (invoice_id, product_id, qty, action_type, synced, status, created) VALUES ($1, $2, $3, $4, false, $5, now()) RETURNING id INTO result_id;
    RETURN result_id;
END;
$$;