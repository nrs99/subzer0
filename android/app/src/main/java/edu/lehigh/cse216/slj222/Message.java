package edu.lehigh.cse216.slj222;

import java.sql.Timestamp;

import java.sql.Timestamp;

public class Message {

    /**
     * The ID of this row of the database
     */
    int msgId;
    /**
     * The message stored in this row
     */
    String message;
    /**
     * The ID of the user who posted the message
     */
    int userId;
    /**
     * The time the message was originally created
     */
    Timestamp dateCreated;
    /**
     * The number of likes the message contains
     */
    int likes;
    /**
     * The number of dislikes the message contains
     */
    int dislikes;

    /**
     * Construct a Message object by providing values for its fields
     */
    public Message(int msgId, String message, int userId, int likes, int dislikes) {
        this.msgId = msgId;
        this.message = message;
        this.userId = userId;
        this.dateCreated = null;
        this.likes = likes;
        this.dislikes = dislikes;
    }

}
