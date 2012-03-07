package com.ds.model;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class ObjectiveTest extends AppEngineTestCase {

    private Objective model = new Objective();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
