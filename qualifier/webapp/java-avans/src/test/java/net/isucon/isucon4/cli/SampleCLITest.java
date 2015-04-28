package net.isucon.isucon4.cli;

import net.isucon.isucon4.TestBase;
import org.junit.Test;

import java.sql.SQLException;

public class SampleCLITest extends TestBase {
    @Test
    public void test() throws SQLException {
        SampleCLI.main(new String[]{});
    }

}
