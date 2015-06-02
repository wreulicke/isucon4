/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Manabu Matsuzaki
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.isucon.isucon4.service;

import com.google.common.primitives.Bytes;
import net.isucon.isucon4.ThresholdConfig;
import net.isucon.isucon4.entity.LoginLog;
import net.isucon.isucon4.entity.User;
import net.isucon.isucon4.exception.BusinessCommitException;
import net.isucon.isucon4.exception.BusinessException;
import net.isucon.isucon4.repository.LoginLogRepository;
import net.isucon.isucon4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class LoginService {

    @Autowired
    ThresholdConfig thresholdConfig;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LoginLogRepository loginLogRepository;

    @Transactional(noRollbackFor = BusinessCommitException.class)
    public LoginLog attemptLogin(String login, String password, String ip) {

        User user = userRepository.findUserByLogin(login)
                .orElseThrow(() -> new BusinessCommitException("Wrong username or password"));

        try {
            checkIpBanned(ip);
            checkUserLocked(user);

            String hash = calculatePasswordHash(password, user.getSalt());
            if (Objects.equals(user.getPasswordHash(), hash)) {
                LoginLog loginLog = new LoginLog();
                loginLog.setCreatedAt(new Date());
                loginLog.setUserId(user.getId());
                loginLog.setLogin(login);
                loginLog.setIp(ip);
                loginLog.setSucceeded((byte) 1);
                loginLogRepository.save(loginLog);

                List<LoginLog> loginLogs = loginLogRepository.findLoginLogByUserId(user.getId());

                return loginLogs.get(loginLogs.size() - 1);
            } else {
                throw new BusinessCommitException("Wrong username or password");
            }
        } catch (BusinessException e) {
            LoginLog loginLog = new LoginLog();
            loginLog.setCreatedAt(new Date());
            loginLog.setUserId(user.getId());
            loginLog.setLogin(login);
            loginLog.setIp(ip);
            loginLog.setSucceeded((byte) 0);
            loginLogRepository.save(loginLog);
            throw e;
        }
    }

    void checkIpBanned(String ip) {
        long failures = loginLogRepository.countBannedIp(ip);
        if (failures >= thresholdConfig.getIpBann()) {
            throw new BusinessCommitException("You're banned.");
        }
    }

    void checkUserLocked(User user) {
        long failures = loginLogRepository.countLockedUser(user.getId());
        if (failures >= thresholdConfig.getUserLock()) {
            throw new BusinessCommitException("This account is locked.");
        }
    }

    String calculatePasswordHash(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.reset();
            String v = String.format("%s:%s", password, salt);
            md.update(v.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();

            StringBuilder sb = Bytes.asList(digest).stream()
                    .map(b -> String.format("%02x", b))
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
