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
}