package com.ds.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import com.ds.model.Module;
import com.ds.model.QuestionState;
import com.ds.model.QuestionState.QStatEnum;
import com.ds.model.Session;
import com.ds.model.json.KeyValueMap;
import com.ds.service.QuizProcessorService;
import com.ds.service.ServiceResponse;

public class Utils {
    
    public static boolean isValid(String val){
        if(val == null || val.isEmpty() || val.equalsIgnoreCase("undefined")){
            return false;
        }else{
            return true;
        }
    }
    public static Session getSession(Cookie[] cookies){
        String sessionInfo = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.err.println("Cookie:" +cookie.getName());
                if (cookie.getName().equals("session")) {
                    sessionInfo = cookie.getValue();
                    break;
                }
            }
        }
        if(sessionInfo == null){
            System.err.println("Cannot retrieve session cookie.");
            return null;
        }
        return QuizProcessorService.getInstance().getSession(sessionInfo);
    }
    public static void respond(ServiceResponse result, HttpServletResponse response) throws IOException{        
        Gson gson = new Gson();
        String json = gson.toJson(result);  
        System.err.println(json);
        PrintWriter w = response.getWriter();
        response.setContentType("text/json");            
        w.print(json);
        w.close();
    }
    
    public static ServiceResponse sendError(String error){
        ServiceResponse response = new ServiceResponse();
        response.setStatus(false);
        response.setMetaData(new KeyValueMap("message", error).toJson());
        return response;
    }
    public static ServiceResponse sendSuccess(String error){
        ServiceResponse response = new ServiceResponse();
        response.setStatus(false);
        response.setMetaData(new KeyValueMap("message", error).toJson());
        return response;
    }
    public static boolean alreadyStarted(List<QuestionState> qStates) {
        for(QuestionState s:qStates){
            if(s.getState() != QuestionState.QStatEnum.INITIALIZED
                    || s.getState() != QuestionState.QStatEnum.UNINITIALIZED){
                return true;                
            }
        }
        return false;
    }
    public static int getFirstUnattempted(List<QuestionState> qStates) {
        int i;
        for( i = 0; i<qStates.size() ; i++){
            if(qStates.get(i).getState() == QuestionState.QStatEnum.INITIALIZED
                    || qStates.get(i).getState() == QuestionState.QStatEnum.UNINITIALIZED){
                return i;                
            }
        }
        return i;
    }
    public static void resetQState(List<QuestionState> qStates,
            QStatEnum stat) {
        for(QuestionState s:qStates){
            s.setState(stat);                    
        }
        
    }
    public static void dumpModule(Module module) {
        // TODO Auto-generated method stub
        System.err.printf("name=%s, user=%s\n", module.getName(), module.getUser());
        for(QuestionState s: module.getQStates()){
            System.err.printf("qid=%d, state=%s\n", s.getQid(), s.getState().name());
        }
    }
    public static boolean sessionCheck(HttpServletRequest request) {
        Session session = Utils.getSession(request.getCookies());
        if (session == null) {
           System.err.println("Session is null");           
           request.setAttribute("loginop", "in");
           return false;
        } else{            
           request.setAttribute("loginop", "out");
           return true;
        }
    }
    public static Session getSession(String sessionInfo) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
