SELECT COUNT(1) AS failures
  FROM login_log
 WHERE user_id = /*userId*/'a'
   AND id > IFNULL((select id from login_log where user_id = /*userId*/'a' AND succeeded = 1 ORDER BY id DESC LIMIT 1), 0)
