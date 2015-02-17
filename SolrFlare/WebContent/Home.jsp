<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<title>Preference selection</title>
<meta charset="UTF-8">

<link rel="stylesheet" href="css/homeboxes.css" type="text/css" />

<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.24/jquery-ui.min.js"></script>
<link rel="stylesheet"
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.24/themes/smoothness/jquery-ui.css">
<script src="nytimes/js/topstories.js"></script>
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

<script type="text/javascript">
	$(document).ready(function() {

		var loggedIn =
<%=session.getAttribute("isLoggedIn")%>
	;
		if (loggedIn == null) {
			window.location = "/SolrFlare/Loginpage.jsp";
		}

		var data = "category='Science and Technology'";
		$.post("/SolrFlare/GetHomeArticles", data, function(result) {
			if (result.trim() != "")
			 $("#science_article").html(result);
		});

		data = "category='Lifestyle'";
		$.post("/SolrFlare/GetHomeArticles", data, function(result) {
			if (result.trim() != "")
				$("#lifestyle_article").html(result);
		});

		data = "category='Entertainment'";
		$.post("/SolrFlare/GetHomeArticles", data, function(result) {
			if (result.trim() != "")
				$("#entertainment_article").html(result);
		});

		data = "category='Travel'";
		$.post("/SolrFlare/GetHomeArticles", data, function(result) {
			if (result.trim() != "")
				$("#travel_article").html(result);
		});

		data = "category='Business'";
		$.post("/SolrFlare/GetHomeArticles", data, function(result) {
			if (result.trim() != "")
				$("#business_article").html(result);
		});
		var data = "category='Automobiles'";
		$.post("/SolrFlare/GetHomeArticles", data, function(result) {
			if (result.trim() != "")
				$("#automobiles_article").html(result);
		});
		var data = "category='World'";
		$.post("/SolrFlare/GetHomeArticles", data, function(result) {
			if (result.trim() != "")
				$("#world_article").html(result);
		});
		var data = "category='Miscellaneous'";
		$.post("/SolrFlare/GetHomeArticles", data, function(result) {
			if (result.trim() != "")
				$("#misc_article").html(result);
		});
	});
</script>
</head>

<body>


	<div id="fb-root"></div>

	<!-- UI Elements here -->
	<jsp:include page="Headerbar.jsp" />

	<br>
	<br>
	<section class="homestories">
		<div class="wrap">
			<h1>Recommended stories</h1>
			<table class="story_table" border="1">

				<!-- 1st Row -->
				<tr>
					<!-- Science & technology -->
					<td class="card">
						<table class="card_table">
							<tr>
								<td class="img_and_title"><img
									src="images/icons/science.png"
									style="width: 50px; height: 50px;" /><br> <span
									class="card_title">Science and technology</span></td>
								<td class="card_articles">
									<div class="articletext" id="science_article">Dummy text: It is a
										long established fact that a reader will be distracted by the
										readable content of a page when looking at its layout. The
										point of using Lorem Ipsum is that it has a more-or-less
										normal distribution of letters, as opposed to using 'Content
										here, content here', making it look like readable English. It
										is a long established fact that a reader will be distracted by
										the readable content of a page when looking at its layout. The
										point of using Lorem Ipsum is that it has a more-or-less
										normal distribution of letters, as opposed to using 'Content
										here, content here', making it look like readable English.</div>
								</td>
							</tr>
						</table>
					</td>

					<!-- Lifestyle -->
					<td class="card">
						<table class="card_table">
							<tr>
								<td class="img_and_title"><img
									src="images/icons/lifestyle.png"
									style="width: 50px; height: 50px;" /><br> <span
									class="card_title">Lifestyle</span></td>
								<td class="card_articles">
									<div class="articletext" id="lifestyle_article">Dummy text: It is a
										long established fact that a reader will be distracted by the
										readable content of a page when looking at its layout. The
										point of using Lorem Ipsum is that it has a more-or-less
										normal distribution of letters, as opposed to using 'Content
										here, content here', making it look like readable English. It
										is a long established fact that a reader will be distracted by
										the readable content of a page when looking at its layout. The
										point of using Lorem Ipsum is that it has a more-or-less
										normal distribution of letters, as opposed to using 'Content
										here, content here', making it look like readable English.</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>

				<!-- 2nd Row -->
				<tr>
					<!-- Entertainment -->
					<td class="card">
						<table class="card_table">
							<tr>
								<td class="img_and_title"><img
									src="images/icons/entertainment.png"
									style="width: 50px; height: 50px;" /><br> <span
									class="card_title">Entertainment</span></td>
								<td class="card_articles">
									<div class="articletext" id="entertainment_article">It is
										a long established fact that a reader will be distracted by
										the readable content of a page when looking at its layout. The
										point of using Lorem Ipsum is that it has a more-or-less
										normal distribution of letters, as opposed to using 'Content
										here, content here', making it look like readable English. It
										is a long established fact that a reader will be distracted by
										the readable content of a page when looking at its layout. The
										point of using Lorem Ipsum is that it has a more-or-less
										normal distribution of letters, as opposed to using 'Content
										here, content here', making it look like readable English.</div>
								</td>
							</tr>
						</table>
					</td>

					<!-- Travel -->
					<td class="card">
						<table class="card_table">
							<tr>
								<td class="img_and_title"><img
									src="images/icons/travel.png"
									style="width: 50px; height: 50px;" /><br> <span
									class="card_title">Travel</span></td>
								<td class="card_articles">
									<div class="articletext" id="travel_article">Dummy text: It is a long
										established fact that a reader will be distracted by the
										readable content of a page when looking at its layout. The
										point of using Lorem Ipsum is that it has a more-or-less
										normal distribution of letters, as opposed to using 'Content
										here, content here', making it look like readable English. It
										is a long established fact that a reader will be distracted by
										the readable content of a page when looking at its layout. The
										point of using Lorem Ipsum is that it has a more-or-less
										normal distribution of letters, as opposed to using 'Content
										here, content here', making it look like readable English.</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>

				<!-- 3rd Row -->
				<tr>
					<!-- Business -->
					<td class="card">
						<table class="card_table">
							<tr>
								<td class="img_and_title"><img
									src="images/icons/business.png"
									style="width: 50px; height: 50px;" /><br> <span
									class="card_title">Business</span></td>
								<td class="card_articles">
									<div class="articletext" id="business_article">Dummy text: It is a
										long established fact that a reader will be distracted by the
										readable content of a page when looking at its layout. The
										point of using Lorem Ipsum is that it has a more-or-less
										normal distribution of letters, as opposed to using 'Content
										here, content here', making it look like readable English. It
										is a long established fact that a reader will be distracted by
										the readable content of a page when looking at its layout. The
										point of using Lorem Ipsum is that it has a more-or-less
										normal distribution of letters, as opposed to using 'Content
										here, content here', making it look like readable English.</div>
								</td>
							</tr>
						</table>
					</td>

					<!-- Automobiles -->
					<td class="card">
						<table class="card_table">
							<tr>
								<td class="img_and_title"><img
									src="images/icons/automobiles.png"
									style="width: 50px; height: 50px;" /><br> <span
									class="card_title">Automobiles</span></td>
								<td class="card_articles">
									<div class="articletext" id="automobiles_article">Dummy text: It is a
										long established fact that a reader will be distracted by the
										readable content of a page when looking at its layout. The
										point of using Lorem Ipsum is that it has a more-or-less
										normal distribution of letters, as opposed to using 'Content
										here, content here', making it look like readable English. It
										is a long established fact that a reader will be distracted by
										the readable content of a page when looking at its layout. The
										point of using Lorem Ipsum is that it has a more-or-less
										normal distribution of letters, as opposed to using 'Content
										here, content here', making it look like readable English.</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>

				<!-- 4th Row -->
				<tr>

					<!-- World news -->
					<td class="card">
						<table class="card_table">
							<tr>
								<td class="img_and_title"><img src="images/icons/world.png"
									style="width: 50px; height: 50px;" /><br> <span
									class="card_title">World news</span></td>
								<td class="card_articles">
									<div class="articletext" id="world_article">Dummy text: It is a long
										established fact that a reader will be distracted by the
										readable content of a page when looking at its layout. The
										point of using Lorem Ipsum is that it has a more-or-less
										normal distribution of letters, as opposed to using 'Content
										here, content here', making it look like readable English. It
										is a long established fact that a reader will be distracted by
										the readable content of a page when looking at its layout. The
										point of using Lorem Ipsum is that it has a more-or-less
										normal distribution of letters, as opposed to using 'Content
										here, content here', making it look like readable English.</div>
								</td>
							</tr>
						</table>
					</td>

					<!-- Misc -->
					<td class="card">
						<table class="card_table">
							<tr>
								<td class="img_and_title"><img src="images/icons/misc.png"
									style="width: 50px; height: 50px;" /><br> <span
									class="card_title">Miscellaneous</span></td>
								<td class="card_articles">
									<div class="articletext" id="misc_article">Dummy text: It is a long
										established fact that a reader will be distracted by the
										readable content of a page when looking at its layout. The
										point of using Lorem Ipsum is that it has a more-or-less
										normal distribution of letters, as opposed to using 'Content
										here, content here', making it look like readable English. It
										is a long established fact that a reader will be distracted by
										the readable content of a page when looking at its layout. The
										point of using Lorem Ipsum is that it has a more-or-less
										normal distribution of letters, as opposed to using 'Content
										here, content here', making it look like readable English.</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
	</section>

	<div class="messagetext" id="message"></div>

	<input type="hidden" name="userInfo1" id="userInfo1"
		value="dummyvalue1" />
	<input type="hidden" name="userInfo2" id="userInfo2"
		value="dummyvalue2" />

	<br />
	<br />

	<!--Footer bar-->
	<jsp:include page="Footer.html" />

</body>
</html>