package com.ds.controller.editor;

import java.util.LinkedList;
import java.util.List;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import com.ds.meta.ModuleMeta;
import com.ds.model.Module;
import com.ds.model.Presentation;
import com.ds.model.Session;
import com.ds.model.json.KeyValueMap;
import com.ds.service.QuizProcessorService;
import com.ds.util.Utils;

public class IndexController extends Controller {

    private QuizProcessorService service = QuizProcessorService.getInstance();
    
    @Override
    public Navigation run() throws Exception {
        //we have to read all the available question types 
        //and send them to the jsp page  
        Session session = Utils.getSession(request.getCookies()); 
        if(!Utils.sessionCheck(request)){
            String url = "../login?op=login";
            request.setAttribute("orig", basePath );
            return forward(response.encodeRedirectURL(url));
        }
      //get all the modules and add them to the request
        ModuleMeta m = ModuleMeta.get();
        List<Module> modules = Datastore.query(m).filter(m.owner.equal(session.getUser())).asList();
        List<String> modJson  = new LinkedList<String>();
        for(Module module:modules){
        	KeyValueMap map = new KeyValueMap();
        	map.put("mname", module.getName());        	
        	map.put("mid", module.getKey().getId());
        	map.put("total", module.getQuestionCount());
        	if(module.getParent() != null){
        		map.put("parent", module.getParent());
        	}
        	if(module.getPrevSibling() != null){
        		map.put("prevSibling", module.getPrevSibling());
        	}
        	if(module.getFirstChild() != null){
        		map.put("firstChild", module.getFirstChild());
        	}
        	if(module.getNextSibling() != null){
        		map.put("nextSibling", module.getNextSibling());
        	}
        	map.put("questionCount", module.getQuestionCount());
        	map.put("cloneCount", module.getCloneCount());
        
        	map.put("state", module.getState() == null ? "draft": 
        		module.getState().name().toLowerCase());
        	modJson.add(map.toJson());
        }
        request.setAttribute("modules", modJson);     
        return forward("index.jsp");
    }
}
