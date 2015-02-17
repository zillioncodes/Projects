<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<title>Article Viewer</title>
<meta charset="UTF-8">

<link rel="stylesheet" href="css/boxes.css" type="text/css" />
<link rel="stylesheet" href="css/glowingtext.css" type="text/css" />

<script
    src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<script
    src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.24/jquery-ui.min.js"></script>
<link rel="stylesheet"
    href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.24/themes/smoothness/jquery-ui.css">

<script src="nytimes/js/searchresults.js"></script>
<script src="nytimes/widgets/core/Core.js"></script>
<script src="nytimes/widgets/core/AbstractManager.js"></script>
<script src="nytimes/widgets/managers/Manager.jquery.js"></script>
<script src="nytimes/widgets/core/Parameter.js"></script>
<script src="nytimes/widgets/core/ParameterStore.js"></script>
<script src="nytimes/widgets/core/AbstractWidget.js"></script>
<script src="nytimes/widgets/ResultWidget.js"></script>
<script src="nytimes/widgets/jquery/PagerWidget.js"></script>
<script src="nytimes/widgets/core/AbstractFacetWidget.js"></script>
<script src="nytimes/widgets/TagcloudWidget.js"></script>
<script src="nytimes/widgets/CurrentSearchWidget.9.js"></script>
<script src="nytimes/widgets/core/AbstractTextWidget.js"></script>
<script src="nytimes/widgets/AutocompleteWidget.js"></script>
<script src="nytimes/widgets/CountryCodeWidget.js"></script>
<script src="nytimes/widgets/CalendarWidget.js"></script>

<link href="css/datepicker.css" rel="stylesheet" type="text/css" />

<script>
	$(document).ready(function() {
		var loggedIn = <%=session.getAttribute("isLoggedIn")%>;	
		if (loggedIn == null) {
			window.location = "/SolrFlare/Loginpage.jsp";
		} else {
			$.get("/SolrFlare/loadNewsDescription", function(result) {
	            $("#docDesc").html(result);
	    	});
			$.get("/SolrFlare/MoreLikeThis", function(result) {
	           $("#more").html(result);
	    	});
		}
	});
	function moreClick(){
		var $this = $(this), div = $this.closest('div');
		var data = "description="+div.html();
		$.post("/SolrFlare/loadNewsDescription", data, function(result) {
            if (result.trim() == "success") {
            	window.location = "/SolrFlare/NewsDescription.jsp";
            } 
        });
		$.get("/SolrFlare/loadNewsDescription", function(result) {
            $("#docDesc").html(result);
    	});
		$.get("/SolrFlare/MoreLikeThis", function(result) {
           $("#more").html(result);
    	});
	}
</script>

</head>

<body>

	<!-- UI Elements here -->
	<jsp:include page="Headerbar.jsp" />

	<br>
	<br>
	<section class="resultbox">
		<div id="result">
			<div id="docDesc"></div>
			Was this helpful? &nbsp;<span class="like"><img alt="logout" src="images/like.png"></span>
&nbsp;&nbsp;<span class="dislike"><img alt="logout" src="images/dislike.png"></span>
		</div>
	</section>
	<section class="morelikebox">
		<b><center>More like this</center></b>
		<div id="more"></div>
	</section>
	<!--Footer bar-->
	<jsp:include page="Footer.html" />
	<input type="hidden" id="querytext" name="querytext">
</body>
</html>