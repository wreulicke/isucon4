SELECT ip
     , MAX(id) AS last_login_id
  FROM login_log
 WHERE succeeded = 1
 GROUP by ip
