package com.ds.controller.editor;

import java.util.List;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.ds.model.Presentation;
import com.ds.model.Session;
import com.ds.service.Utils;

public class UpdateController extends Controller {
    
    
    @Override
    public Navigation run() throws Exception {
        Session session = Utils.getSession(request.getCookies()); 
        if(session == null){
            return redirect("../login");
        }
        //we have to read all the available question types 
        //and send them to the jsp page        
        List<Presentation> typesList = Utils.getQuizTypes();
        requestScope("quiztypes", typesList);        
        return forward("update.jsp");
    }
}
