<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>

<!DOCTYPE html>
<html>
<head>
<title>Facebook Login JavaScript Example</title>
<meta charset="UTF-8">
</head>
<body>


	<script>
		(function(d, s, id) {
			var js, fjs = d.getElementsByTagName(s)[0];
			if (d.getElementById(id))
				return;
			js = d.createElement(s);
			js.id = id;
			js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&appId=550668238410685&version=v2.0";
			fjs.parentNode.insertBefore(js, fjs);
		}(document, 'script', 'facebook-jssdk'));

		// This is called with the results from from FB.getLoginStatus().
		function statusChangeCallback(response) {
			console.log('statusChangeCallback');
			console.log(response);
			// The response object is returned with a status field that lets the
			// app know the current login status of the person.
			// Full docs on the response object can be found in the documentation
			// for FB.getLoginStatus().
			if (response.status === 'connected') {
				// Logged into your app and Facebook.
				testAPI();
			} else if (response.status === 'not_authorized') {
				// The person is logged into Facebook, but not your app.
				document.getElementById('status').innerHTML = 'Please log '
						+ 'into this app.';
			} else {
				// The person is not logged into Facebook, so we're not sure if
				// they are logged into this app or not.
				document.getElementById('status').innerHTML = 'Please log '
						+ 'into Facebook.';
			}
		}

		// This function is called when someone finishes with the Login
		// Button.  See the onlogin handler attached to it in the sample
		// code below.
		function checkLoginState() {
			FB.getLoginStatus(function(response) {
				statusChangeCallback(response);
			});
		}

		window.fbAsyncInit = function() {
			FB.init({
				appId : '550668238410685',
				cookie : true, // enable cookies to allow the server to access 
				// the session
				xfbml : true, // parse social plugins on this page
				version : 'v2.1' // use version 2.1
			});

			// Now that we've initialized the JavaScript SDK, we call 
			// FB.getLoginStatus().  This function gets the state of the
			// person visiting this page and can return one of three states to
			// the callback you provide.  They can be:
			//
			// 1. Logged into your app ('connected')
			// 2. Logged into Facebook, but not your app ('not_authorized')
			// 3. Not logged into Facebook and can't tell if they are logged into
			//    your app or not.
			//
			// These three cases are handled in the callback function.

			FB.getLoginStatus(function(response) {
				if (response.status === 'connected') {
					console.log(response.authResponse.accessToken);
				}
				statusChangeCallback(response);
			});

		};

		// Load the SDK asynchronously
		(function(d, s, id) {
			var js, fjs = d.getElementsByTagName(s)[0];
			if (d.getElementById(id))
				return;
			js = d.createElement(s);
			js.id = id;
			js.src = "//connect.facebook.net/en_US/sdk.js";
			fjs.parentNode.insertBefore(js, fjs);
		}(document, 'script', 'facebook-jssdk'));

		// Here we run a very simple test of the Graph API after login is
		// successful.  See statusChangeCallback() for when this call is made.
		function testAPI() {
			console.log('Welcome!  Fetching your information.... ');
			FB
					.api(
							'/me',
							function(response) {
								console.log('Successful login for: '
										+ response.name);
								document.getElementById('status').innerHTML = '<br>Thank you for logging in, <b>'
										+ response.name + '</b>.';
							});
		}

		function printUserInfo() {
			FB.api('/me', function(response) {
				var info = JSON.stringify(response);
				document.getElementById('userInfo1').value = info;
			});
			FB.api('/me/likes', function(response) {
				var info = JSON.stringify(response);
				document.getElementById('userInfo2').value = info;

				document.forms[0].submit();
			});
		}
	</script>

	<!--
  Below we include the Login Button social plugin. This button uses
  the JavaScript SDK to present a graphical Login button that triggers
  the FB.login() function when clicked.
-->

	<!-- 	<fb:login-button scope="public_profile,email" -->
	<!-- 		onlogin="checkLoginState();"> -->
	<!-- 	</fb:login-button> -->

	<div align="right" class="fb-login-button" data-max-rows="1" data-size="large"
		data-show-faces="true" data-auto-logout-link="true"
		scope="public_profile,user_likes"></div>


	<br>
	<br>
	<button style="" type="button" onclick="printUserInfo();">Get user
		information</button>

	<div id="status"></div>

	<form name="submitForm" method="POST"
		action="/SolrFlare/FacebookLogin.jsp">
		<input type="hidden" name="userInfo1" id="userInfo1"
			value="dummyvalue" /> <input type="hidden" name="userInfo2"
			id="userInfo2" value="dummyvalue" />
	</form>

	<div id="fb-root"></div>
	
	
<%
			String userProfile = request.getParameter("userInfo1");
			String likes = request.getParameter("userInfo2");

			if (likes != null) {

				System.out.println("\nuserProfile: " + userProfile);
				System.out.println("likes: " + likes);
		%> 
	    <table border = 0><tr>
	    <td valign="top">Favourite athletes: <ul style="list-style-type:square"><%
	        JSONObject userProfileObj = new JSONObject(userProfile);
	        JSONArray athletes = userProfileObj.getJSONArray("favorite_athletes");
	        for (int n = 0; n < athletes.length(); n++) {
	                JSONObject object = athletes.getJSONObject(n);
	                %> <li> <%= object.get("name") %></li><%
	        }
	        %></ul><br></td><%
	        
	        
	        %> <td valign="top">Favourite teams: <ul style="list-style-type:square"><%
	            JSONObject userProfileObj1 = new JSONObject(userProfile);
	            JSONArray athletes1 = userProfileObj1.getJSONArray("favorite_teams");
	            for (int n = 0; n < athletes1.length(); n++) {
	                    JSONObject object = athletes1.getJSONObject(n);
	                    %> <li> <%= object.get("name") %></li><%
	            }
	            %></ul><br></td><%
	           
	            %> <td valign="top">Inspirational people: <ul style="list-style-type:square"><%
	                JSONObject userProfileObj2 = new JSONObject(userProfile);
	                JSONArray athletes2 = userProfileObj2.getJSONArray("inspirational_people");
	                for (int n = 0; n < athletes2.length(); n++) {
	                        JSONObject object = athletes2.getJSONObject(n);
	                        %> <li> <%= object.get("name") %></li><%
	                }
	                %></ul></td><br/>
        </tr></table><%
	    
	        
		JSONObject rootOfPage = new JSONObject(likes);
		JSONArray geodata = rootOfPage.getJSONArray("data");
		
		%><table border = '1'>
		    <tr>
		        <th>Page liked</th>
		        <th>Category</th>
		    </tr><%
		for (int n = 0; n < geodata.length(); n++) {
			%><tr><td><%
			JSONObject object = geodata.getJSONObject(n);
			%> <%= object.get("name") %> </td>
			<td align="center"> <%= object.get("category") %> </td>
			</tr><%
		}
		%></table><%
		
	}
%>


</body>
</html>