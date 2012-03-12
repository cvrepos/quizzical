<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>

<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<title>Welcome to Quiz Editor</title>
		<link type="text/css" href="../css/cube.css" rel="stylesheet" />
		 
		<script type="text/javascript" src="../js/jquery-1.6.2.min.js"></script>
		<script type="text/javascript" src="../js/jquery-ui-1.8.16.custom.min.js"></script>
		<script type="text/javascript">
		$(function(){
			var modJsons  = new Array();
			<c:forEach var="m" items="${modules}">
			modJson = ${m};
			modJsons.push(modJson);
			</c:forEach>
			$(function(){
				 $.each(modJsons, function(index, module){		 
					 $("#modules").append("<li><a href='process?QN_op=update_mod&QN_mid=" 
							 + module.mid + "&QN_mname=" + module.mname +"'>" + module.mname + "</a> Total Questions: " + module.total + " </li> ");
				 });
			});
						
			$('#module-form').hide();		
			$('#create-button').click( function() {
				$('#module-form').show();	
			});
		});
		</script>
</head>
<body>
<h1>Career Vision</h1>
<jsp:include page="../common/header.jsp" />
<ul class="toolbar" id="tools">  	
</ul>

<p>Welcome to the quiz editor. </p>
<p>These are your modules:</p>
<ul id='modules'>

</ul>

<ul>
	<li><input type="button" id='create-button' value='Create Module'/></li>
	<li><input type="button" id='update-button' value='Update Module'/></li>	
</ul>

<div id = 'module-form'>
 	<form method='post' action="./process">
 		<label for='QN_module'>Enter the module name:</label>
 		<input type='text' name='QN_module'></input>
 		<input type='hidden' name='QN_op' value='create_mod'></input>
 		<input type='submit' name='submit' value='submit'/>
 	</form>
</div>
<div id="spinner" class="spinner" style="display:none;">
	<img id="img-spinner" src="../css/images/ajax-loader.gif" alt="Loading"/>
</div>

<jsp:include page="../common/footer.jsp" />
</body>
</html>
