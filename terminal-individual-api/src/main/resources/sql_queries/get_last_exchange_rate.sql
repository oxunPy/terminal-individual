CREATE OR REPLACE FUNCTION get_last_exchange_rate()
    RETURNS TABLE (
                      id integer,
                      inv_date timestamp,
                      main_currency character varying,
                      main_currency_val numeric,
                      to_currency character varying,
                      to_currency_val numeric
                  ) AS $$
BEGIN
    RETURN QUERY (
        select
            excr.id,
            excr.date,
            mc.code as main_currency,
            excr.mainvalue as main_currency_val,
            tc.code as to_currency,
            excr.tovalue as to_currency_val
        from exchange_rate excr
                 left join currency mc on mc.id = excr.main_currency_id
                 left join currency tc on tc.id = excr.to_currency_id
        order by excr.date desc limit 1 );
END;
$$ LANGUAGE plpgsql;