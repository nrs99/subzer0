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
     * A prepared statement for unliking a message in the database
     */
    private PreparedStatement mUnlike;

    /**
     * RowData is like a struct in C: we use it to hold data, and we allow direct
     * access to its fields. In the context of this Database, RowData represents the
     * data we'd see in a row.
     * 
     * We make RowData a static class of Database because we don't really want to
     * encourage users to think of RowData as being anything other than an abstract
     * representation of a row of the database. RowData and the Database are tightly
     * coupled: if one changes, the other should too.
     */
    public static class RowData {
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
         * Construct a RowData object by providing values for its fields
         */
        public RowData(int msgId, String message, int userId, Timestamp dateCreated, int likes) {
            this.msgId = msgId;
            this.message = message;
            this.userId = userId;
            this.dateCreated = dateCreated;
            this.likes = likes;
        }
    }

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
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
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
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO messages VALUES (default, ?, ?, 0, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * from messages WHERE msgid=?");
            db.mSelectAllNewest = db.mConnection.prepareStatement("SELECT * from messages ORDER BY datecreated DESC");
            db.mSelectAllOldest = db.mConnection.prepareStatement("SELECT * from messages ORDER BY datecreated ASC");
            db.mSelectAllPopular = db.mConnection.prepareStatement("SELECT * from messages ORDER BY likes DESC");
            db.mLike = db.mConnection.prepareStatement("UPDATE messages SET likes = likes + 1 WHERE msgid = ?");
            db.mUnlike = db.mConnection.prepareStatement("UPDATE messages SET likes = likes - 1 WHERE msgid = ?");
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
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
    int insertRow(String message, int userId) {
        int count = 0;
        try {
            mInsertOne.setInt(1, userId);
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
    ArrayList<RowData> selectAllNewest() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = mSelectAllNewest.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("msgId"), rs.getString("message"), rs.getInt("userId"),
                        rs.getTimestamp("dateCreated"), rs.getInt("likes")));
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
    ArrayList<RowData> selectAllOldest() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = mSelectAllOldest.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("msgId"), rs.getString("message"), rs.getInt("userId"),
                        rs.getTimestamp("dateCreated"), rs.getInt("likes")));
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
    ArrayList<RowData> selectAllPopular() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = mSelectAllPopular.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("msgId"), rs.getString("message"), rs.getInt("userId"),
                        rs.getTimestamp("dateCreated"), rs.getInt("likes")));
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
    RowData selectOne(int id) {
        RowData res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowData(rs.getInt("msgId"), rs.getString("message"), rs.getInt("userId"),
                        rs.getTimestamp("dateCreated"), rs.getInt("likes"));
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
     * @param id msgID of message to be unliked
     */
    int unlikeOne(int id) {
        int res = 0;
        try {
            mUnlike.setInt(1, id);
            res = mUnlike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

}