SELECT COUNT(1) AS failures
  FROM login_log
 WHERE ip = /*ip*/'a'
   AND id > IFNULL((select id from login_log where ip = /*ip*/'a' AND succeeded = 1 ORDER BY id DESC LIMIT 1), 0)
