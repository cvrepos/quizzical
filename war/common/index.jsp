<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>common Index</title>
</head>
<body>
<div id='user_access'>
			<div id='login'>
			<form id='form_sign_in' method='post' action='/common'>
			  <label class='legendlabel'>Sign In</label>
			  
			  <div class='formentry'>										
						<label for='email'>Email</label>
						<input type='text' class='textinput' name='email'></input>
				</div>					
				<div class='buttons'>		
					<input type='hidden' name='op' value='login'></input>
					<input type='submit' id='button_login' name='login' value='Login'></input>					
				</div>
				
			</form>	
	 		</div>	
</body>
</html>
