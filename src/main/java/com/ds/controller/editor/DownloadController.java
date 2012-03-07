package com.ds.controller.editor;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class DownloadController extends Controller {

    @Override
    public Navigation run() throws Exception {
        return forward("download.jsp");
    }
}
