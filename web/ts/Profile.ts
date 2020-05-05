class Profile {

    private static readonly NAME = "Profile";

    private static profiledID = "117017900165252299426";

    /**
     * Track if the Singleton has been initialized
     * 
     */
    private static isInit = false;

    private static init() {
        if (!Profile.isInit) {
            Profile.isInit = true;
        }
    }

    public static refresh() {
        Profile.init();
        $.ajax({
            type: "GET",
            url: backendUrl + "/messages/user/" + Profile.getID(),
            dataType: "json",
            success: Profile.update
        });  
    }

    private static update(data: any) {
        // Remove the table of data, if it exists
        $("#" + Profile.NAME).remove();
        // Use a template to re-generate the table, and then insert it
        $("body").append(Handlebars.templates[Profile.NAME + ".hb"](data));
        // Find all of the Like buttons, and set their behavior
        $("." + Profile.NAME + "-likebtn").click(Profile.clickLike);
        // Find all of the Dislike buttons, and set their behavior
        $("." + Profile.NAME + "-dislikebtn").click(Profile.clickDislike);
        // Set go back behavior
        $("." + Profile.NAME + "-goBack").click(Profile.goBack);
        $("." + Profile.NAME + "-commentbtn").click(Profile.goToComments);
        $("." + Profile.NAME + "-linkbtn").click(Profile.openLink);
        $("#" + Profile.NAME + "-follow").click(Profile.follow);
        $("#" + Profile.NAME + "-settings").click(Profile.goToSettings);

    console.log("profile.getid: "+Profile.getID());
    console.log("local storage"+localStorage.getItem("ID"));
    // var localstor = "101723331302029401229";
    if(Profile.getID() == localStorage.getItem("ID")){//hiding follow button...CHANGE LATER
    // if(Profile.getID() ==localstor){//hiding follow button...CHANGE LATER

                var x = document.getElementById("Profile-follow");
                var y = document.getElementById("Profile-settings");
                x.style.display = "none";
                y.style.display = "block";


        }else{
            var x = document.getElementById("Profile-follow");
            var y = document.getElementById("Profile-settings");
            x.style.display = "block";
            y.style.display = "none";

        }
    }


    private static onSubmitResponse(data: any) {
        // If we get an "ok" message, clear the form and refresh the main 
        // listing 
        if (data.mStatus === "ok") {
            Profile.refresh();
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("You can't follow yourself.");
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    }

    /**
     * clickLike is the code we run in response to a click of a like button
     */
    private static clickLike() {
        let id = $(this).data("value");
        let userID = localStorage.getItem("ID");
	    $.ajax({
		    type: "PUT",
    		url: backendUrl + "/messages/" + id + "/like/" + userID,
            dataType: "json",
            data: JSON.stringify({ msgID : id }),
		    success: Profile.refresh
    	});
    }
    /** 
     * clickDislike is the code we run in response to a click of a like button
     */ 
    private static clickDislike() {
        let id = $(this).data("value");
        let userID = localStorage.getItem("ID");
        $.ajax({
            type: "PUT",
            url: backendUrl + "/messages/" + id + "/dislike/" + userID,
            dataType: "json",
            data: JSON.stringify({ msgID : id }),
            success: Profile.refresh
        });
    }

    public static hide() {
        $("#" + Profile.NAME).hide();
    }

    public static show() {
        $("#" + Profile.NAME).show();
    }

    private static goBack() {
        Profile.hide();
        ElementList.refresh();
        ElementList.show();
    }

    public static setID(newID) {
        Profile.init();
        Profile.profiledID = newID;
    }

    private static getID(){
        return Profile.profiledID;
    }

    private static goToComments() {
        let msgId = $(this).data("value");
        ViewComments.setMsgId(msgId);
        NewCommentForm.setID(msgId);
        ViewComments.refresh();
        ViewComments.show();
        Profile.hide();
    }

    private static goToSettings(){
        Profile.hide();
        Settings.refresh();
        Settings.show();
        
    }

    private static openLink() {
        let url = $(this).data("value");
        window.open(url, '_blank');
    }

            /**
     * Send data to submit the form only if the fields are both valid.  
     * Immediately hide the form when we send data, so that the user knows that 
     * their click was received.
     */
    private static follow() {//the hiding button needs to be added!!!


        let useridB = Profile.getID();
        let useridA = localStorage.getItem("ID");
        console.log("useridA="+useridA+" useridB="+useridB);
        var x = document.getElementById("Profile-follow");
    
        //set up an AJAX post.  When the server replies, the result will go to
        //onSubmitResponse
        $.ajax({
            type: "PUT",
            url: backendUrl + "follow",//change later
            dataType: "json",
            data: JSON.stringify({userA: useridA, userB: useridB}),
            success: Profile.onSubmitResponse
        });

    }
}