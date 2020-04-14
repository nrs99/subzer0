/**
 * NewCommentForm encapsulates all of the code for the form for adding an entry
 */
class NewCommentForm {

    /**
     * The name of the DOM entry associated with NewCommentForm
     */
    private static readonly NAME = "NewCommentForm";

    const backendUrl = "http://subzer0.herokuapp.com";

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
        if (!NewCommentForm.isInit) {
            $("body").append(Handlebars.templates[NewCommentForm.NAME + ".hb"]());
            $("#" + NewCommentForm.NAME + "-OK").click(NewCommentForm.submitForm);
            $("#" + NewCommentForm.NAME + "-Close").click(NewCommentForm.hide);
            NewCommentForm.isInit = true;
        }
    }

    /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    public static refresh() {
        NewCommentForm.init();
    }

    /**
     * Hide the NewCommentForm.  Be sure to clear its fields first
     */
    private static hide() {
        $("#" + NewCommentForm.NAME + "-message").val("");
        $("#" + NewCommentForm.NAME).modal("hide");
    }

    /**
     * Show the NewCommentForm.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear.
     */
    public static show() {
        $("#" + NewCommentForm.NAME + "-message").val("");
        $("#" + NewCommentForm.NAME).modal("show");
    }


    /**
     * Send data to submit the form only if the fields are both valid.  
     * Immediately hide the form when we send data, so that the user knows that 
     * their click was received.
     */
    private static submitForm() {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        let msg = "" + $("#" + NewCommentForm.NAME + "-message").val();
        if (msg === "") {
            window.alert("Error: message is empty");
            return;
        } else if (msg.length > 250) {
            window.alert("Error: message longer than 250 characters");
        }
        NewCommentForm.hide();

        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        $.ajax({
            type: "POST",
            url: backendUrl + "/comments",
            dataType: "json",
            data: JSON.stringify({ msgId: NewCommentForm.msgid, comment: msg, userId: localStorage.getItem("ID")}),
            success: NewCommentForm.onSubmitResponse
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
            ViewComments.refresh();
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

    public static setID(data: number) {
        this.msgid = data;
    }
}