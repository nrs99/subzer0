package edu.lehigh.cse216.slj222.backend;

/**
 * SimpleRequest provides a format for clients to present title and message 
 * strings to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */
public class SimpleRequest {
    /**
     * The message being provided by the client.
     */
    public String message;
    /**
     * The user ID of the client
     */
    public String userID;
    
    public String link;

    public String photoURL;
}