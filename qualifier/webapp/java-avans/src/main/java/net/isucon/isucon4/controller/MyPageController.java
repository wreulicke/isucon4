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
import me.geso.webscrew.response.WebResponse;
import net.isucon.isucon4.model.Session;

import javax.inject.Inject;
import java.io.IOException;

public class MyPageController extends BaseController {

    @Inject
    Session session;

    @GET("/mypage")
    public WebResponse index() throws IOException, TemplateException {

        // ログインエラー処理に使用するセッション属性をクリアする
        session.setLogin(null);
        session.setMsg(null);

        if (session == null || session.getLoginLog() == null ||
                Strings.isNullOrEmpty(session.getLoginLog().getLogin())) {
            session.setMsg("You must be logged in");

            return redirect("/");
        }

        return freemarker("mypage.html.ftl")
                .param("createdAt", session.getLoginLog().getCreatedAt())
                .param("ip", session.getLoginLog().getIp())
                .param("login", session.getLoginLog().getLogin())
                .render();
    }
}
