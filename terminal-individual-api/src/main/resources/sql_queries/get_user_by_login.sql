drop function if exists get_user_by_login(varchar);
create or replace function get_user_by_login(log varchar)
returns table(
                 id              integer,
                 info            varchar(255),
                 login           varchar(255),
                 user_name       varchar(255),
                 pass            varchar(255),
                 role            varchar(255))
AS $$
BEGIN
    RETURN QUERY(
                 select
                      u.id as id,
                      u.info as info,
                      u.login as login,
                      u.name as user_name,
                      u.pass as pass,
                      dr.name as role
                 from dealer_user u
                 left join dealer_role dr on u.role_id = dr.id and dr.status != 3
                 where u.login = $1 and u.status != 3);
END;
$$ LANGUAGE plpgsql;