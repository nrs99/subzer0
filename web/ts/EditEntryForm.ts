/**
 * EditEntryForm encapsulates all of the code for the form for editing an entry
 */
class EditEntryForm {

    // The name of the DOM entry associated with EditEntryForm
    private static readonly NAME = "EditEntryForm";

    // Track if the Singleton has been initialized
    private static isInit = false;

    private static init() {
	if (!EditEntryForm.isInit) {
	    $("body").append(Handlebars.templates[EditEntryForm.NAME + ".hb"]());
	    $("#" + EditEntryForm.NAME + "-OK").click(EditEntryForm.submitForm);
	    $("#" + EditEntryForm.NAME + "-Close").click(EditEntryForm.hide);
	    EditEntryForm.isInit = true;
	}
    }

    public static refresh() {
	EditEntryForm.init();	
    }

    public static hide() {
        $("#" + EditEntryForm.NAME + "-message").val("");
        $("#" + EditEntryForm.NAME).modal("hide");
    }

    public static show() {
        $("#" + EditEntryForm.NAME + "-message").val("");
        $("#" + EditEntryForm.NAME).modal("show");
    }

    public static submitForm() {
        let id = $(this).data("value");
    	// get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        let msg = "" + $("#" + EditEntryForm.NAME + "-message").val();
        if (msg === "") {
            window.alert("Error: message is empty");
            return;
        }
        EditEntryForm.hide();
        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        $.ajax({
            type: "PUT",
            url: "/messages",
            dataType: "json",
            data: JSON.stringify({ msgID: id, message: msg }),
            success: EditEntryForm.onSubmitResponse
	    });
    }

    public static onSubmitResponse(data: any) {
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
