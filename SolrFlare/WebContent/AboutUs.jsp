<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<title>Preference selection</title>
<meta charset="UTF-8">

<link rel="stylesheet" href="css/boxes.css" type="text/css" />

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

</head>

<body>

	<script type="text/javascript">
		
	</script>

	<div id="fb-root"></div>

	<!-- UI Elements here -->
	<jsp:include page="Headerbar.jsp" />

	<br>
	<br>
	<section class="aboutme">
		<div class="wrap">
    		<h1>About us </h1>
    		<div class="bar"><i></i></div>
    		<p>It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).</p>
    		<p>There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc.</p>
    		<p>Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of "de Finibus Bonorum et Malorum" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, "Lorem ipsum dolor sit amet..", comes from a line in section 1.10.32.</p>
		</div>
	</section>

	<div class="messagetext" id="message"></div>

	<input type="hidden" name="userInfo1" id="userInfo1"
		value="dummyvalue1" />
	<input type="hidden" name="userInfo2" id="userInfo2"
		value="dummyvalue2" />

    <br/><br/>

	<!--Footer bar-->
	<jsp:include page="Footer.html" />

</body>
</html>