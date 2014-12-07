SELECT login
  FROM (SELECT user_id
             , login
             , MAX(succeeded) as max_succeeded
             , COUNT(1) as cnt
          FROM login_log
         GROUP BY user_id
       ) AS t0
 WHERE t0.user_id IS NOT NULL
   AND t0.max_succeeded = 0
   AND t0.cnt >= /*threshold*/0
