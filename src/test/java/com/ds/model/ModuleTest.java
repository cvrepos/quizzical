package com.ds.model;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class ModuleTest extends AppEngineTestCase {

    private ModCopy model = new ModCopy();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
