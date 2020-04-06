class Profile {

    private static readonly NAME = "Profile";

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
        let id = 117017900165252299426; // PLACEHOLDER
        Profile.init();
        $.ajax({
            type: "GET",
            url: backendUrl + "/messages/user/" + id,
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
}