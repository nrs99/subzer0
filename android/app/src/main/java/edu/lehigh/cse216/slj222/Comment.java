package edu.lehigh.cse216.slj222;

import java.sql.Timestamp;

public class Comment {

    /**
     * The ID of this row of the database
     */
    int commentId;

    /**
     *
     */
    int msgId;
    /**
     * The comment stored in this row
     */
    String comment;
    /**
     * The ID of the user who posted the message
     */
    String userId;
    /**
     * The time the message was originally created
     */
    Timestamp dateCreated;


    public Comment(int commentId, int msgId, String comment, String userId) {
        this.commentId = commentId;
        this.msgId = msgId;
        this.comment = comment;
        this.userId = userId;
        this.dateCreated = null;
    }

}