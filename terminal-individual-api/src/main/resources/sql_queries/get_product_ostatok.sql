-- Bunda funksiyalar turadi va programma ishga tushganda bularni ham yozadi. ortiqcha narsalar yozmaslikka harakat qiling!!!!
-- #getProductOstatok
DROP FUNCTION IF EXISTS getproductostatok(bigint, bigint, timestamp without time zone);
CREATE OR REPLACE FUNCTION getproductostatok(product_id bigint, warehous_id bigint, date timestamp without time zone)
    RETURNS numeric AS
$BODY$
BEGIN
    RETURN (SELECT SUM(COALESCE(ii.action_type * ii.qty, 0))
            FROM invoice_item ii
            LEFT JOIN invoice i ON i.id=ii.invoice_id
            WHERE
                 not (i.status = any(ARRAY[3,4]))  and
                 not (ii.status = any(ARRAY[3,4])) and
                 i.date < $3                       and
                 i.warehous_id = $2                and
                 ii.product_id = $1
            GROUP BY ii.product_id);
END;
$BODY$
    LANGUAGE plpgsql VOLATILE
                     COST 100;