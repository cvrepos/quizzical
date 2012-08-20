<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>


<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>viewer Index</title>					
		<link type="text/css" href="../css/custom.css" rel="stylesheet" />
		<script type="text/javascript" src="../js/jquery.min.js"></script>
		<script type="text/javascript" src="../js/json2.js"></script>
		<script type="text/javascript">
		<% if (request.getAttribute("QN_mname") != null && request.getAttribute("QN_modid") != null ){ 
		%>
		var mname = "<%= request.getAttribute("QN_mname") %>";
		var mid = "<%= request.getAttribute("QN_modid") %>";
		<%
		}
		%>
		$(function() {
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

		    $("#message_board").html("<p>Congratulations! The module <i>" + mname 
				    + "</i> is loaded. You can now start adding quizes to it.");

		    // install handlers for the submit and preview buttons
		    $("#submit-button").click(function(){		                         
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
		            question.tags.push(mid); 
		            dataString += 'QN_op=add&'; 
		            dataString += 'mid=' + mid + '&'; 
		            dataString += 'ajax=y&'; 		             
		            dataString += 'question=' + JSON.stringify(question, null, 2);  
		            doAjax(dataString);
		            
		            
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


			<div id='question_container' >
	
            <fieldset> 
              <span class='formentry'> 
              <label for='question' style='display:block'>Question<font color='red'>*</font>:</label>
               <textarea name='question' id='question' class='required' title='Which of the following planets has its moons named after characters from the works of William Shakespeare?'></textarea> 
              </span> 
            
             <span class='formentry'> 
               <label for='answer1' style='display:block' >Option 1<font color='red'>*</font>:</label>             
               <textarea name='answer1' id='answer1' style='display:block' class='required' title='Mars' x-webkit-speech></textarea>
                Is the above option correct?<input type='checkbox' id='isCorrect1' name='isCorrect1' ></input> 
              </span> 
            
              <span class='formentry'> 
               <label for='answer2' style='display:block' >Option 2:</label>             
               <textarea name='answer2' id='answer2' style='display:block' title='Uranus' ></textarea>
                Is the above option correct?<input type='checkbox' id='isCorrect2'  name='isCorrect2' ></input> 
              </span> 
            
              <span class='formentry'> 
               <label for='answer3' style='display:block' >Option 3:</label>             
               <textarea name='answer3' id='answer3' style='display:block' title='Venus'></textarea>
                Is the above option correct?<input type='checkbox' id='isCorrect3'  name='isCorrect3' ></input> 
              </span> 
            
              <span class='formentry'> 
               <label for='answer4' style='display:block' >Option 4:</label>             
               <textarea name='answer4'  id='answer4' style='display:block' title='Jupitar'></textarea>
                Is the above option correct?<input type='checkbox' id='isCorrect4'  name='isCorrect4' ></input> 
              </span> 
            
              <span class='formentry'> 
               <label for='tags' >Tags:</label>             
               <textarea name='tags' id='tags' style='display:block' title='Solar System, Moons, Astronomy'></textarea>
              </span> 
            
               <input type='hidden' name='type' id='type' value='objective'/>  
            
              <span class='formentry'> 
               <label for='hints' >Hints:</label>             
               <textarea name='hints' id='hints' style='display:block' title='Miranda,Titania are two of the moons of this planet.'></textarea> 
              </span> 
               <input type='button' id='submit-button' value='submit'></input> 
               <input type='button' class='preview-button' name='preview' value='preview'></input>
            </fieldset> 
       </div>
       	</div>  
       <jsp:include page="../common/footer.jsp" />
</div>
</body>
</html>
