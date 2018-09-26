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

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Bytes;
import net.isucon.isucon4.ThresholdConfig;
import net.isucon.isucon4.entity.LoginLog;
import net.isucon.isucon4.entity.User;
import net.isucon.isucon4.exception.BusinessCommitException;
import net.isucon.isucon4.exception.BusinessException;
import net.isucon.isucon4.repository.LoggingRepository;
import net.isucon.isucon4.repository.LoginRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.buf.HexUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final ThresholdConfig thresholdConfig;

    private final LoginRepository loginRepository;

    private final LoggingRepository loggingRepository;

    @Transactional(noRollbackFor = BusinessCommitException.class)
    public LoginLog attemptLogin(String login, String password, String ip) {

        User user = loginRepository.findUserByLogin(login)
                .orElseThrow(() -> new BusinessCommitException("Wrong username or password"));

        try {
            checkIpBanned(ip);
            checkUserLocked(user);

            String hash = calculatePasswordHash(password, user.getSalt());
            if (Objects.equals(user.getPasswordHash(), hash)) {
                loggingRepository.create(true, login, ip, user);
                return loginRepository.findLoginLogByUserId(user.getId());
            } else {
                throw new BusinessCommitException("Wrong username or password");
            }
        } catch (BusinessException e) {
            loggingRepository.create(false, login, ip, user);
            throw e;
        }
    }

    void checkIpBanned(String ip) {
        long failures = loginRepository.countBannedIp(ip);
        if (failures >= thresholdConfig.getIpBann()) {
            throw new BusinessCommitException("You're banned.");
        }
    }

    void checkUserLocked(User user) {
        long failures = loginRepository.countLockedUser(user.getId());
        if (failures >= thresholdConfig.getUserLock()) {
            throw new BusinessCommitException("This account is locked.");
        }
    }

    String calculatePasswordHash(String password, String salt) {
        return Hashing.sha256().newHasher().putString(password, StandardCharsets.UTF_8)
            .putString(":", StandardCharsets.UTF_8)
            .putString(salt, StandardCharsets.UTF_8)
            .hash().toString();
    }
}
