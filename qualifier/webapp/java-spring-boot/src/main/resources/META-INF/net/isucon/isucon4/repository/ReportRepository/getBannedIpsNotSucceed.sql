SELECT ip
  FROM (SELECT ip
             , MAX(succeeded) as max_succeeded
             , COUNT(1) as cnt
          FROM login_log
         GROUP BY ip
       ) AS t0
 WHERE t0.max_succeeded = 0
   AND t0.cnt >= /*threshold*/0
