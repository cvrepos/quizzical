package com.ds.controller;

import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.ds.model.Session;
import com.ds.model.User;
import com.ds.service.QuizProcessorService;
import com.ds.util.StaticValues;
import com.ds.util.Utils;

public class LoginController extends Controller {
    

    private QuizProcessorService service = QuizProcessorService.getInstance();
    private final static Logger log = Logger.getLogger(LoginController.class.getName());
    @Override
    public Navigation run() throws Exception {
        
        Session session = Utils.getSession(request.getCookies());
        if(session == null){
            //check if the user is attempting login
            String op = (String)request.getAttribute("op");
            if(Utils.isValid(op) && op.equals("login")){
                String username = (String)request.getAttribute("username");
                String password = (String)request.getAttribute("password");
                if(!Utils.isValid(username) || !Utils.isValid(password)){
                    request.setAttribute("error", "Please enter a valid User id and Password.");
                    return forward("login.jsp");
                }
                
                if(service.isValidUser(username, password)){
                    //set the cookie in the response
                    Cookie sCookie = new Cookie("session", service.generateSessionCookie(username));
                    //BUG: chrome cannot set a cookie on redirect
                    sCookie.setPath("/");
                    response.addCookie(sCookie);                    
                    //check for the orig URL that forwarded the user to login
                    String origUrl = (String) request.getAttribute("orig");                    
                    if(!Utils.isValid(origUrl)){
                        log.info("No orig URL is found");  
                        return redirect("./viewer/");
                    }else{
                        origUrl +="?";
                        Enumeration<?> attrs = request.getAttributeNames();                        
                        while(attrs.hasMoreElements()){
                            String attr = (String)attrs.nextElement();
                            if(attr.startsWith(StaticValues.QUIZ_NAMESPACE)){                                
                                origUrl += "&" + attr +"="+ request.getAttribute(attr);
                            }                                                        
                        }
                        log.info(">>>>>Orig url:" + origUrl);
                        return redirect(response.encodeRedirectURL(origUrl));
                        
                    }                
                }else{
                    request.setAttribute("error", "Invalid username or password. Please enter a valid User id and Password.");
                    return forward("login.jsp");
                }                
                
            }else if("adduser".equals(op)){
                String username = (String)request.getAttribute("username");
                String password = (String)request.getAttribute("password");
                String email = (String)request.getAttribute("email");
                if(!Utils.isValid(username)){
                    request.setAttribute("error", "Username cannot be null");
                    return forward("login.jsp");
                }
                if(!Utils.isValid(password)){
                    request.setAttribute("error", "Password cannot be null");
                    return forward("login.jsp");
                }
                if(!Utils.isValid(email)){
                    request.setAttribute("error", "Email cannot be null");
                    return forward("login.jsp");
                }
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);
                service.addUser(user);
                return redirect("./viewer/");
            }
            
        }else{
            //check if there is a request for logout
            String op = (String)request.getAttribute("op");
            if(Utils.isValid(op)){
               if(op.equals("logout")){
                   log.info("logout requested.");
                   //clean the session
                   service.removeSession(session);  
                   Cookie cookie = new Cookie("session", "");                   
                   cookie.setMaxAge(0);                   
                   cookie.setComment("EXPIRING COOKIE at " + System.currentTimeMillis());
                   response.addCookie(cookie);
                   return redirect("./logout");
               }
            }
        }
        return forward("login.jsp");
        
    }
}
