<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Welcome to Quiz Editor</title>
<link type="text/css" href="../css/custom.css" rel="stylesheet" />
<link type="text/css" href="../css/jquery.dataTables.css" rel="stylesheet" />
<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/jquery.dataTables.min.js"></script>

<style>
</style>
<script type="text/javascript">

$(function(){
	var mname;
	var mid;
	<% if (request.getAttribute("QN_mname") != null && request.getAttribute("QN_mid") != null ){ 
		%>
		mname = "<%= request.getAttribute("QN_mname") %>";
		mid = "<%= request.getAttribute("QN_mid") %>";
		<%
		}
	%>
	
	
		
	var loadResponse = function(response) {
		alert("submitted");
		if(response.status == true){
			var qObj = JSON.parse(response.metaData);
			if(qObj.code == "ADDED"){											
				$("#message_board").html("Successfully added the question to module." 
						+ "Total questions added: <i>" + qObj.total + "</i>.");
				return;					
			}
			
		}
	};
			
	var doAjax = function(dataString, onSuccess){
		dataString += "&m=y";
		//alert(dataString);
		$.ajax({					
			type: "POST",
			url: "./process",
			data: dataString,
			success: onSuccess										
		  	});
	};
		
    var onModuleUpdate = function(response){
			if(response.status == true){				
				var qObj = JSON.parse(response.metaData);
				if(qObj.code == "SAVED"){
					console.log("Module updated");
				}
			}else{
				console.log("Failed to update");
			}
			
	};
		
	var onModuleLoad = function(response){
			if(response.status == true){
				console.log("response received");
				var qObj = JSON.parse(response.metaData);
				if(qObj.code == "LOADED"){	
					questions = new Array();
					var count = 0;			
					moduleId = $("#module_id").val();				
					$.each(qObj.questions, function(index, question){
						var tQuestion = new Array();
						paramPos["index"] = tQuestion.push(++count);
						paramPos["qid"] = tQuestion.push(question.key.id);
						paramPos["question"] = tQuestion.push(question.question);
						// TODO: make it server side
						var tags = new Array();
						question.tags.splice(-1, 1);
						paramPos["tags"] = tQuestion.push(question.tags);
						paramPos["hints"] = tQuestion.push(question.hints);
						paramPos["answer1"] = tQuestion.push(question.answers[0]);						
						paramPos["answer2"] = tQuestion.push(question.answers[1]);
						paramPos["answer3"] = tQuestion.push(question.answers[2]);
						paramPos["answer4"] = tQuestion.push(question.answers[3]);
						questions.push(tQuestion);
						console.log("questions loaded:" + question.key.id);
					});	
					$("module_description").val(qObj.description);
						
													
				}
			}
	 }
	 if(mid != null && mname != null){
	     doAjax("QN_op=load_module&QN_mid=" + mid, onModuleLoad);
	     $("module_name").val(mname);
	     $("module_id").val(mid);
	 } 	            
	    
	  $("#save-name-button").click(function(){
			 var mname = $("#module_name").val();
			 var mid   = $("#module_id").val();
			 doAjax("QN_op=update_module_name&QN_mid=" + mid + "&QN_mname=" + mname + "", onModuleUpdate);
		});
		$("#save-description-button").click(function(){
			 var description = $("#module_description").val();
			 var mid   = $("#module_id").val();			 
			 doAjax("QN_op=update_module_description&QN_mid=" + mid + "&QN_description=" + description + "", onModuleUpdate);
		});
	});

</script>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="../common/header.jsp" />
		<div id="content">
		    <div id='notification_banner_text'>
				<div>Welcome take.a.test editor!</div>
			</div>
    		
		
			<div id='module_info'>
				<h3>Name</h3>
				<input type='text' id='module_name'></input>
				<input type='hidden' id='module_id'></input>
				<input type='button' id='save-name-button' value='Save'></input>
				<h3>Description</h3>
				<textarea id='module_description'></textarea>
				<input type='button' id='save-description-button' value='Save'></input>	
				<h3>Questions</h3>
				<div id='module_questions'></div>	
				<input type='button' id='back-button' value='Back'/>
			</div>
	
			
		</div>
		<jsp:include page="../common/footer.jsp" />
	</div>
</body>
</html>
