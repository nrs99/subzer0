//var newLoginForm: NewLogin;

/**
 * NewLogin encapsulates all of the code for the form for logging in
 */

declare var gapi: any;
declare var GoogleAuth: any; // Google Auth object.
declare var googleUser: any;
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

    private static signedInID = "";

    /**
     * Initialize the NewLogin by creating its element in the DOM and 
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


    public static signOut() {
        var auth2 = gapi.auth2.getAuthInstance();
        auth2.signOut().then(function () {
          console.log('User ' + localStorage.getItem("fullName") + ' signed out.');
        });
        localStorage.setItem("state", "logged-out");
        ElementList.hide();
        Navbar.hide();
        Profile.hide();
        ViewComments.hide();
        NewLogin.show();
    }

    public static hide() {
        $("#" + NewLogin.NAME).hide();
    }

    public static show() {
        NewLogin.refresh();
        $("#" + NewLogin.NAME).show();
    }

    public static getID() {
        return this.signedInID;
    }
}