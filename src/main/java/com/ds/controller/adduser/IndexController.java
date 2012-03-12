package com.ds.controller.adduser;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class IndexController extends Controller {

    @Override
    public Navigation run() throws Exception {    	
        return forward("../login?op=adduser&username=admin&password=admin_test&email=amardeka11@yahoo.com");
    }
}
