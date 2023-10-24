DROP FUNCTION IF EXISTS make_company_main(bigint);
CREATE OR REPLACE FUNCTION make_company_main(company_id bigint)
returns bool LANGUAGE plpgsql AS
$$
DECLARE
    result bigint default 1;
BEGIN
    UPDATE company
    SET is_main = (company_id = id)
    WHERE 1 = 1;
    return result;
END;
$$;


