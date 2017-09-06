/* t1 */
SELECT *
FROM client
WHERE name = :name;

/* jet */
SELECT *
FROM client
WHERE 1 = 1
#if(x)
AND x = :x
#end
#if(y)
AND y = :y
#end;