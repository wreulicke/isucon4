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

import com.google.common.base.Objects;
import com.google.common.primitives.Bytes;
import net.isucon.isucon4.ThresholdConfig;
import net.isucon.isucon4.entity.User;
import net.isucon.isucon4.exception.BusinessException;
import net.isucon.isucon4.repository.LoggingRepository;
import net.isucon.isucon4.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@Transactional
public class LoginService {

    @Autowired
    ThresholdConfig thresholdConfig;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    LoggingRepository loggingRepository;

    @Transactional(noRollbackFor = BusinessException.class)
    public Optional<User> attemptLogin(String login, String password, String ip) {

        Optional<User> user = loginRepository.findUserByLogin(login);

        try {
            checkIpBanned(ip);
            checkUserLocked(user);

            user.orElseThrow(() -> new BusinessException("Wrong username or password"));

            user.ifPresent(u -> {
                String hash = calculatePasswordHash(password, u.getSalt());
                if (Objects.equal(u.getPasswordHash(), hash)) {
                    loggingRepository.create(true, login, ip, user);
                } else {
                    throw new BusinessException("Wrong username or password");
                }
            });
        } catch (BusinessException e) {
            loggingRepository.create(false, login, ip, user);
            throw e;
        }

        return user;
    }

    void checkIpBanned(String ip) {
        long failures = loginRepository.countBannedIp(ip);
        if (failures >= thresholdConfig.getIpBann()) {
            throw new BusinessException("You're banned.");
        }
    }

    void checkUserLocked(Optional<User> user) {
        user.ifPresent(u -> {
            long failures = loginRepository.countLockedUser(u.getId());
            if (failures >= thresholdConfig.getUserLock()) {
                throw new BusinessException("This account is locked.");
            }
        });
    }

    String calculatePasswordHash(String password, String salt) {

        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        md.reset();
        String v = String.format("%s:%s", password, salt);
        md.update(v.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();

        StringBuilder sb = Bytes.asList(digest).stream()
                .map(b -> String.format("%02x", b))
                .collect(StringBuilder::new,
                        (sb1, s) -> sb1.append(s),
                        (sb2, sb3) -> sb2.append(sb3));

        return sb.toString();
    }
}
