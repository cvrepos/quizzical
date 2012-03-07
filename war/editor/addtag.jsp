<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>editor Addtag</title>
</head>
<body>

<form method='post' action='process'>
	<label for='parent'>Parent</label>
	<input type='text' name='parent'></input>
	<label for='childs'>Childs</label>
	<textarea name='childs'></textarea>
	<input type='hidden' name='op' value='addtag'/>
	<input type='submit' value='submit'/>
</form>

</body>
</html>
