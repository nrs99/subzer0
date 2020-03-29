package edu.lehigh.cse216.slj222.backend;

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
         * 
         */
        //int comments;
        /**
         * Construct a Message object by providing values for its fields
         */
        public Message(int msgId, String message, String userId, Timestamp dateCreated, int likes, int dislikes) {
            this.msgId = msgId;
            this.message = message;
            this.userId = userId;
            this.dateCreated = dateCreated;
            this.likes = likes;
            this.dislikes = dislikes;
            //this.comments = comments;
        }

        /**
         * String for testing purposes
         */
        public String toString() {
            String str;
            str = "ID: " + msgId + ", Message: " + message;
            return str;
        }

}