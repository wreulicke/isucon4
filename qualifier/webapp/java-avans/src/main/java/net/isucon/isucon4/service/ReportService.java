/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Manabu Matsuzaki
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

import javafx.util.Pair;
import net.isucon.isucon4.constant.IsuconConstant;
import net.isucon.isucon4.repository.ReportRepository;
import net.isucon.isucon4.row.LoginLog;
import net.isucon.isucon4.row.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class ReportService {

    @Inject
    ReportRepository reportRepository;

    public Map<String, List<String>> getReport() {

        // IP
        List<LoginLog> ips = reportRepository.getBannedIpsNotSucceed(IsuconConstant.THRESHOLD_IP_BAN);
        List<LoginLog> bannedIpsLastSucceeds = reportRepository.getBannedIpsLastSucceed();
        List<String> filteredIps = bannedIpsLastSucceeds.stream()
                .map(reportRepository::getBannedIpsLastSucceedCounts)
                .filter(pair -> pair.getValue() >= IsuconConstant.THRESHOLD_IP_BAN)
                .map(Pair::getKey)
                .collect(Collectors.toList());
        List<String> ipList = ips.stream()
                .map(LoginLog::getIp)
                .collect(Collectors.toList());
        ipList.addAll(filteredIps);

        // User
        List<User> users = reportRepository.getLockedUsersNotSucceed(IsuconConstant.THRESHOLD_USER_LOCK);
        List<LoginLog> lockedUsersLastSucceeds = reportRepository.getLockedUsersLastSucceed();
        List<String> filteredUsers = lockedUsersLastSucceeds.stream()
                .map(reportRepository::getLockedUsersLastSucceedCounts)
                .filter(pair -> pair.getValue() >= IsuconConstant.THRESHOLD_USER_LOCK)
                .map(Pair::getKey)
                .collect(Collectors.toList());
        List<String> userList = users.stream()
                .map(User::getLogin)
                .collect(Collectors.toList());
        userList.addAll(filteredUsers);

        Map<String, List<String>> map = new LinkedHashMap<>();
        map.put("banned_ips", ipList);
        map.put("locked_users", userList);

        return map;
    }
}
