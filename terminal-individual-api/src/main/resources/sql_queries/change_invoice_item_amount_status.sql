create or replace function change_invoice_item_amount_status(invoice_id bigint, stat integer) returns text
    language plpgsql
as
$$
BEGIN
    UPDATE invoice_item_amount
    SET status = $2
    WHERE invoice_item_amount.invoice_item = $1;
    RETURN ' ';
END;
$$;