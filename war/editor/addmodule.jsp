<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Welcome to Quiz Editor</title>
<link type="text/css" href="../css/custom.css" rel="stylesheet" />

<style>
</style>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="../common/header.jsp" />
		<div id="content">
		    <div id='notification_banner_text'>
				<div>Welcome take.a.test editor!</div>
			</div>    		
			<div id='module_form'>
			<form action="/editor/process">
				<label for='QN_module'>Enter the module name:</label> <input
					type='text' name='QN_module'></input><br /> <label
					for='QN_description'>Briefly describe the module:</label>
				<textarea name='QN_description'></textarea>
				<label for='QN_parent'>Select a parent module:</label> <select
					name='QN_parent' class='options'>
				<option value='none'>None</option>
				</select> <label for='QN_prevSibling'>Or select a previous sibling
				module:</label> <select name='QN_prevSibling' class='options'>
				<option value='none'>None</option>
				</select> <input type='hidden' name='QN_op' value='create_mod'></input> <input
				type='submit' name='submit' value='submit' />
			</form>
			</div>	
		</div>
		<jsp:include page="../common/footer.jsp" />
	</div>
</body>
</html>
