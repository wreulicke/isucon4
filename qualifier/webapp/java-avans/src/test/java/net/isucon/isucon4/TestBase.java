package net.isucon.isucon4;

import net.isucon.isucon4.config.Config;
import net.isucon.isucon4.provider.ConfigProvider;
import org.junit.BeforeClass;

public class TestBase {
    protected static Config config;

    @BeforeClass
    public static void setupClass() {
        String env = System.getProperty("java-avans.env");
        if (env == null) {
            System.setProperty("java-avans.env", "test");
        }
        env = System.getProperty("java-avans.env");
        if (!(env.equals("test"))) {
            throw new RuntimeException("Do not run test case on non-test environment");
        }

        config = new ConfigProvider().get();
    }
}

