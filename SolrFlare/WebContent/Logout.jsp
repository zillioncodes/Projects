<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<title>Preference selection</title>
<meta charset="UTF-8">

<link rel="stylesheet" href="css/boxes.css" type="text/css" />
<link rel="stylesheet" href="css/glowingtext.css" type="text/css" />

<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.24/jquery-ui.min.js"></script>

<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>

<link href="css/datepicker.css" rel="stylesheet" type="text/css" />

<script>
	$(function() {
		$("#datepicker").datepicker();

		$(".ui-state-default").on("mouseenter", function() {
			$(this).attr('title', 'This is the hover-over text'); // title attribute will be shown during the hover
		});
	});
</script>

</head>

<body>
	<!-- UI Elements here -->
	<jsp:include page="LoginHeader.html" />
	<br>
	<br>
	<section>
		<center><b>You have successfully logged out.</b></center>
	</section>

	<!--Footer bar-->
	<jsp:include page="Footer.html" />

</body>
</html>