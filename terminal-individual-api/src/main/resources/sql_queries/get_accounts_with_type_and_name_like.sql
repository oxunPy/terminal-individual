drop function if exists get_accounts_with_type_and_name_like(type integer, namelike character varying, active integer, updated integer);
create or replace function get_accounts_with_type_and_name_like(type integer, namelike character varying, active integer, updated integer)
    returns TABLE(id integer, first_name character varying, last_name character varying, printable_name character varying, phone character varying, opening_balance numeric)
    language plpgsql
as
$$
BEGIN
    RETURN QUERY (SELECT dc.id,
                         dc.first_name,
                         dc.last_name,
                         dc.printable_name,
                         dc.phone,
                         dc.opening_balance
                  FROM dealer_client dc
                  WHERE (dc.status = $3 OR dc.status = $4)
                    AND dc.account_type = $1
                    AND (lower(dc.printable_name) like lower('%'||$2||'%')));
END;
$$;