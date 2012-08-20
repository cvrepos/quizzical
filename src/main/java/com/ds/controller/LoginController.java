package com.ds.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.ds.exception.UserManagementException;
import com.ds.model.Session;
import com.ds.model.User;
import com.ds.service.Utils;
import com.ds.util.ConstStrings;
import com.ds.util.Operation;
import com.ds.util.ResultStrings;



public class LoginController extends Controller {

	private final static Logger log = Logger.getLogger(LoginController.class
			.getName());

	@Override
	public Navigation run() throws Exception {
		log.info("LoginController run()");
		String sop = (String) request.getAttribute(ConstStrings.OP);
		if(!Utils.isValid(sop)){
			return forward(ConstStrings.LOGIN_JSP);
		}
		Operation op = Operation.getOperation(sop);
		if (op.equals(Operation.LOGIN)) {
			return Login();
		} else if (op.equals(Operation.ADDUSER)) {
			return AddUser();
		} else if (op.equals(Operation.LOGOUT)) {
			return Logout();
		} else {
			log.warning("Invalid operation:"+ op.toString());
			return forward(ConstStrings.LOGIN_JSP);
		}

	}


	private Navigation Login() {
		log.info("LOGIN invoked");
		String username = (String) request.getAttribute(ConstStrings.USERNAME);
		String password = (String) request.getAttribute(ConstStrings.PASSWORD);
		if (!Utils.isValid(username) || !Utils.isValid(password)) {
			request.setAttribute(ConstStrings.ERROR, ResultStrings.INVALID_USER);
			return forward(ConstStrings.LOGIN_JSP);
		}
		if (Utils.isValidUser(username, password)) {
			// set the cookie in the response
			Cookie sCookie = new Cookie("session",
					Utils.generateSessionCookie(username));
			// BUG: chrome cannot set a cookie on redirect
			sCookie.setPath("/");
			response.addCookie(sCookie);
			// check for the orig URL that forwarded the user to login
			String origUrl = (String) request.getAttribute(ConstStrings.RETURN_TO);
			if (!Utils.isValid(origUrl)) {
				log.info("No RETURN_TO URL is found");
				return redirect("./viewer/");
			} else {
				origUrl += "?";
				Enumeration<?> attrs = request.getAttributeNames();
				while (attrs.hasMoreElements()) {
					String attr = (String) attrs.nextElement();
					if (attr.startsWith(ConstStrings.QUIZ_NAMESPACE)) {
						origUrl += "&" + attr + "="
								+ request.getAttribute(attr);
					}
				}
				log.info(">>>>>RETURN_TO url:" + origUrl);
				return redirect(response.encodeRedirectURL(origUrl));

			}
		} else {
			request.setAttribute("error",
					"Invalid username or password. Please enter a valid User id and Password.");
			return forward("login.jsp");
		}

	}
	
	private Navigation AddUser() {

		//boolean bValidCaptchaResponse = false;
//		HttpClient httpclient = new DefaultHttpClient();		
//        try {
//            HttpPost httpPost = new HttpPost("http://www.google.com/recaptcha/api/verify");
//            
//            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
//            nvps.add(new BasicNameValuePair("privatekey", "6Ld9-88SAAAAAA44G1Y-EjTsdVllUCKiPTPcFX0F"));
//            String remoteip = request.getRemoteAddr();            
//            nvps.add(new BasicNameValuePair("remoteip", remoteip));
//            String challenge = (String) request.getAttribute("recaptcha_challenge_field");
//            if(!Utils.isValid(challenge)){
//            	//ERROR
//            }
//            
//            nvps.add(new BasicNameValuePair("challenge", challenge));
//            String response = (String) request.getAttribute("recaptcha_response_field");
//            nvps.add(new BasicNameValuePair("response", response));   
//            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps);              
//            httpPost.setEntity(entity);
//           
//            
//                     
//            // Create a response handler
//            ResponseHandler<String> responseHandler = new BasicResponseHandler();
//            String responseBody = httpclient.execute(httpPost, responseHandler);            
//            log.info(responseBody);
//            if(responseBody != null){
//            	String[] responseLines = responseBody.split("\n");
//            	if(responseLines.length >= 2){
//            		log.info("line 1:"+ responseLines[0]);
//            		log.info("line 2:"+ responseLines[0]);
//            	}
//            }
//            
//            return redirect("./viewer/");
//
//        } catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//            // When HttpClient instance is no longer needed,
//            // shut down the connection manager to ensure
//            // immediate deallocation of all system resources
//            httpclient.getConnectionManager().shutdown();
//        }
        
		
		
		String username = (String) request.getAttribute("username");
		String password = (String) request.getAttribute("password");
		String email = (String) request.getAttribute("email");
		if (!Utils.isValid(username)) {
			request.setAttribute("error", "Username cannot be null");
			return forward("login.jsp");
		}
		if (!Utils.isValid(password)) {
			request.setAttribute("error", "Password cannot be null");
			return forward("login.jsp");
		}
		if (!Utils.isValid(email)) {
			request.setAttribute("error", "Email cannot be null");
			return forward("login.jsp");
		}
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		try {
			Utils.addUser(user);
		} catch (UserManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return redirect("./viewer/");
	}
	
	private Navigation Logout() {

		Session session = Utils.getSession(request.getCookies());
		log.info("logout requested.");
		// clean the session
		Utils.removeSession(session);
		Cookie cookie = new Cookie("session", "");
		cookie.setMaxAge(0);
		cookie.setComment("EXPIRING COOKIE at " + System.currentTimeMillis());
		response.addCookie(cookie);
		return redirect("./logout");

	}
	

}
