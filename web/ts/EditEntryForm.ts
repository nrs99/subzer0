
/**
 * EditEntryForm encapsulates all of the code for the form for editing an entry
 * this can be seen to be represented by the edit pop-up button
 */
class EditEntryForm {

    /**
     * The name of the DOM entry associated with EditEntryForm
     */
    private static readonly NAME = "EditEntryForm";

    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    /**
     * Track ID of message that gets passed in from click of Edit Message button on ElementList
     */
    private static messageID: any;

    /**
     * Initialize the EditEntryForm by creating its element in the DOM and 
     * configuring its buttons.  This needs to be called from any public static 
     * method, to ensure that the Singleton is initialized before use. This method 
     * was modified from the steps of the tutorial.
     */
    public static init() {
        if (!EditEntryForm.isInit) {
            $("body").append(Handlebars.templates[EditEntryForm.NAME + ".hb"]());
            $("#" + EditEntryForm.NAME + "-OK").click(EditEntryForm.submitForm);
            $("#" + EditEntryForm.NAME + "-Close").click(EditEntryForm.hide);
            EditEntryForm.isInit = true;
        }
    }

    /**
     * Refresh() doesn't really have much meaning, but just like in Navbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init(). Will not be necessary to refresh the edit bar as it will be a pop-up
     * Include it more so for consistency 
     */
    public static refresh() {
        EditEntryForm.init();
    }

    /**
     * Show the EditEntryForm.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear. Also set the
     * static id variable to what was passed in from the Edit Message button click
     * 
     * @param ID The id of the message
     */
    public static show(ID: any) {
        EditEntryForm.messageID = ID;
        $("#" + EditEntryForm.NAME + "-message").val("");
        $("#" + EditEntryForm.NAME).modal("show");
    }

    /**
     * Hide the EditEntryForm.  Be sure to clear its fields first; this will be necessary
     * for this to just be a pop-up statement 
     */
    private static hide() {
        $("#" + EditEntryForm.NAME + "-message").val("");
        $("#" + EditEntryForm.NAME).modal("hide");
    }

    

    /**
     * Send data to submit the form only if the field is valid.
     * This was modified from the tutorial reccomendations 
     */
    private static submitForm() {
        // get the value of message field, force it to be a string, and check 
        // that it is not empty
        let msg = "" + $("#" + EditEntryForm.NAME + "-message").val();
        if (msg === "") {
            window.alert("Please enter message below:");
            return;
        }
        EditEntryForm.hide();
        //PUT command to the backend for the backend to take the info edited and then POST
		//it to the website  
		//url: backendUrl + ...
        $.ajax({
            type: "PUT",
            url: "/messages/" + EditEntryForm.messageID,
            dataType: "json",
            data: JSON.stringify({mMessage: msg}),
            success: EditEntryForm.onSubmitResponse
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
            ElementList.refresh();
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
} // end class EditEntryForm


