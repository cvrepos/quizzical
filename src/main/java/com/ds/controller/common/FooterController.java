package com.ds.controller.common;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class FooterController extends Controller {

    @Override
    public Navigation run() throws Exception {
        return forward("footer.jsp");
    }
}
