package com.ds.controller.viewer;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.ds.model.Session;
import com.ds.service.QuizProcessorService;
import com.ds.util.Utils;

public class IndexController extends Controller {

    //private QuizProcessorService service = QuizProcessorService.getInstance();
    @Override
    public Navigation run() throws Exception {
        System.err.println("invoked /viwer/index");
        Utils.sessionCheck(request);        
        return forward("index.jsp");
    }
}
