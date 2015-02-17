<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<title>Welcome to Solr flare</title>
<meta charset="UTF-8">

<link rel="stylesheet" href="css/headerstyle.css" type="text/css" />
<link rel="stylesheet" href="css/navbar.css" type="text/css" />
<link rel="stylesheet" href="css/bodystyle.css" type="text/css" />

<script src="js/jquery-1.11.1.js"></script>
</head>

<body>
	<div id="fb-root"></div>
	<script>
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
// 				testAPI();
// 				fb_login();
			} else if (response.status === 'not_authorized') {
				// The person is logged into Facebook, but not your app.
				// document.getElementById("message").innerHTML = "Hello, please authorize our Facebook app to help us understand your interests better.";
			} else {
				// The person is not logged into Facebook, so we're not sure if
				// they are logged into this app or not.
                // document.getElementById("message").innerHTML = "Hello, please login to our Facebook app to help us understand your interests better.";
			}
		}

		// This function is called when someone finishes with the Login
		// Button.  See the onlogin handler attached to it in the sample
		// code below.
		function checkLoginState() {
			FB.getLoginStatus(function(response) {
				statusChangeCallback(response);
				if (response.status == 'connected') {
	                document.getElementById("message").innerHTML = "Welcome. You are connected.";

                    storeUserInterests();
                    doesFbUserExist();
				}
			});
		}

		window.fbAsyncInit = function() {
			FB.init({
				appId : '550668238410685',
				cookie : true, // enable cookies to allow the server to access the session
				status : true, // check login status
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

		var user_id, user_name, user_email;
		function fb_login() {
			FB
					.login(
							function(response) {

								if (response.authResponse) {
									console
											.log('Welcome. Fetching your information.... ');
									//console.log(response); // dump complete info
									access_token = response.authResponse.accessToken;
									//get access token
									user_id = response.authResponse.userID;
									//get FB UID

									FB.api('/me', function(response) {
										user_name = response.name;
										user_email = response.email;
										
// 										storeUserInterests();
// 						                doesFbUserExist(user_id, user_name, user_email);
									});

									checkLoginState();
									
								} else {
									//user hit cancel button
									console.log('User cancelled login or did not fully authorize.');

								}
							}, {
								scope : 'publish_stream,email'
							});
		}
		(function() {
			var e = document.createElement('script');
			e.src = document.location.protocol
					+ '//connect.facebook.net/en_US/all.js';
			e.async = true;
			document.getElementById('fb-root').appendChild(e);
		}());

		// Here we run a very simple test of the Graph API after login is
		// successful.  See statusChangeCallback() for when this call is made.
// 		function testAPI() {
// 			console.log('Welcome. Fetching your information.. ');
// 			FB.api('/me', function(response) {
// 				console.log('Successful login for: ' + response.name
// 						+ ", email: " + response.email);
// 			});
// 		}

        /* Does FB user already exist */
        function doesFbUserExist() {
            var data = "username=" + user_id;

            /* Get user preferences */
            getUserPreferences(username);
            
            $.post("/SolrFlare/DoesUserExist", data, function(result) {
                if (result == "yes") {

                    /* Call clusterer */
                    callClusterer();
                    
                    window.location = "/SolrFlare/Home.jsp";
                } else {
                    createFacebookUserInDb(user_id, user_name, user_email);
                }
            });
        }
		
        function createFacebookUserInDb(username, name, email) {
            var password = "nopassword";
            var data = "name=" + name + "&username=" + username + "&email=" + email + "&password=" + password + "&isfacebookuser=true";

            $.post("/SolrFlare/CreateUserInDB", data, function(result) {
                  if (result.trim() == "success") {
                      document.getElementById("message").innerHTML = "Your account has been created.";
                      storeUserInterests();

                      /* Call clusterer */
                      callClusterer();
                      
                      window.location = "/SolrFlare/SelectPreferences.jsp";
                  } else {
                      document.getElementById("message").innerHTML = "Your account could not be created: " + result;
                  }
            });
        }
        
		function login_normal_user() {
			var username = document.getElementById("username").value;
			var password = document.getElementById("password").value;
            
			if (username.trim() == "") {
                document.getElementById("message").innerHTML = "Please enter a username";
			} else if (password.trim() == "") {
                document.getElementById("message").innerHTML = "Please enter a password";
            } else {
            	authenticateUser(username, password);
            }
		}
		
		/* Does user already exist */
        function authenticateUser(username, password) {
            var data = "username=" + username + "&password=" + password;

            $.post("/SolrFlare/AuthenticateUser", data, function(result) {
                if (result.trim() == "success") {
                	document.getElementById("message").innerHTML = "Welcome " + username + ". You are connected.";
                	
                	/* Get user preferences */
                	getUserPreferences(username);
                	
                	/* Call clusterer */
                	callClusterer();
                	
                	window.location = "/SolrFlare/Home.jsp";
                } else {
                	document.getElementById("message").innerHTML = result;
                }
            });
        }
		
        function getUserPreferences(username) {
            var data = "username=" + username;

            $.post("/SolrFlare/GetUserPreferencesServlet", data, function(result) {
                console.log("Got user preferences into session.");
            });
        }
        
        function callClusterer(username) {
            var data = "";
            
            $.post("/SolrFlare/ClustererServlet", data, function(result) {
            	if (result.trim() == "success")
            	    console.log("Clustering done.");
            	else 
            		console.log("Clustering failed.");
            });
        }

		function fb_login_action() {
			fb_login();
// 			checkLoginState();
		}

		var userInfo1 = "nodata";
		var userInfo2 = "nodata";
		var img = "";
		function storeUserInterests() {
			FB.api('/me', function(response) {
				var info = JSON.stringify(response);
				document.getElementById('userInfo1').value = info;
				userInfo1 = info;
				putInfoInDatabase();
			});
			FB.api('/me/likes', function(response) {
				var info = JSON.stringify(response);
				document.getElementById('userInfo2').value = info;
				userInfo2 = escape(info);
			});
			FB.api('/me?fields=picture', function(response) {
				var info = JSON.stringify(response);
				img = info;
			});
		}

		/* Send AJAX request */
		function putInfoInDatabase() {
			var data = "about=" + userInfo1 + "&likes=" + userInfo2 + "&img=" + img;

			$.post("/SolrFlare/FacebookDataToDB", data, function(result) {
// 				alert(result);
			});
		}
	</script>

	<!-- UI Elements here -->
	<jsp:include page="Headerbar.jsp" />

	<br>
	<br>
	<section class="loginpanel">
		<div class="wrap">
			<div align="center">
				<input type="image" id="facebooklogin" onclick="fb_login_action();"
					src="images/fbLogin3.png" alt="Login with facebook" width="200"
					height="45" />
			</div>
			<br />
			<div align="center">Or</div>
			<br /> <input id = "username" class="top-field" type="text" placeholder="username"
				required> <input id = "password" class="bottom-field" type="password"
				placeholder="password" required>
			<!--         <a href="" class="forgot_link">forgot ?</a> -->
			<br />
			<button onclick="login_normal_user();">Sign in</button>
			<br> <br>
			<button id='darkbutton' onclick="window.location.href='NewUser.jsp'">New
				user? Sign-up here</button>
		</div>
	</section>

	<div class="messagetext" id="message"></div>

	<input type="hidden" name="userInfo1" id="userInfo1"
		value="dummyvalue1" />
	<input type="hidden" name="userInfo2" id="userInfo2"
		value="dummyvalue2" />

	<script src="js/index.js"></script>

	<!--Footer bar-->
	<jsp:include page="Footer.html" />

</body>
</html>