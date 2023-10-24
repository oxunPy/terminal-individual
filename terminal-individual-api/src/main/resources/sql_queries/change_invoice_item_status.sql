create or replace function change_invoice_item_status(invoice_id bigint, stat integer) returns text
    language plpgsql
as
$$
BEGIN
UPDATE invoice_item
SET status = $2
WHERE invoice_item.invoice_id = $1;
RETURN ' ';
END;
$$;