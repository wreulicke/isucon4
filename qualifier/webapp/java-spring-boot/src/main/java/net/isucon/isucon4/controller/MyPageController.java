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

package net.isucon.isucon4.controller;

import com.google.common.base.Strings;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import net.isucon.isucon4.JavaScriptEngine;
import net.isucon.isucon4.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.script.ScriptException;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    JavaScriptEngine scriptEngine = new JavaScriptEngine();

    @Autowired
    Session session;

    @RequestMapping(method = RequestMethod.GET)
    String index(ModelMap model, RedirectAttributes attributes) {

        if (session == null || session.getLoginLog() == null ||
                Strings.isNullOrEmpty(session.getLoginLog().getLogin())) {
            attributes.addFlashAttribute("msg", "You must be logged in");
            return "redirect:/";
        }

        try {
            NashornScriptEngine nashorn = scriptEngine.getEngineHolder().get();
            Object markupLastLogin = nashorn.invokeFunction("renderMyPageLastLogin",
                    session.getLoginLog().getCreatedAt(),
                    session.getLoginLog().getIp());
            Object markupLogin = nashorn.invokeFunction("renderMyPageLogin", session.getLoginLog().getLogin());
            model.addAttribute("markupLastLogin", markupLastLogin);
            model.addAttribute("markupLogin", markupLogin);

            return "mypage";
        } catch (ScriptException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}