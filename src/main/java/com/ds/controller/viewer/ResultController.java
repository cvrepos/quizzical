package com.ds.controller.viewer;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class ResultController extends Controller {

    @Override
    public Navigation run() throws Exception {
        return forward("result.jsp");
    }
}
