class ViewComments {

    /**
     * The name of the DOM entry associated with ViewComments
     */
    private static readonly NAME = "ViewComments";

    private static msgid = 2;

    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    private static init() {
        if (!ViewComments.isInit) {
            ViewComments.isInit = true;
        }
    }

    /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    public static refresh() {
        ViewComments.init();
        $.ajax({
            type: "GET",
            url: backendUrl + "/messages/" + this.msgid + "/comments",
            dataType: "json",
            success: ViewComments.update
        });
    }

    public static hide() {
        $("#" + ViewComments.NAME).hide();
    }

    public static show() {
        $("#" + ViewComments.NAME).show();
    }

    private static update(data: any) {
        // Remove the table of data, if it exists
        $("#" + ViewComments.NAME).remove();
        // Use a template to re-generate the table, and then insert it
        $("body").append(Handlebars.templates[ViewComments.NAME + ".hb"](data));
        // Set go back behavior
        $("." + ViewComments.NAME + "-goBack").click(ViewComments.goBack);
        $("." + ViewComments.NAME + "-newComment").click(NewCommentForm.show);
        $("." + ViewComments.NAME + "-editbtn").click(ViewComments.editComment);
        $("." + ViewComments.NAME + "-photo").click(ViewComments.goToProfile);
        $("." + ViewComments.NAME + "-displayName").click(ViewComments.goToProfile);
    }

    public static setMsgId(newId) {
        ViewComments.msgid = newId;
    }

    private static goBack() {
        ViewComments.hide();
        ElementList.refresh();
        ElementList.show();
    }

    private static editComment() {
        let cid = $(this).data("value");
        EditEntryForm.setID(cid);
        EditEntryForm.show();
    }

    private static goToProfile() {
        let profiledID = $(this).data("value");
        Profile.setID(profiledID);
        Profile.refresh();
        Profile.show();
        ViewComments.hide();
    }


}