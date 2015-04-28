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

package net.isucon.isucon4.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HttpSessionUtil {

    private HttpSessionUtil() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T getSessionAttribute(
            HttpServletRequest request, String name) {

        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        } else {
            return (T) session.getAttribute(name);
        }
    }

    public static void setSessionAttribute(
            HttpServletRequest request, String name, Object value) {

        HttpSession session = request.getSession();
        if (session == null) {
            throw new IllegalStateException("HttpSession is null");
        } else {
            session.setAttribute(name, value);
        }
    }

    public static void removeSessionAttribute(
            HttpServletRequest request, String name) {

        HttpSession session = request.getSession();
        if (session == null) {
            throw new IllegalStateException("HttpSession is null");
        } else {
            session.removeAttribute(name);
        }
    }
}
