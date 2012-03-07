<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>


<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>viewer Index</title>			
		<link type="text/css" href="../css/cube.css" rel="stylesheet" />	
		<script type="text/javascript" src="../js/jquery-1.6.2.min.js"></script>
		<script type="text/javascript" src="../js/json2.js"></script>
		<script type="text/javascript">
		<% if (request.getAttribute("QN_action") != null && request.getAttribute("QN_mname") != null ){ 
		%>
		var action = "<%= request.getAttribute("QN_action") %>";
		var module = "<%= request.getAttribute("QN_mname") %>";
		<%
		}
		%>
		$(function() {
			var loadResponse = function(response) {
				if(response.status == true){
					var qObj = JSON.parse(response.metaData);
					if(qObj.code == "NEXT" || qObj.code == "AVAILABLE"){
						doAjax(qObj.params);
						return;					
					} else if(qObj.code == "ALREADY_STARTED"){
						$('#question_container').hide();					
						$('#message_board').html("You have already started the module. <br/>" + 
								"If you want to restart the module please click restart. <br/>" + 
								"If you want to resume the module please click resume button. <br/>" + 
								"<input type='button' id='reset-button' value='restart'/>"  + 
								"<input type='button' id='resume-button' value='resume'/>"
								);
						$('#reset-button').click( function(){
								doAjax(qObj.reset);
						});
						$('#resume-button').click( function(){
								doAjax(qObj.resume)
						});
						return;
					} else if(qObj.code == "COMPLETED"){
						alert("Congratulations! You have completed the module.");
						window.location.href = "./result?QN_mname="+ qObj.mname;
						return;						
					}
					else {									
						$('#question').html(qObj.question);
						$('#ans1').html(qObj.ans1);
						$('#ans2').html(qObj.ans2);
						$('#ans3').html(qObj.ans3);
						$('#ans4').html(qObj.ans4);
						$('#qid' ).val(qObj.qid);
						$('#mname').val(qObj.mname);
						$('#message_board').hide();
						$('#question_container').show();
					}					
				}else if(response.status == false){
					var qObj = JSON.parse(response.metaData);
					$('#question_container').html(qObj.message);
				}else{
					$('#question_container').html("Error getting response.");
				}
			};
			var doAjax = function(dataString){
				dataString += "&m=y";
				//alert(dataString);
				$.ajax({					
					type: "POST",
					url: "./process",
					data: dataString,
					success: loadResponse
				  	});
			};
			
			if(action == 'start' && module){
				var dataString = "QN_action=start&QN_mname=" + module;
				doAjax(dataString);				
			}else{
				$("#question_container").hide();
			}
			$('#next-button').click(function() {
				var dataString = "QN_action=submit&QN_qid=" + $('#qid').val() + "&QN_mname=" + $('#mname').val() + "&QN_ans=";
				if($('#c1').is(':checked')){
		          dataString += "1,";   
				}
				if($('#c2').is(':checked')){
			          dataString += "2,";   
				}
				if($('#c3').is(':checked')){
			          dataString += "3,";   
				}
				if($('#c4').is(':checked')){
			          dataString += "4,";   
				}
				$('input:checkbox').removeAttr('checked');						
				doAjax(dataString);	
				
	  		});
		});
		</script>				
</head>
<body>
<h1>Career Vision</h1>
<header>
	<a href="">Demo</a>	 
	<a href="../login?op=<%=request.getAttribute("loginop")%>">Log<%=request.getAttribute("loginop") %></a>
</header>
<div id='message_board'>
</div>
<div id='question_container' >
	<p id='question'></p>
	<ul class='answers'>
		<input type='checkbox'  id=c1>(A)<li id='ans1'></li></input>
		<input type='checkbox'  id=c2>(B)<li id='ans2'></li></input>
		<input type='checkbox'  id=c3>(C)<li id='ans3'></li></input>
 		<input type='checkbox'  id=c4>(D)<li id='ans4'></li></input>
	</ul>
	<input type='hidden' id='qid' value='' />
	<input type='hidden' id='mname' value='' />
	<input type="button" id='prev-button' value='prev'/>
	<input type="button" id='next-button' value='next'/>
	<input type="button" id='complete-button' value='complete'/>        
</div>
<footer>
	<a href="">Home</a>	
	<a href="">About Us</a>
</footer>
</body>
</html>
