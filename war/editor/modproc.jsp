<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>


<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>viewer Index</title>			
		<link type="text/css" href="../css/cube.css" rel="stylesheet" />
		<link type="text/css" href="../css/jquery-ui.custom.css" rel="stylesheet" />
		<link type="text/css" href="../css/jquery.dataTables.css" rel="stylesheet" />	
		<script type="text/javascript" src="../js/jquery.min.js"></script>
		<script type="text/javascript" src="../js/jquery-ui.min.js"></script>
		<script type="text/javascript" src="../js/json2.js"></script>
		<script type="text/javascript" src="../js/jquery.jeditable.min.js"></script>
		<script type="text/javascript" src="../js/jquery.dataTables.min.js"></script>		
		<script type="text/javascript">
		
		var mname = "GK_2012";
		var mid = "901";
				
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
			$("#module_info").accordion({
				fillSpace: true,
				collapsible: true,				
				}).accordion("activate", 1);
			//obtain all questions for the module
			var questions = function(){
				doAjax("QN_op=get_questions&QN_mid" + mid);
			};
			
			//query to obtain all questions as 
			$('#module_questions').html( '<table cellpadding="0" cellspacing="0" border="0" class="display" id="questions_table"></table>' );
			var oTable = $("#questions_table").dataTable( {
				"aaData": questions,
				"aoColumns": [
							{ "sTitle": "Id" },
							{ "sTitle": "Question" },
							{ "sTitle": "Option 1" },
							{ "sTitle": "Option 2" },
							{ "sTitle": "Option 3" },
							{ "sTitle": "Option 4" },							
							{
								"sTitle": "Answer",
								"sClass": "center",
								"fnRender": function(obj) {
									var sReturn = obj.aData[ obj.iDataColumn ];									
									return "<b>" + sReturn + "<b>";
								}
							},
							{ "sTitle": "Answers" },
							{ "sTitle": "Tags" },
							{ "sTitle": "Hints" },
						]
					});
			//make row editable
			/* Apply the jEditable handlers to the table */
		    oTable.$('td').editable( '../process', {
		        "callback": function( sValue, y ) {
		            var aPos = oTable.fnGetPosition( this );
		            oTable.fnUpdate( sValue, aPos[0], aPos[1] );
		        },
		        "submitdata": function ( value, settings ) {
		            return {
		                "row_id": this.parentNode.getAttribute('id'),
		                "column": oTable.fnGetPosition( this )[2],
		                "QN_op" : "update_question",
		                "ajax" : "y"
		            };
		        },
		        "height": "14px",
		        "width": "100%"
		    } );	
		});
		</script>				
</head>
<body>
<h1>take.a.test</h1>
<div id='message_board'>
</div>
<div id='module_info'>
	<h3><a href="#">Description</a></h3>
	<div id='module_description'></div>	
	<h3><a href="#">Questions</a></h3>
	<div id='module_questions'></div>	
</div>
<footer>
	<a href="">Home</a>	
	<a href="">About Us</a>
</footer>
</body>
</html>
