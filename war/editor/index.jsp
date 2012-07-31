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
	//show only the module table 	
	$("#module_views").show();
	$("#module_form").hide();
	$("#module_info").hide();
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
		
	var modJsons  = new Array();
	<c:forEach var="m" items="${modules}">
	modJson = ${m};
	modJsons.push(modJson);
	</c:forEach>			 			 		
	 var modulesArray = new Array();
	 var options = $(".options");				 
	 $.each(modJsons, function(index, module){		 					 
		 console.log(">>> Start module:" + module.mname);				 				 				 
		 var modArray = new Array();
		 modArray.push(module.mid ? module.mid :"-");
		 modArray.push(module.mname ? module.mname :"-");
		 modArray.push(module.description? module.description :"-");
		 modArray.push(module.questionCount? module.questionCount :"-");
		 modArray.push(module.cloneCount? module.cloneCount :"-");
		 modArray.push(module.state? module.state :"-");
		 modArray.push(module.parent? module.parent : "-");
		 modArray.push(module.prevSibling ?module.prevSibling :"-");				 
		 modulesArray.push(modArray);
		 options.append($("<option />").val(module.mid).text(module.mname));
		 console.log("<<< End module:" + module.mname);
	 });				 
										
		$('#create-button').click( function() {
			$('#module_form').show();	
		});

		//query to obtain all questions as 
		$('#modules').html( '<table cellpadding="0" cellspacing="0" border="0" class="display" id="module_table"></table>' );
		var oTable  = $("#module_table").dataTable( {
			"aaData": modulesArray,
			"aoColumns": [
						{ "sTitle": "Id" },
						{ "sTitle": "Name" },
						{ "sTitle": "Description" },
						{ "sTitle": "#Questions" },
						{ "sTitle": "#Downloads" },
						{ "sTitle": "State" },
						{ "sTitle": "Parent Module" },
						{ "sTitle": "Prev Module" }
					],
			"sDom" : '<"toolbar">frtip'
		});
		$("div.toolbar").html("<input type='button' id='button_add_module' value='Add New Module'></input>");
		
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
		var onQuestionUpdate = function(response){
			alert("Question update");
			if(response.status == true){
				var qObj = JSON.parse(response.metaData);
				alert(qObj);
				
			}
		};
		var onQuestionLoad = function(response){
			alert(response.metaData);
		};
		//the question container
		var questions;	
		var paramPos = new Array();	
		var moduleId;
		var getParam = function(question, param){
			return question[paramPos[param] -1 ];
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
													
					$('#module_questions').html( '<table cellpadding="0" cellspacing="0" border="0" class="display" id="questions_table"></table>' );
		        	var oTable = $("#questions_table").dataTable( {
		        		"aaData": questions,
		        		"aoColumns": [
		      		        		{ "sTitle": "Serial"},
		        					{ "sTitle": "Qid" },
		        					{ "sTitle": "Question" },		        						
		        					{ "sTitle": "Tags" },
		        					{ "sTitle": "Hints" }
		        				]
		        			});
		        	$("#module_views").hide();
		            $("#module_info").show();	
		         	// install handlers for update and delete
				    var questionSubmitter = function(operation, qid){
					        console.log("question submitter invoked:" + operation);		                         
				            var dataString  = 'type=' + $('#type').val() + '&'; 
				            var question = new Object();
				            question.question =  $('#question').val() ; 
				            var answers = new Array();
				            answer = new Object();            
				            answer.ans = $('#answer1').val() ; 
				            if($('#isCorrect1').is(':checked')){  
				               answer.isCorrect = true; 
				            }else{ 
				               answer.isCorrect = false; 
				            }
				            answer.id = 1; 
				            answers.push(answer);
				            answer = new Object();            
				            answer.ans = $('#answer2').val() ; 
				            if($('#isCorrect2').is(':checked')){  
				               answer.isCorrect = true; 
				            }else{ 
				               answer.isCorrect = false; 
				            }
				            answer.id = 2; 
				            answers.push(answer);
				            answer = new Object();            
				            answer.ans = $('#answer3').val() ; 
				            if($('#isCorrect3').is(':checked')){  
				               answer.isCorrect = true; 
				            }else{ 
				               answer.isCorrect = false; 
				            }
				            answer.id = 3; 
				            answers.push(answer); 
				            answer = new Object();            
				            answer.ans = $('#answer4').val() ; 
				            if($('#isCorrect4').is(':checked')){  
				               answer.isCorrect = true; 
				            }else{ 
				               answer.isCorrect = false; 
				            }
				            answer.id = 4; 
				            answers.push(answer);  
				            question.answers = answers;
				            question.hints = $('#hints').val();             
				            question.tags =  $('#tags').val().split(',') ;
				            question.tags.push(moduleId); 
				            dataString += 'QN_op=' + operation + '&';
				            dataString += 'mid=' + moduleId + '&'; 
				            dataString += 'key=' + qid + '&'; 
				            dataString += 'ajax=y&'; 		             
				            dataString += 'question=' + JSON.stringify(question, null, 2);  
				            doAjax(dataString, onQuestionUpdate);				            				            
					};
					
		            var editQuestion = function(qrow){
		            	var index = $('td:first', qrow).text();
		            	console.log("Editing question:" + index);
						var question = questions[index -1];
						var qid  = getParam(question, "qid");
						//add a form to the question_editor -if already exists replace it
						$('#question_editor').html( "<form  id='question_form'></form>");						
				        var questionContent = "<fieldset>" +
				            "<legend>Question#" + getParam(question, "qid") + "</legend>"+
				            "  <span class='formentry'>"
				                + "   <label for='question' style='display:block'>Question<font color='red'>*</font>:</label>"
				                + "   <textarea name='question' id='question' class='required' >"
				                + getParam(question, "question")
				                + "</textarea>"
				                + "  </span>";
				        //options
				        for(var i = 1; i<=4 ; i++){     
					        var answer = getParam(question, "answer" + i);
				        	questionContent += "  <span class='formentry'>"
				                    + "   <label for='answer"
				                    + i
				                    + "' style='display:block' >Option "
				                    + i
				                    + "<font color='red'>*</font>:</label> "
				                    + "   <textarea name='answer"
				                    + i
				                    + "' id='answer"+ i + "' style='display:block'>"
				                    + answer.ans
				                    + "</textarea>";
				            if (answer.isCorrect) {
				                questionContent +=
				                    "    Is the above option correct?<input type='checkbox' id='isCorrect"
				                        + i
				                        + "' name='isCorrect"
				                        + i
				                        + "' checked></input>"
				                        + "  </span>";
				            } else {
				                questionContent +=
				                    "    Is the above option correct?<input type='checkbox' id='isCorrect"
				                        + i
				                        + "' name='isCorrect"
				                        + i
				                        + "'></input>"
				                        + "  </span>";
				            }
				        }//options complete
				        questionContent +=
				            "  <span class='formentry'>"
				                + "   <label for='tags' >Tags:</label> "
				                + "   <textarea name='tags' id='tags' style='display:block' >"
				                + getParam(question, "tags")
				                + "</textarea>"
				                + "  </span>"
				                +

				                "   <input type='hidden' name='type' id='type' value='objective'/>"
				                +

				                "  <span class='formentry'>"
				                + "   <label for='hints' >Hints:</label> "
				                + "   <textarea name='hints' id='hints' style='display:block' >"
				                + getParam(question,"hints")
				                + "</textarea>"
				                + "  </span>" +
				                "    <input type='hidden' id='form_qid' value='"+ getParam(question,"qid") +"'></input>" +	
				                "    <input type='button' value='update' id='update-question-button'></input>" +				              
				                "    <input type='button' value='delete' id='delete-question-button'></input>" +
				                "</fieldset>" +
				                "</form>";

		                $("#question_form").html(questionContent);
		                $("#update-question-button").click(function(){
			                var qid = $("#form_qid").val();
		                	questionSubmitter("update", qid);
			            });
		                $("#delete-question-button").click(function(){
		                	var qid = $("#form_qid").val();
		                	questionSubmitter("delete",qid);
			            });
			            
			        };

		    		$("#questions_table tbody tr").click( function( e ) {
		    	        if ( $(this).hasClass('row_selected') ) {
		    	            $(this).removeClass('q_row_selected');
		    	        }
		    	        else {
		    	            oTable.$('tr.q_row_selected').removeClass('row_selected');
		    	            $(this).addClass('row_selected');
		    	            var row = oTable.$('tr.row_selected')[0];
		    	            editQuestion(row);
		    	        }
		    	    }); 		
				}
			}
		}
		
		$("#module_table tbody tr").click( function( e ) {
	        if ( $(this).hasClass('row_selected') ) {
	            $(this).removeClass('row_selected');
	        }
	        else {
	            oTable.$('tr.row_selected').removeClass('row_selected');
	            $(this).addClass('row_selected');
	            var row = oTable.$('tr.row_selected')[0];
	            var mid = $('td:first', row).text();
	            var mname = $('td:eq(1)', row).text();
	            var description  = $('td:eq(2)', row).text();
	            $("#module_name").val(mname);
	            $("#module_description").val(description);
	            $("#module_id").val(mid);	            
	            doAjax("QN_op=load_module&QN_mid=" + mid, onModuleLoad); 	            
	        }
	    });
	    $("#back-button").click(function(){
	    	$("#module_info").hide();
	    	$("#module_views").show();
            	
		});
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
    		<div id="module_views" >    	
    			<h3>Your modules</h3>
				<div id="modules"></div>		
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
	
			<div id ='question_editor'></div>
			<div id='module_form'>
			<form action="./process">
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
