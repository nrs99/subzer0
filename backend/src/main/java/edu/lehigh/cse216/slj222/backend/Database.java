package edu.lehigh.cse216.slj222.backend;
 
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
 
import java.util.ArrayList;
 
public class Database {
    /**
     * The connection to the database. When there is no connection, it should be
     * null. Otherwise, there is a valid open connection
     */
    private Connection mConnection;
 
    /**
     * A prepared statement for getting all data in the database Newest messages
     * first
     */
    private PreparedStatement mSelectAllNewest;
 
    /**
     * A prepared statement for getting all data in the database Oldest messages
     * first
     */
    private PreparedStatement mSelectAllOldest;
 
    /**
     * A prepared statement for getting all data in the database Oldest messages
     * first
     */
    private PreparedStatement mSelectAllPopular;
 
    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOne;
 
    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertOne;
 
    /**
     * A prepared statement for liking a message in the database
     */
    private PreparedStatement mLike;
 
    /**
     * A prepared statement for disliking a message in the database
     */
    private PreparedStatement mDislike;
 
    private PreparedStatement mInsertVote;
 
    private PreparedStatement mRemoveVote;
 
    private PreparedStatement mInsertComment;
 
    private PreparedStatement mEditComment;
 
    private PreparedStatement mGetComments;
 
    private PreparedStatement mMessagesByUser;
 
    /**
     * The Database constructor is private: we only create Database objects through
     * the getDatabase() method.
     */
    private Database() {
    }
 
    /**
     * Get a fully-configured connection to the database
     *
     * @param ip   The IP address of the database server
     * @param port The port on the database server to which connection requests
     *             should be sent
     * @param user The user ID to use when connecting
     * @param pass The password to use when connecting
     *
     * @return A Database object, or null if we cannot connect properly
     */
    static Database getDatabase(String db_url) {
        // Create an un-configured Database object
        Database db = new Database();
 
        // Give the Database object a connection, fail if we cannot get one
        try {
            Class.forName("org.postgresql.Driver");
            URI dbUri = new URI(db_url);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
            Connection conn = DriverManager.getConnection(dbUrl, username, password);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Unable to find postgresql driver");
            return null;
        } catch (URISyntaxException s) {
            System.out.println("URI Syntax Error");
            return null;
        }
 
        // Attempt to create all of our prepared statements. If any of these
        // fail, the whole getDatabase() call should fail
        try {
 
            // Standard CRUD operations
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO messages VALUES (default, ?, ?, 0, 0, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * from messages WHERE msgid=?");
            db.mSelectAllNewest = db.mConnection.prepareStatement("SELECT * from messages ORDER BY datecreated DESC");
            db.mSelectAllOldest = db.mConnection.prepareStatement("SELECT * from messages ORDER BY datecreated ASC");
            db.mSelectAllPopular = db.mConnection.prepareStatement("SELECT * from messages ORDER BY (likes - dislikes) DESC");
            db.mLike = db.mConnection.prepareStatement("UPDATE votes SET vote = 1 WHERE msgid = ?");
            db.mDislike = db.mConnection.prepareStatement("UPDATE votes SET vote = -1 WHERE msgid = ?");
            db.mInsertVote = db.mConnection.prepareStatement("INSERT INTO votes VALUES(?, ?, 0)");
            db.mRemoveVote = db.mConnection.prepareStatement("DELETE FROM votes where msgid = ? and userid = ?");
            db.mInsertComment = db.mConnection.prepareStatement("INSERT INTO comments VALUES (?, default, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            db.mEditComment = db.mConnection.prepareStatement("UPDATE comments SET comment = ? WHERE commentid = ?");
            db.mGetComments = db.mConnection.prepareStatement("SELECT * from comments WHERE mid=?");
            db.mMessagesByUser = db.mConnection.prepareStatement("SELECT * from messages WHERE userid = ? ORDER BY datecreated DESC");
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
    }
 
    Connection getConnection() {
        return mConnection;
    }
 
    /**
     * Close the current connection to the database, if one exists.
     *
     * NB: The connection will always be null after this call, even if an error
     * occurred during the closing operation.
     *
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            mConnection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }
 
    /**
     * Insert a row into the database
     *
     * @param message The message body in the new row
     * @param userId  The user ID of the person who is posting the message
     *
     * @return The number of rows that were inserted
     */
    int insertRow(String message, String userId) {
        int count = 0;
        try {
            mInsertOne.setString(1, userId);
            mInsertOne.setTimestamp(2, new Timestamp(System.currentTimeMillis())); // Gets current time of system
            mInsertOne.setString(3, message);
            mInsertOne.executeUpdate();
            ResultSet rs = mInsertOne.getGeneratedKeys();
            if (rs.next()) {
                count += rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
 
    /**
     * Query the database for a list of all rows from newest to oldest
     *
     * @return All rows, as an ArrayList
     */
    ArrayList<Message> selectAllNewest() {
        ArrayList<Message> res = new ArrayList<Message>();
        try {
            ResultSet rs = mSelectAllNewest.executeQuery();
            while (rs.next()) {
                res.add(new Message(rs.getInt("msgId"), rs.getString("message"), rs.getString("userId"),
                        rs.getTimestamp("dateCreated"), rs.getInt("likes"), rs.getInt("dislikes")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
 
    /**
     * Query the database for a list of all rows from oldest to newest
     *
     * @return All rows, as an ArrayList
     */
    ArrayList<Message> selectAllOldest() {
        ArrayList<Message> res = new ArrayList<Message>();
        try {
            ResultSet rs = mSelectAllOldest.executeQuery();
            while (rs.next()) {
                res.add(new Message(rs.getInt("msgId"), rs.getString("message"), rs.getString("userId"),
                        rs.getTimestamp("dateCreated"), rs.getInt("likes"), rs.getInt("dislikes")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
 
    /**
     * Query the database for a list of all rows from most likes to least likes
     *
     * @return All rows, as an ArrayList
     */
    ArrayList<Message> selectAllPopular() {
        ArrayList<Message> res = new ArrayList<Message>();
        try {
            ResultSet rs = mSelectAllPopular.executeQuery();
            while (rs.next()) {
                res.add(new Message(rs.getInt("msgId"), rs.getString("message"), rs.getString("userId"),
                        rs.getTimestamp("dateCreated"), rs.getInt("likes"), rs.getInt("dislikes")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
 
    /**
     * Get all data for a specific row, by ID
     *
     * @param id The id of the row being requested
     *
     * @return The data for the requested row, or null if the ID was invalid
     */
    Message selectOne(int id) {
        Message res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new Message(rs.getInt("msgId"), rs.getString("message"), rs.getString("userId"),
                        rs.getTimestamp("dateCreated"), rs.getInt("likes"), rs.getInt("dislikes"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
 
    /**
     * Add a like to a certain message
     *
     * @param id msgID of message to be liked
     */
    int likeOne(int id) {
        int res = 0;
        try {
            mLike.setInt(1, id);
            res = mLike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
 
    /**
     * Remove a like from a certain message
     *
     * @param id msgID of message to be disliked
     */
    int dislikeOne(int id) {
        int res = 0;
        try {
            mDislike.setInt(1, id);
            res = mDislike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
 
    int insertVote(int msgid, String userid) {
        int count = 0;
        try {
            mInsertVote.setInt(1, msgid);
            mInsertVote.setString(2, userid);
            mInsertVote.executeUpdate();
            ResultSet rs = mInsertVote.getGeneratedKeys();
            if (rs.next()) {
                count += rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
 
    int removeVote(int msgid, String userid) {
        try {
            mRemoveVote.setInt(1, msgid);
            mRemoveVote.setString(2, userid);
            mRemoveVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
 
    int insertComment(int msgid, String comment, String userid) {
        int count = 0;
        try {
            mInsertComment.setInt(1, msgid);
            mInsertComment.setString(2, userid);
            mInsertComment.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            mInsertComment.setString(4, comment);
        mInsertComment.executeUpdate();
            ResultSet rs = mInsertOne.getGeneratedKeys();
            if (rs.next()) {
                count += rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
 
    int editComment(int commentid, String comment) {
        int count = 0;
        try {
            mEditComment.setInt(1, commentid);
            mEditComment.setString(2, comment);
            count = mEditComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
   
    ArrayList<Comment> getComments(int msgId) {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        try {
            mGetComments.setInt(1, msgId);
            ResultSet rs = mGetComments.executeQuery();
            while(rs.next()) {
                comments.add(new Comment(rs.getInt("commentId"), rs.getInt("msgId"),
                    rs.getString("comment"), rs.getString("userId"), rs.getTimestamp("dateCreated")));
            }
            rs.close();
            return comments;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
 
    }
 
    ArrayList<Message> selectAllByUser(String userID) {
        ArrayList<Message> res = new ArrayList<Message>();
        try {
            mMessagesByUser.setString(1, userID);
            ResultSet rs = mMessagesByUser.executeQuery();
            while (rs.next()) {
                res.add(new Message(rs.getInt("msgId"), rs.getString("message"), rs.getString("userId"),
                        rs.getTimestamp("dateCreated"), rs.getInt("likes"), rs.getInt("dislikes")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}