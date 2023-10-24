drop function if exists get_accounts_with_type(type integer, active integer, updated integer)
create or replace function get_accounts_with_type(type integer, active integer, updated integer)
    returns TABLE(id bigint, first_name character varying, last_name character varying, printable_name character varying, phone character varying, opening_balance numeric)
    language plpgsql
as
$$
BEGIN
    RETURN QUERY (
        SELECT
            dc.id,
            dc.first_name,
            dc.last_name,
            dc.printable_name,
            dc.phone,
            dc.opening_balance
        FROM dealer_client dc
        WHERE (dc.status = $2 OR dc.status = $3) AND dc.account_type = $1);
END;
$$;