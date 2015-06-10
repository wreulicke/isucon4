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

package net.isucon.isucon4.controller;

import com.google.common.base.Strings;
import freemarker.template.TemplateException;
import me.geso.avans.annotation.GET;
import me.geso.avans.annotation.POST;
import me.geso.avans.annotation.Param;
import me.geso.webscrew.response.WebResponse;
import net.isucon.isucon4.exception.BusinessException;
import net.isucon.isucon4.model.Session;
import net.isucon.isucon4.row.LoginLog;
import net.isucon.isucon4.service.LoginService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class LoginController extends BaseController {

    @Inject
    Session session;

    @Inject
    LoginService loginService;

    @GET("/")
    public WebResponse index() throws IOException, TemplateException {

        // セッションから取り出す
        String login = session.getLogin();
        String msg = session.getMsg();

        // セッションから取り出したらクリアする
        session.setLogin(null);
        session.setMsg(null);

        return freemarker("index.html.ftl")
                .param("login", login)
                .param("msg", msg)
                .render();
    }

    @POST("/login")
    public WebResponse login(@Param("login") String login, @Param("password") String password) {

        String ip = getRemoteIp();

        try {
            LoginLog loginLog = loginService.attemptLogin(login, password, ip);
            session.setLoginLog(loginLog);
            session.setLogin(null);
            session.setMsg(null);

            return redirect("/mypage");
        } catch (BusinessException e) {
            session.setLoginLog(null);
            session.setLogin(login);
            session.setMsg(e.getMessage());

            return redirect("/");
        }
    }

    String getRemoteIp() {
        HttpServletRequest request = getServletRequest();
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (!Strings.isNullOrEmpty(xForwardedFor)) {
            return xForwardedFor;
        } else {
            return request.getRemoteAddr();
        }
    }
}
