CREATE OR REPLACE FUNCTION get_user(login_str character varying, pass_str character varying)
    RETURNS TABLE (
                      user_name character varying,
                      login character varying,
                      pass character varying) AS $$
BEGIN
    RETURN QUERY (
        select
            u.name,
            u.login,
            u.pass
        from dealer_user u
        where u.login=$1 and u.pass=$2 and u.status != 3);
END;
$$ LANGUAGE plpgsql;