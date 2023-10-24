drop function if exists get_today_income_and_expense(timestamp without time zone, character varying, integer, integer, integer, integer);
create or replace function get_today_income_and_expense(req_date timestamp without time zone, type_source character varying, req_income_type integer, req_expense_type integer)
    returns TABLE(income numeric, expense numeric)
    language plpgsql
as
$$
BEGIN
    RETURN QUERY (SELECT
                         sum(iia.amount) filter ( where iia.invoice_item = ii.id and ii.action_type = req_income_type and i.date >= req_date and i.date < req_date::date + '1 day'::interval ) as incomes,
                         sum(iia.amount) filter ( where iia.invoice_item = ii.id and ii.action_type = req_expense_type and i.date >= req_date and i.date < req_date::date + '1 day'::interval) as expenses
                  FROM invoice i
                  INNER JOIN invoice_item ii on i.id = ii.invoice_id
                  INNER JOIN invoice_item_amount iia on ii.id = iia.invoice_item and iia.currency_id = 2
                  WHERE i.date >= req_date::date AND i.date < req_date::date + '1 day'::interval AND i.typesource = type_source AND (i.status = 2 OR i.status = 5 OR i.status= 6) AND i.warehous_id = warehouse_id_);
END ;
$$;