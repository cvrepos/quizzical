<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>

<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<title>Welcome to Quiz Editor</title>
		<link type="text/css" href="../css/jquery-ui.custom.css" rel="stylesheet" />	
		<script type="text/javascript" src="../js/jquery.min.js"></script>
		<script type="text/javascript" src="../js/jquery.custom.min.js"></script>
		
		<script type="text/javascript" src="../js/jquery.validate.js"></script>
		<script type="text/javascript" src="../js/jquery.example.js"></script>
		<script type="text/javascript">

		var tagLoader = function(){
			$('#spinner').show();
			var dataString = "op=gettags";
			$.ajax({
			type: "POST",
			url: "process",
			data: dataString,
			dataType: 'html',
			success: function(response) {	
				    $('#tag-cloud').addClass('fixedtags');						
					$('#tag-cloud').html(response);
					$('.tag-count').hide();
					//add event listeners 
					$('.tag-name').click(function(){
						var vals = $('#tags').val();									
						if(vals){
							vals += "," + this.innerText;										
						}else{
							vals = this.innerText;
						}									
						$('#tags').val(vals);
					});
					
			},
			error : function(){
				alert("Error loading tags");
			}
		  });
        };
        var invokeDelete = function(key) {
        	$('#spinner').show();
			var dataString = "op=delete&key="+key;
			alert(dataString);
			$.ajax({
			type: "POST",
			url: "process",
			data: dataString,			
			success: function(response) {	
				    if(response.status){
						$('#form'+key).hide();						
					}else{
						alert("Unable to delete");
					}
				    
			},
			error : function(){
				alert("Error loading tags");
			}
		  }); 
        };
        var invokeUpdate = function(key, data) {
        	$('#spinner').show();
			var dataString = data;			
			$.ajax({
			type: "POST",
			url: "process",
			data: dataString,			
			success: function(response) {	
				    if(response.status){
						$('#form'+key).addClass("ui-state-highlight");					
					}else{
						alert("Unable to update");
					}
				    
			},
			error : function(){
				alert("Error in update");
			}
		  }); 
        };

        var quiztypes  = new Array();
		<c:forEach var="e" items="${quiztypes}">
     		quiztype = new Object();
     		quiztype.name = "${f:h(e.name)}";
     		quiztype.html = "${e.html}";
     		quiztype.previewtemplate = "${e.previewTemplate}";
     		quiztype.previewfunction = ${e.previewFunction};
     		quiztype.submitprocessorfunction = ${e.submitProcessorFunction};
     		quiztypes.push(quiztype);
      </c:forEach>
        
		    //create a dragable object which when dropped to a droppable panel will cause a 
		    //form to be created
	  $(function(){
		  
		  tagLoader();
		//clickHandler for search
		  $("#search-button").click(function(){
			  $('#spinner').show();
				var dataString = "op=search&update=yes&val=" +$("#search-text").val();
				$.ajax({
					type: "POST",
					url: "process",
					data: dataString,
					dataType: 'html',
					success: function(response) {
						$('#results').html(response);	
						$.each(quiztypes, function(index, val){
					         $(".preview-button."+val.name).click(function(event){
						         //find out the type associated with the button
						         $("#preview").html(val.previewtemplate);						         
						         val.previewfunction(event,$(this).attr('id'));
						         
						     });
					         $(".delete-button."+val.name).click(function(event){
						         invokeDelete($(this).attr('id'));
						         
						     });
					         $(".update-button."+val.name).click(function(event){
						         var key = $(this).attr('id');
					        	 var data = val.submitprocessorfunction("update", key);
					        	 alert(data);
						         invokeUpdate(key, data);
						         
						     });
						     
					    });
												
					},
					error :function(){
						alert("Error searching.")
					}
			  });
		  });
		  
	  });
 	</script>
		<style type="text/css">	
			body {
    			padding-left: 11em;
    			font-family: Georgia, "Times New Roman",
          				Times, serif;
    			color: purple;
    			background-color: #d8da3d 
    			}
    		.fixedtags {    			    			
    			position: fixed;
    			top: 15em;
    			left: 1em;
    			width: 9em;
    			background: white;
    			margin: 0.5em 0;
    			padding: 0.3em 
    			}
    		.tag-name:hover{
    			color: green;
    			background-color:black
    		}			
    		ul.toolbar {
    			list-style-type: none;
    			padding: 0;
    			margin: 0;
    			position: fixed;
    			top: 2em;
    			left: 1em;
    			width: 9em 
    			}
    		ul.toolbar li {
    			background: white;
    			margin: 0.5em 0;
    			padding: 0.3em;
    			border-right: 1em solid black 
    			}
    			fieldset
				{
					border: 1px solid #781351;
					width: 30em
				}

				legend
				{
					color: #fff;
					background: #ffa20c;
					border: 1px solid #781351;
					padding: 2px 6px
				}

	
    		.preview_question{ display:block; }
    		.preview_question ul{list-style-type: none;}
    		.preview_question li{padding: 0.3em;}
    			    								
            #droppable { min-width: 20em; min-height: 20em; padding: 0.5em; float: left; margin: 1em; border-style: dotted; position: relative}
            .formentry { display:block; padding: 0.5em} 
            textarea {resize:vertical; max-height:10em; min-width:30em;}
            #preview { max-width: 30em; min-width: 20em; min-height: 20em; padding: 0.5em; float: left; margin: 1em; border-style: none; position: relative}
            #questionform label.error { display:block; color:red}
            
            .spinner {
				position: fixed;
				top: 50%;
				left: 50%;
				margin-left: -50px; /* half width of the spinner gif */
				margin-top: -50px; /* half height of the spinner gif */
				text-align:center;
				z-index:1234;
				overflow: auto;
				width: 100px; /* width of the spinner gif */
				height: 102px; /*hight of the spinner gif +2px to fix IE8 issue */
			}
		</style>	
	</head>

<body>

<ul class="toolbar" id="tools">  	
</ul>

<p>Welcome to the quiz editor. Please drag and drop the question types to start adding quizes.
</p>
<div id='search-box'>
	<label for='search-text'>Search:</label>
	<input type='text' id='search-text'></input>
	<input type='button' id='search-button' value='search'></input>
</div>
<div id='results'>
</div>

<div id="preview">
</div>

<div id="tag-cloud">
</div>


<div id="spinner" class="spinner" style="display:none;">
	<img id="img-spinner" src="../css/images/ajax-loader.gif" alt="Loading"/>
</div>


</body>
</html>
