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
    String userId;
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
     * 1 - Like
     * -1 - Dislike
     * 0 - No status
     */
    int myLike;

    int commentCount;

    String displayName;

    String photoURL;

    String link;

    String photoImage;

    String mimeType;

    /**
     * Construct a Message object by providing values for its fields
     */
    public Message(int msgId, String message, String userId, int likes, int dislikes, int commentCount, String displayName, String photoURL, String link, String photoImage, String mimeType) {
        this.msgId = msgId;
        this.message = message;
        this.userId = userId;
        this.dateCreated = null;
        this.likes = likes;
        this.dislikes = dislikes;
        this.commentCount = commentCount;
        myLike = 0; // Set it to not be on creation, change later once route is updated
        this.displayName = displayName;
        this.photoURL = photoURL;
        this.link = link;
        this.photoImage = photoImage;
        this.mimeType = mimeType;
    }

    public void setLike(int myLike) {
        this.myLike = myLike;
    }

}
