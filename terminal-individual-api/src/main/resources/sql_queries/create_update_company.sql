DROP FUNCTION IF EXISTS create_update_company(varchar, bool, varchar, text, varchar, varchar, varchar, varchar);
CREATE OR REPLACE FUNCTION create_update_company(name varchar(50), is_main bool, phone_number varchar(50), motto text, director varchar(50),
                                          manager varchar(50), telegram_contact varchar(50), email varchar(50), company_id bigint)
RETURNS BIGINT LANGUAGE plpgsql AS
$$
DECLARE
    result_id bigint;
BEGIN
     IF(company_id IS NOT NULL) THEN
         UPDATE company
         SET company_name = $1,
             phone_number = $3,
             motto = $4,
             director = $5,
             updated = NOW(),
             manager = $6,
             telegram_contact = $7,
             email = $8
         WHERE id = $9 RETURNING id INTO result_id;
     ELSE
         INSERT INTO company(created, status, company_name, is_main, phone_number, motto, director, manager, telegram_contact, email)
         VALUES(NOW(), 2, $1, $2, $3, $4, $5, $6, $7, $8) RETURNING ID INTO result_id;
     end if;

     return result_id;
END;
$$;

-- select create_update_company('sdfsfd', true, '3rwerwef', 'sdfsdfsd', 'sw3rw', '23wdfsdf', '32sfsdvb', 'sdfxmcxba', null)