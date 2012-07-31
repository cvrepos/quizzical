<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link type="text/css" href="../css/custom.css" rel="stylesheet" />
<title>editor Home</title>
</head>
<body>
<div id="wrapper">
	<jsp:include page="../common/header.jsp" />
	<div id="content">
		<div id='notification_banner_text'>
				<div><b>1000</b> tests created. <b>2456</b> tests taken.</div>
		</div>
		<div id='notification_dynamic_text'>
				<div>Trending now.<i>CSAT 2012, MOCK GRE, PSYCHOLOGY</i></div> 
		</div>  	
		<div id='home_info'>
			<ul class='home_info_list'>
				<li class='home_info_item'>
					<div>
						<label>What is take.a.test?</label>
						<p>
						Think of take.a.test as your one stop shop for organizing online tests. As an editor, you will 
			create tests. If you want to share the tests only with your students, you can just publish to them.
			If you want to publish them to the world, just get them reviewed by two of your friends. Once reviewed 
			we will make sure that they reach thousands of our users worldwide. Adding tests is free! Add as many tests 
			as you want. You can earn money from your tests too! To learn more>> 
						</p>
					</div>
				</li>
				<li class='home_info_item'>
					<div>
					<label>How is it different?</label>
						<p>
						It is unique - in that it will provide you statistical analysis of the tests as taken by your students.
						It will provide you and your students analysis on how they have done question-by-question basis.  
						</p>
					</div>
				</li>
			</ul>						
		</div>  		
	</div>
	<jsp:include page="../common/footer.jsp" />
</div>
</body>
</html>
