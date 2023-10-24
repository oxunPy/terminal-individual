drop function if exists create_invoice(bigint, timestamp without time zone, integer, character varying, character varying, integer, character varying, bigint);
create or replace function create_invoice(account_id bigint, date timestamp without time zone, type integer, info character varying, type_source character varying, status integer, uid character varying, warehous_id bigint) returns bigint
    language plpgsql
as
$$
DECLARE
    result_id bigint;
BEGIN
    IF type = 5 THEN
         INSERT INTO invoice(to_dealer_id, date, type, info, typesource, synced, status, uid, warehous_id)
         VALUES($1, $2, $3, $4, $5, false, $6, $7, $8) RETURNING id INTO result_id;
    ELSE
        INSERT INTO invoice (client_id, date, type, info, typesource, synced, status, uid, warehous_id)
         VALUES ($1, $2, $3, $4, $5, false, $6, $7, $8) RETURNING id INTO result_id;
    END IF;
    RETURN result_id;
END;
$$;