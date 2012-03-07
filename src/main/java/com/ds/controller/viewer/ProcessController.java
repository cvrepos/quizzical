package com.ds.controller.viewer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Transaction;
import com.ds.meta.ModuleMeta;
import com.ds.model.Module;
import com.ds.model.Objective;
import com.ds.model.Question;
import com.ds.model.QuestionState;
import com.ds.model.Session;
import com.ds.model.json.KeyValueMap;
import com.ds.model.json.MatchSearch;
import com.ds.service.QuizProcessorService;
import com.ds.service.ServiceResponse;
import com.ds.util.StaticValues;
import com.ds.util.Utils;

public class ProcessController extends Controller {

    private QuizProcessorService service = QuizProcessorService.getInstance();

    @Override
    public Navigation run() throws Exception {
        System.err.println("invoked /viwer/process");
        
        if(!Utils.sessionCheck(request)){
            String url = "../login?op=login";
            request.setAttribute("orig", basePath + "process");
            return forward(response.encodeRedirectURL(url));
        }
        
        String magic = (String) request.getAttribute("m");
        if(!Utils.isValid(magic) || !magic.equals("y")){
            return forward("process.jsp");
        }
        
        
        String action =
            (String) request.getAttribute(StaticValues.QUIZ_NAMESPACE + "action");
        if (!Utils.isValid(action)) {
            System.err.println("Action is invalid");
            return null;
        }
        if (action.equals("start")) {
            Utils.respond(onStart(), response);
        }  else if (action.equals("get")) {
            Utils.respond(onGet(), response);            
        }  else if (action.equals("submit")){
            Utils.respond(onSubmit(), response);
        }  else if (action.equals("reset")){
            Utils.respond(onReset(), response);
        } else if(action.equals("result")){
            Utils.respond(onResult(), response);
        } else if(action.equals("moduleanalysis")){
            Utils.respond(onModuleAnalyze(), response);
        }
        return null;
    }

    private ServiceResponse onGet() {
        System.err.println("In onGet");

        Session session = Utils.getSession(request.getCookies());
        String mname = (String) request.getAttribute(StaticValues.QUIZ_NAMESPACE + "mname");
        if (!Utils.isValid(mname)) {
            System.err.println("Invalid module id");
            return Utils.sendError("Invalid module id");
        }
        ModuleMeta m = ModuleMeta.get();
        Module module =
            Datastore
                .query(m)
                .filter(m.name.equal(mname), m.user.equal(session.getUser()))
                .asSingle();
        if (module == null) {
            return Utils.sendError("Unable to find module");
        }

        System.err.println("Found a module");
        Utils.dumpModule(module);
        String qid = (String) request.getAttribute(StaticValues.QUIZ_NAMESPACE +"qid");
        if (!Utils.isValid(qid)) {
            System.err.println("Qid is invalid");
            return Utils.sendError("Invalid qid");
        }
     // now we have to get the next question out of the module and
        // present to the user
        List<QuestionState> qids = module.getQStates();
        int index = Integer.parseInt(qid);
        if (index  >= qids.size()) {
            return Utils.sendSuccess("Completed all the questions");
        }
        
        QuestionState state = module.getQStates().get(index);
       
        Objective q = (Objective) service.getQuizByKey(state.getQid());
        if (q == null) {
            return Utils.sendError("Invalid index");
        }        
       
        KeyValueMap map = new KeyValueMap();
        map.put("question", q.getQuestion());
        map.put("qid",   Integer.toString(index));
        map.put("mname", module.getName());
        map.put("ans1", q.getAnswers().get(0).getAns());
        map.put("ans2", q.getAnswers().get(1).getAns());
        map.put("ans3", q.getAnswers().get(2).getAns());
        map.put("ans4", q.getAnswers().get(3).getAns());
        ServiceResponse response = new ServiceResponse();
        response.setStatus(true);
        response.setMetaData(map.toJson());
        return response;

    }
    
    private ServiceResponse onSubmit() {
        System.err.println("In onSubmit");
        Session session = Utils.getSession(request.getCookies());
        String mname = (String) request.getAttribute(StaticValues.QUIZ_NAMESPACE + "mname");
        if (!Utils.isValid(mname)) {
            System.err.println("Invalid module id");
            return Utils.sendError("Invalid module id");
        }
        ModuleMeta m = ModuleMeta.get();
        Module module =
            Datastore
                .query(m)
                .filter(m.name.equal(mname), m.user.equal(session.getUser()))
                .asSingle();
        if (module == null) {
            return Utils.sendError("Unable to find module");
        }

        System.err.println("Found a module");
        Utils.dumpModule(module);
        String qid = (String) request.getAttribute(StaticValues.QUIZ_NAMESPACE +"qid");
        if (!Utils.isValid(qid)) {
            System.err.println("Qid is invalid");
            return Utils.sendError("Invalid qid");
        }

        int index = Integer.parseInt(qid);
        QuestionState state = module.getQStates().get(index);
        long id = state.getQid();
        // the user has clicked next - check for answers
        String ans = (String) request.getAttribute(StaticValues.QUIZ_NAMESPACE +"ans");
        if (Utils.isValid(ans)) {
            // valid answers
            String[] vals = ans.split(",");
            if (vals.length > 4) {
                System.err.println("Invalid answer set");
                return Utils.sendError("Invalid answer set");
            }
            // make a query to check if the answer is correct
            MatchSearch search = new MatchSearch();
            search.setAnswers(vals);
            search.setId(id);
            boolean result = service.matchAnswers(search, session, null);
            if (result) {
                System.err.println("Its a correct answer.");
                state.setState(QuestionState.QStatEnum.CORRECT);
                
            } else {
                System.err.println("Its a wrong answer.");
                state.setState(QuestionState.QStatEnum.INCORRECT);
            }
        }else{
            //make the state ATTEMPTED
            state.setState(QuestionState.QStatEnum.ATTEMPTED);
            
        }
        module.getQStates().set(index, state);
        // save the state
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(module);
        tx.commit();

        // now we have to get the next question out of the module and
        // present to the user
        List<QuestionState> qids = module.getQStates();
        ServiceResponse response = new ServiceResponse();
        KeyValueMap map = new KeyValueMap();
        if (index + 1 >= qids.size()) {
            map.put("code", "COMPLETED");
            //map.put("result", "QN_action=result&QN_mname=" + module.getName());
            map.put("mname", module.getName());
        }else{
            map.put("params" , "QN_action=get&QN_qid=" + Integer.toString(index + 1) + "&QN_mname=" 
                + module.getName());
            map.put("code", "NEXT"); 
        }
        response.setStatus(true);
        response.setMetaData(map.toJson());
        return response;

    }

    

    private ServiceResponse onStart() {
        
        System.err.println("Invoked onStart");

        Session session = Utils.getSession(request.getCookies());
        if (session == null) {
            return Utils.sendError("Invalid session.");
        }
        String mname = (String) request.getAttribute(StaticValues.QUIZ_NAMESPACE + "mname");
        if (!Utils.isValid(mname)) {
            System.err.println("Invalid module id");
            return Utils.sendError("Invalid module id");
        }
        ModuleMeta m = ModuleMeta.get();
        Module module =
            Datastore
                .query(m)
                .filter(m.name.equal(mname), m.user.equal(session.getUser()))
                .asSingle();
        if (module != null) {
            List<QuestionState> ansStates = module.getQStates();
            // check if force start enabled - in such case reset the question
            // state
            String force =
                (String) request.getAttribute(StaticValues.QUIZ_NAMESPACE + "force");
            if (Utils.isValid(force)) {
                if (force.equals(StaticValues.YES)) {
                    Utils.resetQState(
                        ansStates,
                        QuestionState.QStatEnum.INITIALIZED);
                }
            } else {
                if (Utils.alreadyStarted(ansStates)) {
                    // the user has already started the quiz - ask him if he
                    // wants to restart -
                    // view the result
                    ServiceResponse response = new ServiceResponse();
                    response.setStatus(true);
                    KeyValueMap map = new KeyValueMap();
                    map.put("code", "ALREADY_STARTED");
                    map.put(
                        "reset",
                        "QN_action=reset&QN_mname=" + module.getName());
                    map.put(
                        "resume",
                        "QN_action=get&QN_mname="
                            + module.getName()
                            + "&QN_qid="
                            + Utils.getFirstUnattempted(ansStates));
                    response.setMetaData(map.toJson());
                    return response;
                }
            }
            
        } else {
            module = new Module();
            Iterator<Question> it = service.getQuizByTag(mname);
            module.setName(mname);
            module.setUser(session.getUser());
            List<QuestionState> qStates = module.getQStates();
            while (it.hasNext()) {
                Question q = it.next();
                System.err.printf("Loaded qid=%d\n", q.getKey().getId());
                qStates.add(new QuestionState(q.getKey().getId(), 
                    QuestionState.QStatEnum.INITIALIZED));
            }
            

        }
        
        //save this module/module update to DataStore
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(module);
        tx.commit();
        Utils.dumpModule(module);
        
        KeyValueMap map = new KeyValueMap();
        map.put("message", "Successfully started the module");
        map.put("params" , "QN_action=get&QN_qid=0&QN_mname=" 
            + module.getName());
        map.put("code", "NEXT");            
        ServiceResponse response = new ServiceResponse();
        response.setStatus(true);
        response.setMetaData(map.toJson());
        return response;

    }
    
    private ServiceResponse onReset() {
        
        System.err.println("In onReset()");
        Session session = Utils.getSession(request.getCookies());
        if (session == null) {
            return Utils.sendError("Invalid session.");
        }
        String mname = (String) request.getAttribute(StaticValues.QUIZ_NAMESPACE + "mname");
        if (!Utils.isValid(mname)) {
            System.err.println("Invalid module id");
            return Utils.sendError("Invalid module id");
        }
        ModuleMeta m = ModuleMeta.get();
        Module module =
            Datastore
                .query(m)
                .filter(m.name.equal(mname), m.user.equal(session.getUser()))
                .asSingle();
        ServiceResponse response = new ServiceResponse();
        KeyValueMap map = new KeyValueMap();
        if (module != null) {
            List<QuestionState> qStates = module.getQStates();
            for(QuestionState s: qStates){
                s.setState(QuestionState.QStatEnum.INITIALIZED);
            }
          //save this module/module update to DataStore
            Transaction tx = Datastore.beginTransaction();
            Datastore.put(module);
            tx.commit();
            
            map.put("message", "Successfully started the module");
            map.put("params" , "QN_action=get&QN_qid=0&QN_mname=" 
                + module.getName());
            map.put("code", "NEXT");            
            response.setStatus(true);
        }else{           
            response.setStatus(true);
            map.put("message", "Unable to find module.");                     
        }
        response.setMetaData(map.toJson());
        return response;

    }
    
    private ServiceResponse onResult() {
        
        System.err.println("In onResult()");
        Session session = Utils.getSession(request.getCookies());
        if (session == null) {
            return Utils.sendError("Invalid session.");
        }
        String mname = (String) request.getAttribute(StaticValues.QUIZ_NAMESPACE + "mname");
        if (!Utils.isValid(mname)) {
            System.err.println("Invalid module id");
            return Utils.sendError("Invalid module id");
        }
        ModuleMeta m = ModuleMeta.get();
        Module module =
            Datastore
                .query(m)
                .filter(m.name.equal(mname), m.user.equal(session.getUser()))
                .asSingle();
        ServiceResponse response = new ServiceResponse();
        KeyValueMap map = new KeyValueMap();
        if (module != null) {
            response.setStatus(true);
            List<QuestionState> qStates = module.getQStates();
            int corrects = 0;
            int wrongs = 0;
            int unanswered = 0;
            for(QuestionState s: qStates){
                if(s.getState() == QuestionState.QStatEnum.ATTEMPTED){
                    unanswered++;
                }else if(s.getState() == QuestionState.QStatEnum.CORRECT){
                    corrects++;
                }else if(s.getState() == QuestionState.QStatEnum.INCORRECT){
                    wrongs++;
                }
            }
            map.put("ncorrect", Integer.toString(corrects));
            map.put("nwrong", Integer.toString(wrongs));
            map.put("nunanswered", Integer.toString(unanswered));
            map.put("mname", module.getName());
            map.put("code", "RESULTS");
        }else{           
            response.setStatus(false);
            map.put("message", "Unable to find module.");                     
        }
        response.setMetaData(map.toJson());
        return response;

    }
    private ServiceResponse onModuleAnalyze() {
        
        System.err.println("In onModuleAnalyze()");
        Session session = Utils.getSession(request.getCookies());
        if (session == null) {
            return Utils.sendError("Invalid session.");
        }
        String mname = (String) request.getAttribute(StaticValues.QUIZ_NAMESPACE + "mname");
        if (!Utils.isValid(mname)) {
            System.err.println("Invalid module id");
            return Utils.sendError("Invalid module id");
        }
        ModuleMeta m = ModuleMeta.get();
        Module module =
            Datastore
                .query(m)
                .filter(m.name.equal(mname), m.user.equal(session.getUser()))
                .asSingle();
        ServiceResponse response = new ServiceResponse();
        KeyValueMap map = new KeyValueMap();
        if (module != null) {
            response.setStatus(true);
            List<QuestionState> qStates = module.getQStates();
            Map<String, Integer> tagCounts = new HashMap<String, Integer>();
            for(QuestionState s: qStates){
                Question q = service.getQuizByKey(s.getQid());
                for(String tag: q.getTags()){
                    if(tagCounts.containsKey("tag_"+ tag)){
                        int counts = tagCounts.get("tag_"+ tag);
                        tagCounts.put("tag_"+ tag, ++counts);
                    }else{
                        tagCounts.put("tag_"+ tag, 1);
                    }
                }
            }
            Iterator<String> keys = tagCounts.keySet().iterator();
            while(keys.hasNext()){
                String tag = keys.next();
                map.put(tag, Integer.toString(tagCounts.get(tag)));
            }                        
            map.put("mname", module.getName());
            map.put("code", "MODULE_ANALYSIS");
        }else{           
            response.setStatus(false);
            map.put("message", "Unable to find module.");                     
        }
        response.setMetaData(map.toJson());
        return response;

    }
    
    

}
