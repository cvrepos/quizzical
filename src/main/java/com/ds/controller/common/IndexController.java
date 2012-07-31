package com.ds.controller.common;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.ds.util.SampleConsumer;
import com.ds.util.Utils;

public class IndexController extends Controller {

	
	
    @Override
    public Navigation run() throws Exception {    			
        return forward("index.jsp");
    }
}
