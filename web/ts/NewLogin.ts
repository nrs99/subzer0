//var newLoginForm: NewLogin;
/**
 * NewLogin encapsulates all of the code for the form for logging in
 */

 declare var gapi:any;
 declare var GoogleAuth: any; // Google Auth object.

class NewLogin {
	/**
     * The name of the DOM entry associated with NewEntryForm
     */
    private static readonly NAME = "NewLogin";

    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    /**
     * Initialize the NewEntryForm by creating its element in the DOM and 
     * configuring its buttons.  This needs to be called from any public static 
     * method, to ensure that the Singleton is initialized before use
     */
    private static init() {
        if (!NewLogin.isInit) {
            $("body").prepend(Handlebars.templates[NewLogin.NAME + ".hb"]());
            $("#" + NewLogin.NAME + "-signIn").click(NewLogin.show);
            NewLogin.initClient();
            NewLogin.renderButton();
            $("#" + NewLogin.NAME + "-signOff").click(NewLogin.signOut);

            NewLogin.isInit = true;
        }
    }

    /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    public static refresh() {
        NewLogin.init();
    }

    /**
     * Hide the NewEntryForm.  Be sure to clear its fields first
     */
    private static hide() {
        $("#" + NewLogin.NAME + "-signOut").val("");
        $("#" + NewLogin.NAME).modal("hide");
    }

    private static initClient() {
       gapi.client.init({
            'apiKey': 'AIzaSyBNFCZM7GjZSnX_qh-ucZ8MPlAOEo41ZLU',
            'clientId': '363085709256-27bcmvdo6sqga5b2nsk0ks1g4uh9nf52',
            'scope': 'https://www.googleapis.com/auth/drive.metadata.readonly',
            'discoveryDocs': ['https://www.googleapis.com/discovery/v1/apis/drive/v3/rest']
    }).then(function () {
        GoogleAuth = gapi.auth2.getAuthInstance();

        // // Listen for sign-in state changes.
        // NewLogin.GoogleAuth.isSignedIn.listen(updateSigninStatus);
    });
    } 

    /**
     * Show the NewEntryForm.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear.
     */
    public static show() { //similar to rednder button
        $("#" + NewLogin.NAME).modal("show"); 
    }
    
    public static renderButton() {
        //$("#" + NewLogin.NAME).modal("show");
        gapi.signin2.render('NewLogin-signIn', {
            'scope' : "profile email",
            'width' : 240,
            'height': 50,
            'longtitle': true,
            'theme' : 'dark',
            'onsuccess': NewLogin.onSuccess,
            'onfailiure': NewLogin.onFailure
        });
    }
    
    public static onSuccess (googleUser: any) {
        //get the google profile data
        //var profile = googleUser.getBasicProfile();
        //retrieve the google account data
        
        gapi.client.load('auth2', 'v2', function() {
            let request = gapi.client.oauth2.userInfo.get('me');
        });

         gapi.client.load('auth2', function () {
            if (GoogleAuth.isSignedIn.get()) {
            let profile = GoogleAuth.currentUser.get().getBasicProfile();
            
            console.log('ID: ' + profile.getId());
            console.log('Full Name: ' + profile.getName());
            console.log('Given Name: ' + profile.getGivenName());
            console.log('Family Name: ' + profile.getFamilyName());
            console.log('Image URL: ' + profile.getImageUrl());
            console.log('Email: ' + profile.getEmail());
          }
            request.execute(function(resp) {
                let profileHTML = '<h3>Welcome ' + resp.name + '! <a href = "javascript:void(0);" onclick = "signOut();"> Sign out</a></h3>';
                profileHTML+= 'img src = " ' + resp.pic + ' " /><p><b>GoogleID: </b> ' + resp.id + '</p><p><b>Name: </b> ' + resp.name + '</p><p><b>Email: </b>' + resp.email + '</p><p><b>Gender: </b' +
                resp.gender + '</p><p><b>Locale: </b>' + resp.locale + '</p><p><b> Google profile: </b> <a target="_blank" href = " ' + resp.link + ' "> click to view profile</a></p>';
                document.getElementsByClassName("userContent")[0].innerHTML = profileHTML;
                document.getElementById("signIn").style.display = "none";
                //document.getElementsByClassName("userContent")[0].style.display = "block";
                NewLogin.saveUserData(resp);


            });
        });
    }

    private static onFailure(error: any) {
        alert(error)
    }

    private static saveUserData(userData) {
        //$.post('some file', {oauth_provider: 'google', userData: JSON.stringify(userData)});
    }
    private static signOut() {
       // $("#" + NewLogin.NAME).hide();
        let auth2 = gapi.auth2.getAuthInstance();
        auth2.signOut().then(function() {
            console.log('User signed out.');
        });
        auth2.disconnect();
    }
} // end of NewLogin class
