<div id="NewLogin">

	<!-- load the Google platform libray -->
    <script src="https://apis.google.com/js/platform.js" async defer></script>


    <!-- specifiy your app's client id -->
    <meta name="google-signin-client_id" content="363085709256-27bcmvdo6sqga5b2nsk0ks1g4uh9nf52.apps.googleusercontent.com">
	
	<div class="g-signin2" id = "NewLogin-signIn" data-onsuccess="onSignIn"></div>
	<script>
      function onSignIn(googleUser) {
        var profile = googleUser.getBasicProfile();
        localStorage.setItem("ID", profile.getId());
        localStorage.setItem("givenName", profile.getGivenName());
        localStorage.setItem("fullName", profile.getName());
        localStorage.setItem("myURL", profile.getImageUrl());
        localStorage.setItem("myURL", profile.getImageUrl());
        localStorage.setItem("email", profile.getEmail());
        localStorage.setItem("state", "logged-in");


        $.ajax({
            type: "PUT",
            url: backendUrl + "/user",
            dataType: "json",
            data: JSON.stringify({ userID: localStorage.getItem("ID"), displayName: localStorage.getItem("fullName"),
            photoURL: localStorage.getItem("myURL"), email: localStorage.getItem("email")})

        });

        Navbar.show();
        Navbar.refresh();
        ElementList.refresh();
        NewLogin.hide();
      }
    </script>

</div>
