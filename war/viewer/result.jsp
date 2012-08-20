<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>viewer Result</title>
	<link type="text/css" href="../css/cube.css" rel="stylesheet" />	
	<script type="text/javascript" src="../js/jquery.min.js"></script>
	<script type="text/javascript" src="../js/json2.js"></script>
	<script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      //google.setOnLoadCallback(drawChart);
      function drawResults(rows) {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Module');
        data.addColumn('number', 'Correct');
        data.addColumn('number', 'Incorrect');
        data.addColumn('number', 'Attempted');
        data.addRows(rows);

        var options = {
          title: 'Here is how you performed.',
          hAxis: {title: 'Modules', titleTextStyle: {color: 'red'}}
        };

        var chart = new google.visualization.ColumnChart(document.getElementById('chart_result'));        
        chart.draw(data, options);
      }

      function drawAnalysis(rows) {
          var data = new google.visualization.DataTable();
          data.addColumn('string', 'Tag');
          data.addColumn('number', 'Number');          
          data.addRows(rows);

          var options = {
            title: 'Module categories',
            vAxis: {title: 'Tags',  titleTextStyle: {color: 'red'}}
          };

          var chart = new google.visualization.BarChart(document.getElementById('chart_analysis'));
          chart.draw(data, options);
        }
      //onload      
      $(function(){
    	  var loadResponse = function(response) {
				if(response.status == true){
					var qObj = JSON.parse(response.metaData);
					if(qObj.code == 'RESULTS'){
						var rows = new Array();
						rows[0] = new Array();
						rows[0][0] = (qObj.mname);
						rows[0][1] = parseInt((qObj.ncorrect));
						rows[0][2] = parseInt((qObj.nwrong));
						rows[0][3] = parseInt((qObj.nunanswered));
						drawResults(rows);
					} else if (qObj.code = "MODULE_ANALYSIS"){
						var rows = new Array();
						$.each( qObj, function(i, n){
						    //alert( "Name: " + i + ", Value: " + n );
						    
						    if(i.indexOf("tag_") != -1){
							    var row = new Array();
							    row[0] = i.split('_')[1];
							    row[1] = parseInt(n);
							    rows.push(row);							    
							}
						});
						drawAnalysis(rows);
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
			
								
			var dataString = "QN_action=result&QN_mname=<%=request.getAttribute("QN_mname") %>" 
						+"&QN_mid=<%=request.getAttribute("QN_mid") %>";
			doAjax(dataString);

			dataString = "QN_action=moduleanalysis&QN_mname=<%=request.getAttribute("QN_mname") %>"
						+"&QN_mid=<%=request.getAttribute("QN_mid") %>";
			doAjax(dataString);
			
			
      });
    </script>    
</head>
<body>
<h1>Career Vision</h1>
<header>
	<a href="">Demo</a>	
	<a href="../login?op=<%=request.getAttribute("loginop")%>">Log<%=request.getAttribute("loginop") %></a>
</header>
    <div id="chart_result" style="width: 900px; height: 500px;"></div>
    <p></p>
    <div id="chart_analysis" style="width: 900px; height: 500px;"></div>
<footer>
	<a href="">Home</a>	
	<a href="">About Us</a>
</footer>
</body>
</html>
