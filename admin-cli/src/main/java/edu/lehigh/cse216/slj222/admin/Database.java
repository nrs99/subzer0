package edu.lehigh.cse216.slj222.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.net.*;

import java.util.ArrayList;

public class Database {
    //messages
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection Connection;

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
    //private PreparedStatmet mTrigger;
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

    



//likes
        /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement lSelectAll;

    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement lSelectOne;

    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement lDeleteOne;

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement lInsertOne;
    /**
     * A prepared statemeting for trigger
     */
    //private PreparedStatmet mTrigger;
    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement lUpdateOne;

    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement lCreateTable;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement lDropTable;


//Comments
        /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement cSelectAll;

    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement cSelectOne;

    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement cDeleteOne;

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement cInsertOne;
    /**
     * A prepared statemeting for trigger
     */
    //private PreparedStatmet mTrigger;
    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement cUpdateOne;

    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement cCreateTable;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement cDropTable;






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
    public static class RowData{
        /**
         * The msgid of this row of the database
         */
        int mMsgid;
         /**
         * The msgid of this row of the database
         */       
        int lMsgid;
        /**
         * The msgid of this row of the database
         */
        int cMsgid;
        /**
         * The userid of this row of the database
         */
        String mUserid;
        
        /**
         * The userid of this row of the database
         */
        String lUserid;
         /**
         * The userid of this row of the database
         */
        String cUserid;
        /**
         * The datecreated
         */
        String mDatecreated;
        /**
         * The datecreated
         */
        String cDatecreated;
        /**
         * The number of likes/dislikes
         */
        int mLike;


        int lLikes;
        /**
         * The message stored in this row
         */
        String  mMessage;
        /**
         * The Comment id 
         */
        int cCommentID;
        /**
         * The message stored in this row
         */
        String  cComment;
        
        /**
         * Construct a RowData object by providing values for its fields
         * messages---
         */
        public RowData(int msgid, String userid, String datecreated, String message) {
            mMsgid = msgid;
            mUserid = userid;
            mDatecreated = datecreated;
            mMessage = message;
        }
        /**
         * Construct a RowData object by providing values for its fields
         * likes---
         */
        public RowData( String userid, int likes, int mid) {
            lMsgid = mid;//not sure about this
            lUserid = userid;
            lLikes = likes;
        }

        //COMMENTS LATER

        public RowData(int msgid, int commentid, String userid, String datecreated, String comment) {
            cMsgid = msgid;
            cCommentID = commentid;
            cUserid = userid;
            cDatecreated = datecreated;
            cComment = comment;
        }

    }

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
    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
    Connection conn = DriverManager.getConnection(dbUrl, username, password);
    if (conn == null) {
        System.err.println("Error: DriverManager.getConnection() returned a null object");
        return null;
    }
    db.Connection = conn;
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
            db.mCreateTable = db.Connection.prepareStatement("CREATE TABLE messages(msgid SERIAL PRIMARY KEY, userid VARCHAR(30), datecreated TIMESTAMP, message VARCHAR(250));");
            // db.mDropTable = db.Connection.prepareStatement("DROP TABLE messages;");

            // Standard CRUD operations
            db.mDeleteOne = db.Connection.prepareStatement("DELETE FROM messages WHERE msgid = ?;");
            //create sequence
            //db.mTrigger = db.Connection.prepareStatement("CREATE SEQUENCE seq_simple");
            db.mInsertOne = db.Connection.prepareStatement("INSERT INTO messages(msgid, userid, datecreated, message) VALUES (default,?, ?, ?);");
            db.mSelectAll = db.Connection.prepareStatement("SELECT * FROM messages;");
            db.mSelectOne = db.Connection.prepareStatement("SELECT * from messages WHERE msgid=?;");
            db.mUpdateOne = db.Connection.prepareStatement("UPDATE messages SET message = ? WHERE msgid = ?;");


             // NB: we can easily get ourselves in trouble here by typing the
            //     SQL incorrectly.  We really should have things like "tblData"
            //     as constants, and then build the strings for the statements
            //     from those constants.
            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table 
            // creation/deletion, so multiple executions will cause an exception


            //LIKES
            db.lCreateTable = db.Connection.prepareStatement("CREATE TABLE likes(userid VARCHAR(30), likes INT);"
            +"ALTER TABLE likes ADD COLUMN mid INT;"
            +" ALTER TABLE likes ADD CONSTRAINT fk_mid FOREIGN KEY(mid) REFERENCES messages(msgid);");
            // db.mDropTable = db.Connection.prepareStatement("DROP TABLE like;");

            // Standard CRUD operations
            db.lDeleteOne = db.Connection.prepareStatement("DELETE FROM likes WHERE msgid = ?;");
            //create sequence
            //db.mTrigger = db.Connection.prepareStatement("CREATE SEQUENCE seq_simple");

            db.lInsertOne = db.Connection.prepareStatement("INSERT INTO likes(userid, likes, mid) VALUES (?, ?, default);");
            db.lSelectAll = db.Connection.prepareStatement("SELECT * FROM likes;");
            db.lSelectOne = db.Connection.prepareStatement("SELECT * FROM likes WHERE userid=?;");
            db.lUpdateOne = db.Connection.prepareStatement("UPDATE likes SET like = ? WHERE mid = ?;");


        // //Comments
            // NB: we can easily get ourselves in trouble here by typing the
            //     SQL incorrectly.  We really should have things like "tblData"
            //     as constants, and then build the strings for the statements
            //     from those constants.

            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table 
            // creation/deletion, so multiple executions will cause an exception
            db.cCreateTable = db.Connection.prepareStatement("CREATE TABLE comments();"
            +"ALTER TABLE comments ADD COLUMN mid INT;"
            +"ALTER TABLE comments ADD CONSTRAINT fk_mid FOREIGN KEY(mid) REFERENCES messages(msgid);"
            +"ALTER TABLE comments ADD COLUMN commentID SERIAL PRIMARY KEY;"
            +"ALTER TABLE comments ADD COLUMN userID VARCHAR(30);"
            +"ALTER TABLE comments ADD COLUMN datecreated TIMESTAMP;"
            +"ALTER TABLE comments ADD COLUMN comment VARCHAR(250);");
            // db.mDropTable = db.Connection.prepareStatement("DROP TABLE messages;");

            // Standard CRUD operations
            db.cDeleteOne = db.Connection.prepareStatement("DELETE FROM comments WHERE commentID = ?");
            //create sequence
            //db.mTrigger = db.Connection.prepareStatement("CREATE SEQUENCE seq_simple");
            db.cInsertOne = db.Connection.prepareStatement("INSERT INTO comments(mid, commentID, userID, datecreated, comment) VALUES (default, default, ?, ?, ?);");
            db.cSelectAll = db.Connection.prepareStatement("SELECT * FROM comments;");
            db.cSelectOne = db.Connection.prepareStatement("SELECT * from comments WHERE comment=?;");
            db.cUpdateOne = db.Connection.prepareStatement("UPDATE comments SET comment = ? WHERE commentID = ?;");
        


        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
    }

    /**
     * Close the current connection to the database, if one exists.1
     * 
     * NB: The connection will always be null after this call, even if an 
     *     error occurred during the closing operation.
     * 
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (Connection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            Connection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            Connection = null;
            return false;
        }
        Connection = null;
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
    int insertRowMessages(String userid, String message) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        int count = 0;
        try {
            mInsertOne.setString(1, userid);

            mInsertOne.setTimestamp(2, ts);

            mInsertOne.setString(3, message);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

     /**
     * Insert a row into the database
     *
     * @param subject The subject for this new row
     * @param like the value of the boolean like 
     * 
     * 
     * 
     * @return The number of rows that were inserted
     */
    // int insertRowLikes(int userid, int like,int mid) {//unsure!
        int insertRowLikes(String userid, int like) {//unsure!
        int count = 0;
        try {
            lInsertOne.setString(1, userid);
            lInsertOne.setInt(2, like);
            // lInsertOne.setInt(3,mid);
            count += lInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }


     /**
     * Insert a row into the database
     * 
     * @param subject The subject for this new row
     * @param like the value of the boolean like 
     * 
     * 
     * 
     * @return The number of rows that were inserted
     */
    int insertRowComments(String userid, String comment) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        int count = 0;
        try {
            cInsertOne.setString(1, userid);
            cInsertOne.setTimestamp(2, ts);
            cInsertOne.setString(3, comment);
            count += cInsertOne.executeUpdate();
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
    ArrayList<RowData> selectAllMessgaes() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("msgid"), rs.getString("userid"),rs.getString("datecreated"),rs.getString("message")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //LIKES
    /**
     * Query the database for a list of all subjects and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowData> selectAllLikes() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = lSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getString("userid"),rs.getInt("likes"),rs.getInt("mid")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    ArrayList<RowData> selectAllComments() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = cSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("mid"), rs.getInt("commentID"),rs.getString("userid"),rs.getString("datecreated"),rs.getString("comment")));
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
    RowData selectOneMessages(int id) {
        RowData res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowData(rs.getInt("msgid"), rs.getString("userid"),rs.getString("datecreated"), rs.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowData selectOneLikes(String id) {
        RowData res = null;
        try {
            lSelectOne.setString(1, id);
            ResultSet rs = lSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowData(rs.getString("userid"), rs.getInt("likes"),rs.getInt("mid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowData selectOneComments(String id) {
        RowData res = null;
        try {
            cSelectOne.setString(1, id);
            ResultSet rs = cSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowData(rs.getInt("mid"), rs.getInt("commentID"),rs.getString("userid"),rs.getString("datecreated"),rs.getString("comment"));
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
    int deleteRowMessages(int id) {
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
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteRowLikes(int id) {
        int res = -1;
        try {
            lDeleteOne.setInt(1, id);
            res = lDeleteOne.executeUpdate();
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
    int deleteRowComments(int id) {
        int res = -1;
        try {
            cDeleteOne.setInt(1, id);
            res = cDeleteOne.executeUpdate();
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
    int updateOneMessages(int id, String message) {
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
     * Update the Like for a row in the database
     * 
     * @param id The id of the row to update
     * @param message The new message contents
     * 
     * @return The number of rows that were updated.  -1 indicates an error.
     */
    int updateOneLikes(int like, int id) {
        int res = -1;
        try {
            lUpdateOne.setInt(1, like);
            lUpdateOne.setInt(2, id);
            res = lUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

        /**
     * Update the Comments for a row in the database
     * 
     * @param id The id of the row to update
     * @param message The new message contents
     * 
     * @return The number of rows that were updated.  -1 indicates an error.
     */
    int updateOneComments(String comment, int id) {
        int res = -1;
        try {
            cUpdateOne.setString(1, comment);
            cUpdateOne.setInt(2, id);
            res = lUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * Create tblData.  If it already exists, this will print an error
     */
    void createTableMessages() {
        try {
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        /**
     * Create tblData.  If it already exists, this will print an error
     */
    void createTableLikes() {
        try {
            lCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        /**
     * Create tblData.  If it already exists, this will print an error
     */
    void createTableComments() {
        try {
            cCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Remove tblData from the database.  If it does not exist, this will print
     * an error.
     */
    // void dropTable() {
    //     try {
    //         mDropTable.execute();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }
}

