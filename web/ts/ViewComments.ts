class ViewComments {

    /**
     * The name of the DOM entry associated with ViewComments
     */
    private static readonly NAME = "ViewComments";

    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    private static init() {
        if (!ViewComments.isInit) {
            $("body").append(Handlebars.templates[ViewComments.NAME + ".hb"]());
            $("#" + ViewComments.NAME + "-Close").click(ViewComments.hide);
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
    }

    private static hide() {
        $("#" + ViewComments.NAME).modal("hide");
    }

    public static show() {
        $("#" + ViewComments.NAME).modal("show");
    }


}