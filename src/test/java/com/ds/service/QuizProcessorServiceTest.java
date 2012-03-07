package com.ds.service;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class QuizProcessorServiceTest extends AppEngineTestCase {

    private QuizProcessorService service = QuizProcessorService.getInstance();

    @Test
    public void test() throws Exception {
        assertThat(service, is(notNullValue()));
    }
}
