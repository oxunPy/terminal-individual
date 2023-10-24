create or replace function create_invoice_item_amount(invoice_item_id bigint, amount numeric, rate numeric, original_rate numeric, currency_id integer, convertion numeric, denominator numeric, type integer, status integer) returns bigint
    language plpgsql
as
$$
DECLARE
    result_id bigint;
BEGIN

    INSERT INTO invoice_item_amount (invoice_item, amount, rate, orginalrate, currency_id, convertion, denominator,discount, synced, type, status, created)
    VALUES ($1::integer, $2, $3, $4, $5, $6, $7, 0, false, $8, $9, now())
    RETURNING id INTO result_id;

    RETURN result_id;
END;
$$;