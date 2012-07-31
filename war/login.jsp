<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Login</title>
	<link type="text/css" href="/css/custom.css" rel="stylesheet" />		

</head>
<body>
<div id='wrapper'>
	<jsp:include page="/common/header.jsp" />
	<div id='content'>
		<div id='notification_banner_text'>
		<b><%
			if (request.getAttribute("error") != null) {    
    			out.println("<div><i>"+request.getAttribute("error")+"</i></div>");
			}else{
				out.println("<div>Welcome to take.a.test! </div>");
			}
		%></b>
		</div>
		<div id='user_access'>
			<div id='login'>
			<form id='form_sign_in' method='post' action=''>
			  <label class='legendlabel'>Sign In</label>
			  
			  <div class='formentry'>										
						<label for='username'>Username</label>
						<input type='text' class='textinput' name='username'></input>
				</div>
				<div class='formentry'>
					<label for='password'>Password</label>
					<input type='password' class='textinput' name='password'></input>
				</div>		
				<div class='buttons'>		
					<input type='hidden' name='op' value='login'></input>
					<input type='submit' id='button_login' name='login' value='Login'></input>
					<input type='button' id='button_reset_password' name='reset_password' value='Forgot Password?'></input>
					<input type='button' id='button_new_user' name='new_user' value='New User?'></input>
				</div>
				
			</form>	
	 		</div>	 	
	 		<div id='create_user'>
	 			<form id='form_create_user' method='post' action=''>
	 			<label class='legendlabel'>Create New User</label>	 			
	 			
	 				<div class='formentry'>									
						<label for='username'>Username</label>
						<input type='text' class='textinput' name='username'></input>
					</div>
	 				
					<div class='formentry'>
						<label for='password'>Password</label>
						<input type='password' class='textinput' name='password'></input>
					</div>
					<div class='formentry'>			
						<label for='conf_password'>Confirm Password</label>
						<input type='password' class='textinput' name='conf_password'></input>
					</div>
					<div class='formentry'>									
						<label for='email'>Email</label>
						<input type='text' class='textinput' name='email'></input>
					</div>
					<div class='formentry'>									
						<label for='challenge'>Challenge</label>
						<input type='text' class='textinput' name='challenge'></input>
					</div>
					<div class='formentry'>									
						<label for='answer'>Answer</label>
						<input type='text' class='textinput' name='answer'></input>
					</div>
					<div class='captch'>					
						<script type="text/javascript" src="http://www.google.com/recaptcha/api/challenge?k=6Ld9-88SAAAAAA44G1Y-EjTsdVllUCKiPTPcFX0F">
  						</script>
  						<noscript>
     						<iframe src="http://www.google.com/recaptcha/api/noscript?k=6Ld9-88SAAAAAA44G1Y-EjTsdVllUCKiPTPcFX0F"
         						height="300" width="500" frameborder="0"></iframe><br>
     						<textarea name="recaptcha_challenge_field" rows="3" cols="40">
     						</textarea>
     						<input type="hidden" name="recaptcha_response_field" value="manual_challenge">         						
  						</noscript>	
  					</div>
  					<div class='buttons'>					 
						<input type='hidden' name='op' value='create_user'></input>
						<input type='submit' id='bn_add_new_user' name='add_new_user' value='Create New Account'></input>
					</div>					
								 
				</form>	
	 		</div>
	 	</div>
	</div>	
	<jsp:include page="/common/footer.jsp" />
</div>
</body>
	<script type="text/javascript" src="/js/jquery.min.js"></script>
	<script type="text/javascript">
	$(function(){		
		$('#form_create_user').hide();
		$('#button_new_user').click(function(){
			$('#form_sign_in').parent().html($('#form_create_user').html()).show();
			
		});		
	});	
	</script>
</html>
