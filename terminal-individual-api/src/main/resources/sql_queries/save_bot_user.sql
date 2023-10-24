DROP FUNCTION IF EXISTS save_bot_user(firstName varchar, lastName varchar, command varchar, chatId bigint, botState varchar, contact varchar, currency varchar);
CREATE OR REPLACE FUNCTION save_bot_user(firstName varchar, lastName varchar, command varchar, chatId bigint, botState varchar, contact varchar, currency varchar)
    RETURNS INTEGER LANGUAGE plpgsql AS
$$
DECLARE
    result_id bigint default 0;
BEGIN

    IF (EXISTS(SELECT id FROM bot_users WHERE chat_id = $4)) THEN
        UPDATE bot_users
        SET updated=now(), status=2, synced=false, first_name=$1, last_name=$2, command=$3, bot_state=$5, contact=$6, currency=$7
        WHERE chat_id = $4 RETURNING id INTO result_id;
    ELSE
        INSERT INTO bot_users(created, status, synced, first_name, last_name, command, chat_id, bot_state, contact, currency)
        VALUES(NOW(), 1, false,  $1, $2, $3, $4, $5, $6, $7) RETURNING id INTO result_id;
    END IF;
    return result_id;
END;
$$;