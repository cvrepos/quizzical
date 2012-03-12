package com.ds.controller.viewer;

import java.util.LinkedList;
import java.util.List;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import com.ds.meta.ModuleMeta;
import com.ds.model.Module;
import com.ds.model.Session;
import com.ds.model.Tag;
import com.ds.model.json.KeyValueMap;
import com.ds.service.QuizProcessorService;
import com.ds.util.Utils;
import com.google.appengine.api.datastore.Transaction;

public class IndexController extends Controller {

    //private QuizProcessorService service = QuizProcessorService.getInstance();
    @Override
    public Navigation run() throws Exception {
        System.err.println("invoked /viwer/index");
        Utils.sessionCheck(request);
        //get all the modules and add them to the request
        ModuleMeta m = ModuleMeta.get();
        List<Module> modules = Datastore.query(m).asList();
        List<String> modJson  = new LinkedList<String>();
        for(Module module:modules){
        	KeyValueMap map = new KeyValueMap();
        	map.put("mname", module.getName());
        	map.put("mid", module.getKey().getId());
        	map.put("questionCount", module.getQuestionCount());
        	map.put("cloneCount", module.getCloneCount());
        	modJson.add(map.toJson());
        }
        request.setAttribute("modules", modJson);
        return forward("index.jsp");
    }
}
