package com.ds.controller;

import org.slim3.tester.ControllerTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class LogoutControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/logout");
        LogoutController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/logout.jsp"));
    }
}
