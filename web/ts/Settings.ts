/**
 * NewCommentForm encapsulates all of the code for the form for adding an entry
 */
class Settings {

    /**
     * The name of the DOM entry associated with NewCommentForm
     */
    private static readonly NAME = "Settings";



    const backendUrl = "http://subzer0.herokuapp.com";

    private static profiledID = "110708943704983237771";


    private static msgid = 0;

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
        if (!Settings.isInit) {
            // $("#" + Settings.NAME + "-OK").click(Settings.submitForm);
            // $("#" + Settings.NAME + "-Close").click(Settings.hide);
            Settings.isInit = true;
        }
    }

    private static update(data: any) {
        $("#" + Settings.NAME).remove();
        // Remove the table of data, if it exists
        // Use a template to re-generate settings page, and then insert it
        $("body").append(Handlebars.templates[Settings.NAME + ".hb"](data));
        // Set go back behavior
        $("." + Settings.NAME + "-goBack").click(Settings.goBack);

    }

    /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    public static refresh() {
        Settings.init();
        $.ajax({
            type: "GET",
            url: backendUrl + "/messages/user/" + Settings.getID(),
            dataType: "json",
            success: Settings.update
        });  
        Settings.init();
    }
    /**
     * Hide the Settins.  Be sure to clear its fields first
     */
    private static hide() {
       $("#" + Settings.NAME).hide();
    }



     

    /**
     * Show the Settings.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear.
     */
    public static show() {
        $("#" + Settings.NAME).show();
    }

    private static goBack() {
        Profile.refresh();
        Settings.hide();
    }

    public static setID(newID) {
        Settings.init();
        Settings.profiledID = newID;
    }

    private static getID(){
        return Settings.profiledID;
    }

    private static followOption(){

        var selected_Follow = document.getElementsByName('Settings-options-follow');
        var selected_Like= document.getElementsByName('Settings-options-like');
        var selected_Comment= document.getElementsByName('Settings-options-comments');
        let tempfollow = true;
        let templike = true;
        let tempcomment = true;
        let userid = localStorage.getItem("ID");

        if(selected_Follow[0].checked){

            console.log(" (YES) Follow val: "+selected_Follow[0]);
            tempfollow = true

        }else if(selected_Follow[1].checked){
            tempfollow = false;

            console.log("(NO) Follow val: "+selected_Follow[1]);


        }else{

            console.log("Error in Follow option...");

        }


        if(selected_Like[0].checked){
            templike = true;

            console.log(" (YES) Someone you followed made a post val: "+selected_Like[0]);

        }else if(selected_Like[1].checked){
            templike = false;

            console.log("(NO) Someone you followed made a post val: "+selected_Like[1]);


        }else{

            console.log("Error in Someone you followed made a post option...");

        }
    

        if(selected_Comment[0].checked){
            tempcomment = true;

            console.log(" (YES) Comment val: "+selected_Comment[0]);

        }else if(selected_Comment[1].checked){
            tempcomment = false;
            console.log("(NO)  Comment val: "+selected_Comment[1]);


        }else{

            console.log("Error in Comment option...");

        }
    

        //set up an AJAX post.  When the server replies, the result will go to
        //onSubmitResponse
        $.ajax({
            type: "POST",
            url: backendUrl + "preferences",//change later
            dataType: "json",
            data: JSON.stringify({userid: userid, followsme: tempfollow, commentsonpost: selected_Comment, followingposts: templike}),
            success: Settings.onSubmitResponse
        });
    }

        /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a 
     * result.
     * 
     * @param data The object returned by the server
     */
    private static onSubmitResponse(data: any) {
        // If we get an "ok" message, clear the form and refresh the main 
        // listing of messages
        if (data.mStatus === "ok") {
            console.log("Data transferred to datbase sucessfully.")
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    }





}