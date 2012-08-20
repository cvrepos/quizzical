package com.ds.controller.editor;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.util.RequestMap;

import com.google.gson.Gson;

import com.ds.model.Module;
import com.ds.model.Session;
import com.ds.model.Tag;
import com.ds.model.Question;
import com.ds.service.Utils;
import com.ds.service.ServiceResponse;
import com.ds.util.ConstStrings;


public class ProcessController extends Controller {

   
    private static final Logger log = Logger.getLogger(ProcessController.class.getName());
    @Override
    public Navigation run() throws Exception {
        log.info("Processing quiz");
        Session session = Utils.getSession(request.getCookies()); 
        if(session == null){
            return redirect("../login");
        }
        //based on the opcode process request
        String op = (String) request.getAttribute(ConstStrings.QUIZ_NAMESPACE + "op");
        if(!Utils.isValid(op)){
            throw new ServletException("op cannot be empty");
        }
        if(op.equals("process")){        	
        	//based on the state of the module - operations will be performed
        	log.info("In op=process");
        	String mid = (String) request.getAttribute(ConstStrings.QUIZ_NAMESPACE + "mid");
            String mname = (String) request.getAttribute(ConstStrings.QUIZ_NAMESPACE + "mname");
            request.setAttribute(ConstStrings.QUIZ_NAMESPACE +"modid", mid);
            request.setAttribute(ConstStrings.QUIZ_NAMESPACE +"mname", mname);
            return forward("modproc.jsp");
        }                
        else if(op.equals("add")){
            log.info("op=add");
            //convert the question to appropriate type
            String type = (String) request.getAttribute("type");
            if(!Utils.isValid(type)){
                throw new ServletException("type cannot be empty");
            }            
            String question = (String) request.getAttribute("question");
            if(!Utils.isValid(question)){
                throw new ServletException("question cannot be empty");
            }      
            String mid = (String) request.getAttribute("mid");            
            respond(Utils.addQuestion(type, null, question, session, mid));
                                 
        } else if(op.equals("delete")){
            log.info("op=delete");
            //convert the question to appropriate type
            String key = (String) request.getAttribute("key");
            if(!Utils.isValid(key)){
                throw new ServletException("key cannot be empty");
            }                                    
            respond(Utils.deleteQuestion(key, session));            
        } else if (op.equals("update")){
            log.info("op=update");
            //convert the question to appropriate type
            String key = (String) request.getAttribute("key");
            if(!Utils.isValid(key)){
                throw new ServletException("key cannot be empty");
            }                                    
            String question = (String) request.getAttribute("question");
            if(!Utils.isValid(question)){
                throw new ServletException("question cannot be empty");
            }
            respond(Utils.updateQuestion(key, question, session));            
        }  else if(op.equals("gettags")){
            log.info("gettags");
            Iterator<Tag> tags = Utils.getTags(20);
            PrintWriter w = this.response.getWriter();
            response.setContentType("text/html");
            String t = "<table id='tag-table'>\n";
            while(tags.hasNext()){                
                Tag tag  = tags.next();
                t += "<tr><td class='tag-name'>"+tag.getName() + "</td>" +
                    "<td class='tag-count'>"+tag.getCount() + "</td></tr>\n";                    
                
            }
            t += "</table>";
            w.print(t);
            w.close();
            
        }else if(op.equals("query")){
            log.info("op=query");            
            String type = (String) request.getAttribute("type");
            if(!Utils.isValid(type)){
                throw new ServletException("search string cannot be empty");
            }
            String query = (String) request.getAttribute("query");
            if(!Utils.isValid(query)){
                throw new ServletException("search string cannot be empty");
            }                                                
            respond(Utils.search(type, query, session)); 
            
        }else if(op.equals("download")){
            String type = (String) request.getAttribute("type");
            if(!Utils.isValid(type)){
                log.info("type is null");
                type = "json";
            }
            download(session, type);
            
        }else if(op.equals("addtag")){
            String parent = (String) request.getAttribute("parent");
            String childs = (String) request.getAttribute("childs");
            Utils.addTag(parent, childs, false);
            return forward("addtag.jsp");
        } else if(op.equals("create_mod")){
            String mname = (String) request.getAttribute(ConstStrings.QUIZ_NAMESPACE + "module");
            String description = (String) request.getAttribute(ConstStrings.QUIZ_NAMESPACE + "description");
            String parent = (String) request.getAttribute(ConstStrings.QUIZ_NAMESPACE + "parent");
            String prevSibling = (String) request.getAttribute(ConstStrings.QUIZ_NAMESPACE + "prevSibling");
            Module mod = null;
            if(parent != null && !parent.equals("none") ){    
            	log.info("calling createModuleAsChild");
            	mod = Utils.createModuleAsChild(mname, description, Long.parseLong(parent), session.getUser());
            }
            else if(prevSibling != null && !prevSibling.equals("none")){
            	log.info("calling createModuleAsSibling");
            	mod = Utils.createModuleAsSibling(mname, description, Long.parseLong(prevSibling), session.getUser());
            } else {
            	log.info("calling createModule");
            	mod = Utils.createModule(mname, description,session.getUser());
            }
            request.setAttribute(ConstStrings.QUIZ_NAMESPACE +"modid", mod.getKey().getId());
            request.setAttribute(ConstStrings.QUIZ_NAMESPACE +"mname", mod.getName());
            return forward("module.jsp");
        } else if(op.equals("update_mod")){
            String mid = (String) request.getAttribute(ConstStrings.QUIZ_NAMESPACE + "mid");
            String mname = (String) request.getAttribute(ConstStrings.QUIZ_NAMESPACE + "mname");
            request.setAttribute(ConstStrings.QUIZ_NAMESPACE +"modid", mid);
            request.setAttribute(ConstStrings.QUIZ_NAMESPACE +"mname", mname);
            return forward("module.jsp");
        }
        else if(op.equals("load_module")){
        	log.info("load_module");
            String mid = (String) request.getAttribute(ConstStrings.QUIZ_NAMESPACE + "mid");
            Utils.respond(Utils.loadModule(mid), response);
            return null;
        }
        else if(op.equals("update_module_name")){
        	log.info("update_module_name");
            String mid = (String) request.getAttribute(ConstStrings.QUIZ_NAMESPACE + "mid");
            String mname = (String) request.getAttribute(ConstStrings.QUIZ_NAMESPACE + "mname"); 
            Module module = Utils.getModule(mid);
            if(module == null){
            	Utils.sendError("Unable to find module");
            }
            module.setName(mname);
            Utils.respond(Utils.updateModule(module), response);
            return null;
        }
        else if(op.equals("update_module_description")){
        	log.info("update_module_description");
            String mid = (String) request.getAttribute(ConstStrings.QUIZ_NAMESPACE + "mid");
            String description = (String) request.getAttribute(ConstStrings.QUIZ_NAMESPACE + "description"); 
            Module module = Utils.getModule(mid);
            if(module == null){
            	Utils.sendError("Unable to find module");
            }
            module.setDescription(description);
            Utils.respond(Utils.updateModule(module), response);
            return null;
        }
        return null;
    }
    
    
    private void download(Session session, String type) throws IOException{
        log.info("search");
        String val = (String) request.getAttribute("val");
        if(!Utils.isValid(val)){
           return;
        }
        //parse the search string question:string;tags:abc,efg;time:mm-dd-yyyy,mm-dd-yyyy        
        String question = null;
        Date start = null;
        Date end = null;
        List<String> tags = new ArrayList<String>(4);
        String[] parts = val.split(";");
        String key,value;
        for(String part:parts){
            String[] kvp = part.split(":");
            if(kvp.length == 1){
                //wrong format
                log.info("invalid search string:"+ part);
                continue;
            }
            if(kvp[1].trim().isEmpty()) {
              //wrong format
                log.info("invalid search string:"+ part);
                continue;
            }
            key = kvp[0].trim();            
            if(key.equalsIgnoreCase("question")){
                question = kvp[1].trim();
            }else if(key.equalsIgnoreCase("tags")){
                String[] vals = kvp[1].split(",");
                for(String tag:vals){
                    tags.add(tag.trim());
                }
            }else if(key.equalsIgnoreCase("time")){
                String[] vals = kvp[1].split(",");
                SimpleDateFormat df = new SimpleDateFormat("MM-DD-YYYY");
                try {
                    start = df.parse(vals[0]);
                    if(vals.length >1){
                        end = df.parse(vals[1]);
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            
            }
        }
        PrintWriter w = this.response.getWriter();
        response.setContentType("text/html");
        //parsing is done now perform query
        Iterator<Question> quizes = Utils.getQuizes(question, tags, start, end, session.getUser());
        Gson gson = new Gson();
        while(quizes.hasNext()){
            Question q = quizes.next();
            log.info(gson.toJson(q));
            w.println(gson.toJson(q));
        }        
        w.close();
    }
    private void respond(ServiceResponse result) throws IOException{        
        Gson gson = new Gson();
        String json = gson.toJson(result);  
        log.info(json);
        PrintWriter w = this.response.getWriter();
        response.setContentType("text/json");            
        w.print(json);
        w.close();
    }
}
