package com.ds.controller.common;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.ds.service.Utils;
import com.ds.util.SampleConsumer;


public class IndexController extends Controller {

	
	
    @Override
    public Navigation run() throws Exception {
    	String login = (String) request.getAttribute("action");
    	SampleConsumer consumer = new SampleConsumer();
		if (!Utils.isValid(login) || !login.equals("login")) {			
			consumer.authRequest("https://www.google.com/accounts/o8/id", 
					this.request, this.response);			
		}
		
        return forward("index.jsp");
    }
}
