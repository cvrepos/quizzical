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
				 var createModuleHtml = function(module) {
					 var content = "<li id='"
							 + module.mid +"'>" 
							 +"<a href='./process?QN_op=update_mod&QN_mname="+module.mname+"&QN_mid="+ module.mid +"'>" 
							 + module.mname + " (Q:"+ module.questionCount + ", D:"+ module.cloneCount + ")</a><ul/></li>";					 
					 return content;
					 
				 };
				 var options = $(".options");
				 $.each(modJsons, function(index, module){		 					 
					 console.log(">>> Start module:" + module.mname);
					 if(!module.parent){
						 //its a root node
						 console.log("root node:" + module.mname);
						 $("#root_modules").append(createModuleHtml(module));						 						 
					 }					 
					 else{
						 //non-root node
						 console.log("non-root node:" + module.mname);
						 if(!module.prevSibling){
							 console.log("No prevSibling of:" + module.mname);
							 //check if parent ul is already created
							 if($("#" + module.parent).length <= 0){
								 //add parent to temporary roots
								 console.log("Parent module :" + module.parent + " has not been added.");
								 $("#temp_roots").append(createModuleHtml(module));								 
							 }
							 //check if its in the temp roots - delete it if exists
							 if($("#" + module.mid).parent().attr("id") == 'temp_root'){
								 console.log("Removing module:" + module.mname + " from temp root");
								 $("#" + module.mid).remove();
							 }	 							 
							 if($("#" + module.parent +" >ul").length >0 ){
								 console.log("Found a parent module. Adding to it");
							 	 $("#" + module.parent +" >ul").prepend( createModuleHtml(module));
							 }else{
								 console.log("Unable to find parent:" + module.parent);
							 }
						 }else{
							 console.log("PrevSibling is :" + module.prevSibling);
							 $(createModuleHtml(module)).insertAfter($("#"+module.prevSibling));
						 }
					 }
					 //fix the first child if it is not already in correct place
					 if($("#" + module.firstChild).parent().attr("id") == 'temp_root'){
						 //val $nodeContent = $("#" + module.firstChild).html();
						 console.log("Fixing firstChild :" + module.firstChild);
						 $('#' + module.mname + " >ul").append( $("#" + module.firstChild).html());
					 }
					 
					 options.append($("<option />").val(module.mid).text(module.mname));
					 console.log("<<< End module:" + module.mname);
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
<ul id='root_modules'>
</ul>

<ul id='temp_modules'>
</ul>


<ul>
	<li><input type="button" id='create-button' value='Create Module'/></li>
	<li><input type="button" id='update-button' value='Update Module'/></li>	
</ul>

<div id = 'module-form'>
 	<form  action="./process">
 		<label for='QN_module'>Enter the module name:</label>
 		<input type='text' name='QN_module'></input><br/>
 		<label for='QN_description'>Briefly describe the module:</label>
 		<textarea name='QN_description'></textarea><br/>
 		<label for='QN_parent'>Select a parent module:</label>
 		<select name='QN_parent' class='options'>
 			<option value='none'>None</option>
 		</select>
 		<label for='QN_prevSibling'>Or select a previous sibling module:</label>
 		<select name='QN_prevSibling' class='options'>
 			<option value='none'>None</option>
 		</select>
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
