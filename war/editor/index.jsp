<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>

<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<title>Welcome to Quiz Editor</title>
		<link type="text/css" href="../css/cube.css" rel="stylesheet" />
		<link type="text/css" href="../css/jquery-ui.custom.css" rel="stylesheet" />
		<link type="text/css" href="../css/jquery.treeview.css" rel="stylesheet" />	
					 
		<script type="text/javascript" src="../js/jquery.min.js"></script>
		<script type="text/javascript" src="../js/jquery-ui.min.js"></script>
		<script type="text/javascript" src="../js/jquery.treeview.js"></script>
		
		<style>
		
		</style>
		<script type="text/javascript">
		$(function(){
			var modJsons  = new Array();
			<c:forEach var="m" items="${modules}">
			modJson = ${m};
			modJsons.push(modJson);
			</c:forEach>
			 
				 var createModuleHtml = function(module) {
					 var content = "<li id='"
							 + module.mid +"'>" 
							 +"<a href='./process?QN_op=update_mod&QN_mname="+module.mname+"&QN_mid="+ module.mid +"'>" 
							 + module.mname + " (Q:"+ module.questionCount + ", D:"+ module.cloneCount + ")</a><ul/></li>";					 
					 return content;
					 
				 };
				 //for each possible state type create a tab
				 //should be replaced with ajax
				 var states = new Array();
				 states.push("draft");
				 states.push("inreview");
				 states.push("suggested");
				 states.push("rejected");
				 states.push("accepted");				 
				 states.push("published");
				 $.each(states, function(index, state){
					 $("#tabs > ul").append("<li><a href='#tabs-"+ (index + 1) + "'> " 
							 + state + "(0)</a></li>");
					 $("#tabs").append("<div id='tabs-" + 
							 + (index + 1) +"'>"
							 + "<ul id='"+state+"_root_modules'></ul>"
							 + "<ul id='"+state+"_temp_modules'></ul>" 
							 + "</div>");					 				     					 
				 });
	
				 var options = $(".options");				 
				 $.each(modJsons, function(index, module){		 					 
					 console.log(">>> Start module:" + module.mname);
					 if(!module.parent){
						 //its a root node
						 console.log("root node:" + module.mname);
						 $("#" + module.state + "_root_modules").append(createModuleHtml(module));	
						 //update the count on the tab
						 var index = $.inArray(module.state, states);
						 if(index != -1){
							 var $value = $("a[href='#tabs-" + (index+1) + "']").html();
							 var num = ($value.match(/.*([0-9]+)/)[1]);
							 num++;
							 $("a[href='#tabs-" + (index+1) + "']").html(module.state + "(" + num +")");
							 						 
						 }				 						 
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
								 $("#" + module.state + "_temp_roots").append(createModuleHtml(module));								 
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
			
						
			$('#module-form').hide();		
			$('#create-button').click( function() {
				$('#module-form').show();	
			});
			$("#tabs").tabs();
			$.each(states, function(index, state){
				 $("#"+state+"_root_modules").treeview({
					 	persist: "location",
						collapsed: true,
						unique: true
				 });
				 				 			 				     					 
			});
		});
		</script>
</head>
<body>
<h1>take.a.test</h1>
<jsp:include page="../common/header.jsp" />
<ul class="toolbar" id="tools">  	
</ul>

<p>Welcome to the quiz editor. </p>
<p>These are your modules:</p>



<div id="tabs">	
	<ul>
	</ul>
</div>

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
