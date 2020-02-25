package edu.lehigh.cse216.slj222;

public class Datum {
    private String message;
    private String title;
    private int ID;

    /**
     * Constructor
     * @param theMessage
     * @param theTitle
     */
    public Datum (String theMessage, String theTitle, int theID) {
        this.message = theMessage;
        this.title = theTitle;
        this.ID = theID;
    }


    /**
     * set message
     * @param newMessage
     */
    public void setMessage(String newMessage) {
        this.message = newMessage;
    }

    /**
     * set title
     * @param newTitle
     */
    public void setTitle (String newTitle) {
        this.title = newTitle;
    }

    public void setID (int newID) {
        this.ID = newID;
    }

    /**
     * get message
     * @return the message
     */
    public String getMessage () {
        return this.message;
    }

    /**
     * get title
     * @return the title
     */
    public String getTitle () {
        return this.title;
    }

}
