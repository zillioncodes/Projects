<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<title>Registration</title>
<meta charset="UTF-8">

<link rel="stylesheet" href="css/headerstyle.css" type="text/css" />
<link rel="stylesheet" href="css/navbar.css" type="text/css" />
<link rel="stylesheet" href="css/bodystyle.css" type="text/css" />
<link
    href="//maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css"
    rel="stylesheet">

<script src="js/jquery-1.11.1.js"></script>
</head>

<body>

    <script type="text/javascript">
    function makeUser() {
    	if (validate()) {
            var username = document.getElementById("username").value;
    		doesUserExist();    		
    	}
    }
    
    function validate() {
    	var success = false;
        var name = document.getElementById("name").value;
        var username = document.getElementById("username").value;
        var email = document.getElementById("email").value;
    	var pass1 = document.getElementById("password").value;
        var pass2 = document.getElementById("reenter").value;
        
        if (name.trim() == "") {
            document.getElementById("message").innerHTML = "Please enter a name.";
        } else if (username.trim() == "") {
            document.getElementById("message").innerHTML = "Please enter a username.";
        } else if (email.trim() == "") {
            document.getElementById("message").innerHTML = "Please enter an email address.";
        } else if (pass1.trim() == "") {
            document.getElementById("message").innerHTML = "Please enter a password.";
        } else if (pass1 != pass2) {
            document.getElementById("message").innerHTML = "The passwords don't match.";
        } else {
        	success = true;
        }
        return success;
    }
    
    /* Does user already exist */
    function doesUserExist() {
        var username = document.getElementById("username").value;
        var data = "username=" + username;
        
        $.post("/SolrFlare/DoesUserExist", data, function(result) {
              if (result == "yes") {
            	  document.getElementById("message").innerHTML = "Username " + username + " is already taken. Please choose a different one.";
              } else {
                  createUserInDb();
                  window.location = "/SolrFlare/SelectPreferences.jsp";
              }
        });
    }
    
    function createUserInDb() {
    	var name = document.getElementById("name").value;
        var username = document.getElementById("username").value;
        var email = document.getElementById("email").value;
        var password = document.getElementById("password").value;
        var data = "name=" + name + "&username=" + username + "&email=" + email + "&password=" + password + "&isfacebookuser=false";

        $.post("/SolrFlare/CreateUserInDB", data, function(result) {
              if (result.trim() == "success") {
                  document.getElementById("message").innerHTML = "Your account has been created.";
              } else {
            	  document.getElementById("message").innerHTML = "Your account could not be created: " + result;
              }
        });
    }
    </script>

    <div id="fb-root"></div>
    
    <!-- UI Elements here -->
    <jsp:include page="Headerbar.jsp" />

    <br>
    <br>
    <section class="loginpanel">
        <div class="wrap">
            <input id="name" class="top-field" type="text" placeholder="Name" required>
            <input id="username" class="mid-field" type="text" placeholder="Username" required>
            <input id="email" class="mid-field" type="email" placeholder="Email-address" required> 
            <input id="password" class="mid-field" type="password" placeholder="Password" required>
            <input id="reenter" class="bottom-field" type="password" placeholder="Re-enter password" required>
            <br />
            <button onclick="makeUser();">Create account</button>
            <br> <br>
            <button id='darkbutton' onclick="window.location.href='Loginpage.jsp'">I already have an account</button>
        </div>
    </section>

    <div class="messagetext" id="message"></div>
    
    <script src="js/index.js"></script>

    <!--Footer bar-->
    <jsp:include page="Footer.html" />

</body>
</html>