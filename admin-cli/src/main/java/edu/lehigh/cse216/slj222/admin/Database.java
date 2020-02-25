package edu.lehigh.cse216.slj222.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.*;

import java.util.ArrayList;

public class Database {
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement mSelectAll;

    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOne;

    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement mDeleteOne;

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertOne;
    /**
     * A prepared statemeting for trigger
     */
    private PreparedStatmet mTrigger;
    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement mUpdateOne;

    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateTable;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropTable;

    /**
     * RowData is like a struct in C: we use it to hold data, and we allow 
     * direct access to its fields.  In the context of this Database, RowData 
     * represents the data we'd see in a row.
     * 
     * We make RowData a static class of Database because we don't really want
     * to encourage users to think of RowData as being anything other than an
     * abstract representation of a row of the database.  RowData and the 
     * Database are tightly coupled: if one changes, the other should too.
     */
    public static class RowData {
        /**
         * The msgid of this row of the database
         */
        int mMsgid;
        /**
         * The userid of this row of the database
         */
        int mUserid;
        /**
         * The datecreated
         */
        String mDatecreated;
        /**
         * The number of likes 
         */
        int mLikes;
        /**
         * The number of dislikes
         */
        int mDislikes;
        /**
         * The message stored in this row
         */
        String  mMessage;
        
        /**
         * Construct a RowData object by providing values for its fields
         */
        public RowData(int msgid, int userid, String datecreated, int likes, int dislikes, String message ) {
            mMsgid = msgid;
            mUserid = userid;
            mDatecreated = datecreated;
            mLikes = likes;
            mDislikes = dislikes;
            mMessage = message;
        }
    }

    /**
     * The Database constructor is private: we only create Database objects 
     * through the getDatabase() method.
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
    //String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
    String hardDbUrl = "jdbc:postgres://wbobgqxniofljr:0feb75c4741735e14f18ab72f07b94562d59741b2db3aae7ffbddbf2d4dd3e43@ec2-52-203-160-194.compute-1.amazonaws.com:5432/d7uf5dueelngct";
    Connection conn = DriverManager.getConnection(hardDbUrl, username, password);
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
        try {
            // NB: we can easily get ourselves in trouble here by typing the
            //     SQL incorrectly.  We really should have things like "tblData"
            //     as constants, and then build the strings for the statements
            //     from those constants.

            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table 
            // creation/deletion, so multiple executions will cause an exception
            db.mCreateTable = db.mConnection.prepareStatement(
                    "CREATE TABLE messages(msgid serial primary key, userid int, datecreated timestamp, likes int, dislikes int, message varchar(250));");
            db.mDropTable = db.mConnection.prepareStatement("DROP TABLE messages;");

            // Standard CRUD operations
            db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM messages WHERE msgid = ?");
            //create sequence
            //db.mTrigger = db.mConnection.prepareStatement("CREATE SEQUENCE seq_simple");
            //db.mInsertOne = db.mConnection.prepareStatement("INSERT into messages (msgid, likes, message) values (seq_simple.nextval,?,?);");
            db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM messages");
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * from messages WHERE msgid=?");
            db.mUpdateOne = db.mConnection.prepareStatement("UPDATE messages SET message = ? WHERE msgid = ?");


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
     * NB: The connection will always be null after this call, even if an 
     *     error occurred during the closing operation.
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
     * @param subject The subject for this new row
     * @param message The message body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertRow(int likes, String message) {
        int count = 0;
        try {
            //mInsertOne.setInt(1, msgid);
            mInsertOne.setInt(2, likes);
            mInsertOne.setString(3, message);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Query the database for a list of all subjects and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowData> selectAll() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("msgid"), rs.getInt("userid"),rs.getString("datecreated"), rs.getInt("likes"), rs.getInt("dislikes"), rs.getString("message")));
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
                res = new RowData(rs.getInt("msgid"), rs.getInt("userid"),rs.getString("datecreated"), rs.getInt("likes"), rs.getInt("dislikes"), rs.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteRow(int id) {
        int res = -1;
        try {
            mDeleteOne.setInt(1, id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the message for a row in the database
     * 
     * @param id The id of the row to update
     * @param message The new message contents
     * 
     * @return The number of rows that were updated.  -1 indicates an error.
     */
    int updateOne(int id, String message) {
        int res = -1;
        try {
            mUpdateOne.setString(1, message);
            mUpdateOne.setInt(2, id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Create tblData.  If it already exists, this will print an error
     */
    void createTable() {
        try {
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove tblData from the database.  If it does not exist, this will print
     * an error.
     */
    void dropTable() {
        try {
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}