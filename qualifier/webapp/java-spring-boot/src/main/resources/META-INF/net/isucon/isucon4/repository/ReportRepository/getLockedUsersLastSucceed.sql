SELECT user_id
     , login
     , MAX(id) AS id
  FROM login_log
 WHERE user_id IS NOT NULL
   AND succeeded = 1
 GROUP BY user_id
