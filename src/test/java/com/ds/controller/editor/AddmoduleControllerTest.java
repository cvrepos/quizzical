package com.ds.controller.editor;

import org.slim3.tester.ControllerTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class AddmoduleControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/editor/addmodule");
        AddmoduleController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/editor/addmodule.jsp"));
    }
}
