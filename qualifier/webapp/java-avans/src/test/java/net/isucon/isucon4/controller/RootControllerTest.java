package net.isucon.isucon4.controller;

import me.geso.mech2.Mech2Result;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RootControllerTest extends ControllerTestBase {
    @Test
    public void testRoot() throws IOException, URISyntaxException {
        final Mech2Result result = mech().get("/").execute();
        assertEquals(200, result.getResponse().getStatusLine().getStatusCode());
        assertTrue(result.getResponseBodyAsString().contains("Hello"));
    }
}
