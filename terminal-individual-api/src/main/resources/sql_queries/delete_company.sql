DROP FUNCTION IF EXISTS delete_company(bigint);
CREATE OR REPLACE FUNCTION delete_company(company_id bigint)
RETURNS BOOL LANGUAGE plpgsql AS
$$
DECLARE result bool default false;
BEGIN
    UPDATE company
    SET status = 3
    WHERE id = company_id;

    result = (select exists(select 1 from company where id = company_id and status = 3));
    return result;
END;
$$;

