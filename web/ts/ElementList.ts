/**
 * The ElementList Singleton provides a way of displaying all of the data 
 * stored on the server as an HTML table.
 */
const backendUrl = "http://subzer0.herokuapp.com";
class ElementList {
    /**
     * The name of the DOM entry associated with ElementList
     */
    private static readonly NAME = "ElementList";

    /**
     * Track if the Singleton has been initialized
     * 
     */
    private static isInit = false;

    /**
     * Initialize the ElementList singleton.  
     * This needs to be called from any public static method, to ensure that the 
     * Singleton is initialized before use.
     */
    private static init() {
        if (!ElementList.isInit) {
            ElementList.isInit = true;
        }
    }

    /**
     * update() is the private method used by refresh() to update the 
     * ElementList
     */
    private static update(data: any) {
        // Remove the table of data, if it exists
        $("#" + ElementList.NAME).remove();
        // Use a template to re-generate the table, and then insert it
        $("body").append(Handlebars.templates[ElementList.NAME + ".hb"](data));
        // Find all of the Like buttons, and set their behavior
        $("." + ElementList.NAME + "-likebtn").click(ElementList.clickLike);
        // Find all of the Dislike buttons, and set their behavior
        $("." + ElementList.NAME + "-dislikebtn").click(ElementList.clickDislike);
        $("." + ElementList.NAME + "-commentbtn").click(ElementList.goToComments);
        $("." + ElementList.NAME + "-photo").click(ElementList.goToProfile);
        $("." + ElementList.NAME + "-displayName").click(ElementList.goToProfile);
        // Refresh ElementList after the sort is updated
        $("#" + ElementList.NAME + "-sort").change(ElementList.refresh);
    }

    /**
     * refresh() is the public method for updating the ElementList
     */
    public static refresh() {
        // Make sure the singleton is initialized
        ElementList.init();
        // Issue a GET, and then pass the result to update()
        let sort = "" + $("#" + ElementList.NAME + "-sort").val();
        if (sort == "undefined") sort = "";
        $.ajax({
            type: "GET",
            url: backendUrl + "/messages" + sort,
            dataType: "json",
            success: ElementList.update
        });
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
            data: JSON.stringify({ msgID: id }),
            success: ElementList.refresh
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
            data: JSON.stringify({ msgID: id }),
            success: ElementList.refresh
        });
    }

    public static hide() {
        $("#" + ElementList.NAME).hide();
    }

    public static show() {
        $("#" + ElementList.NAME).show();
    }

    private static goToProfile() {
        let profiledID = $(this).data("value");
        Profile.setID(profiledID);
        Profile.refresh();
        ElementList.hide();
    }

    private static goToComments() {
        let msgId = $(this).data("value");
        ViewComments.setMsgId(msgId);
        ViewComments.refresh();
        ElementList.hide();
    }

}
