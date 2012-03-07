package com.ds.controller.common;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class HeaderController extends Controller {

    @Override
    public Navigation run() throws Exception {
        return forward("header.jsp");
    }
}
