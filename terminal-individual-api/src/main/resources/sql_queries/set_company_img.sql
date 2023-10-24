DROP FUNCTION IF EXISTS set_company_img(bigint, bigint, varchar);
CREATE OR REPLACE FUNCTION set_company_img(company_id bigint, file_id bigint, img_type varchar)
RETURNS BIGINT LANGUAGE plpgsql AS
$$
DECLARE
    result_id bigint default 0;
BEGIN
    IF(img_type = 'LOGO') THEN UPDATE company SET logo_id = file_id WHERE (id = company_id OR (company_id IS NULL AND company.is_main)) RETURNING 1 INTO result_id;
    END IF;
    IF(img_type = 'FAVICON') THEN UPDATE company SET favicon_id = file_id WHERE (id = company_id OR (company_id IS NULL AND company.is_main)) RETURNING 1 INTO result_id;
    END IF;
    return result_id;
END
$$;

-- select set_company_img(6, 10, 'LOGO')
