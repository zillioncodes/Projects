<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="application/javascript" />

<link rel="stylesheet" href="css/headerstyle.css" type="text/css" />
<link rel="stylesheet" href="css/navbar.css" type="text/css" />

<link
	href="//maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css"
	rel="stylesheet">


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
	<div id="calendar"></div>

	<script type="text/javascript">
		function getResults() {
			var data = "query=" + $("#query").val();
			$.post("/SolrFlare/query", data, function(result) {
				//alert(result);
			});
			window.location = "/SolrFlare/SearchResults.jsp";
		}
		function logout() {
			$.post("/SolrFlare/Logout", function(result) {
				if (result.trim() == "success") {
					window.location = "/SolrFlare/Loginpage.jsp";
				}
			});
		}
	</script>

	<section class="headerbar">
		<div class="logoutButton">
			<img alt="logout" src="images/exit.png" onclick="logout();">
		</div>
		<table class="searchnav_table">
			<tr>
				<td class='first_td' style="padding-bottom: 5px; padding-left: 0px;">
					<!-- Search bar -->

					<ul id="search">
						<div class="container-1">
							<input type="search" id="query" name="query"
								placeholder="Your search query here" autocomplete="off"
								onsearch="getResults();" /> <span class="icon"
								onclick="getResults();"><i class="fa fa-search"></i></span>
						</div>
					</ul>

				</td>

				<td class='center_td' align='center' style="padding-bottom: 25px">
					<!-- Title and subtitle -->
					<div class="headertext">
						<h1>
							<a href="Home.jsp">Solr flare</a>
						</h1>
						<h2>
							<a href="Home.jsp">Personalized news reader</a>
						</h2>
					</div>
				</td>

				<td class='last_td' align='right' valign="bottom"
					style="padding-bottom: 0px">
					<!-- Nav bar -->
					<div id='navbar'>
						<ul>
							<!--  <li class='active'><a href='#'><span>Home</span></a></li> -->
							<li><a href='Home.jsp'><span>Home</span></a></li>
							<li><a href='TopStories.jsp'><span>Top stories</span></a></li>
							<li><a href='SelectPreferences.jsp'><span>Settings</span></a></li>
							<li class='last'><a href='AboutUs.jsp'><span>About
										us</span></a></li>
						</ul>
					</div>
				</td>

			</tr>
		</table>
	</section>

	<div class='bar'>
		<i></i>
	</div>
</body>
</html>