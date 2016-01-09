package net.isucon.isucon4;

import jdk.nashorn.api.scripting.NashornScriptEngine;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

public class JavaScriptEngine {

    private static final String[] SCRIPTS = new String[]{
            "static/js/polyfill.js",
            "static/js/lib/ejs.min-v2.3.4.js",
            "static/js/lib/react.min-v0.14.5.js",
            "static/js/lib/moment.min-v2.10.6.js",
            "static/js/render.js",
            "static/js/helper.js",
            "static/js/react/index.js",
            "static/js/react/mypage.js"
    };

    private ThreadLocal<NashornScriptEngine> engineHolder = new ThreadLocal<NashornScriptEngine>() {
        @Override
        protected NashornScriptEngine initialValue() {
            NashornScriptEngine nashorn =
                    (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");
            Arrays.stream(SCRIPTS).forEach(s -> {
                try {
                    nashorn.eval(read(s));
                } catch (ScriptException e) {
                    throw new RuntimeException(e);
                }
            });

            return nashorn;
        }
    };

    public ThreadLocal<NashornScriptEngine> getEngineHolder() {
        return engineHolder;
    }

    private Reader read(String path) {
        InputStream in = getClass().getClassLoader().getResourceAsStream(path);
        return new InputStreamReader(in);
    }
}
