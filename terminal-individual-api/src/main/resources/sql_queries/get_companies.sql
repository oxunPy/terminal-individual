DROP FUNCTION IF EXISTS get_companies();
CREATE OR REPLACE FUNCTION get_companies()
RETURNS TABLE(id integer, companyName varchar(50), favIconImgName varchar(255),
              logoImgName varchar(255), manager varchar(50), director varchar(50),
              email varchar(50), isMain boolean, motto text, phoneNumber varchar(50), telegramContact varchar(50)) AS $$
DECLARE
BEGIN
    RETURN QUERY(
        SELECT c.id,
               c.company_name,
               (select af.file_name from as_file af where af.id = c.favicon_id) as faviconImgName,
               (select af.file_name from as_file af where af.id = c.logo_id) as logoImgName,
               c.manager,
               c.director,
               c.email,
               c.is_main,
               c.motto,
               c.phone_number,
               c.telegram_contact
        FROM company c
        where not (c.status = any(ARRAY[3,4]))
    );
END;
$$ LANGUAGE plpgsql;

 select * from get_companies()