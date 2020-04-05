//var newLoginForm: NewLogin;

/**
 * NewLogin encapsulates all of the code for the form for logging in
 */

declare var gapi: any;
declare var GoogleAuth: any; // Google Auth object.
declare var Handlebars: any;
declare var request: any;
declare var profile: any;

const backendUrl = "https://subzer0.herokuapp.com/";

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

            NewLogin.isInit = true;
        }
    }

    public static refresh() {
        NewLogin.init();
    }

    private static onSignIn(googleUser) {
        var profile = googleUser.getBasicProfile();
        console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
        console.log('Name: ' + profile.getName());
        console.log('Image URL: ' + profile.getImageUrl());
        console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.

        // Don't show these things until logged in
        ElementList.show();
        Navbar.show();
    }

    public static signOut() {
        var auth2 = gapi.auth2.getAuthInstance();
        auth2.signOut().then(function () {
          console.log('User signed out.');
        });
        ElementList.hide();
        Navbar.hide();
      }
}