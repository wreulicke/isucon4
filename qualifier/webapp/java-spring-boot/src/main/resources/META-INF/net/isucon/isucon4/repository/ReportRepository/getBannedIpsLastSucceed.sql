SELECT ip
     , MAX(id) AS id
  FROM login_log
 WHERE succeeded = 1
 GROUP by ip
