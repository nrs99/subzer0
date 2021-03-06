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

    private PreparedStatement mInsertVote;

    private PreparedStatement mRemoveVote;

    private PreparedStatement mGetVote;

    private PreparedStatement mInsertComment;

    private PreparedStatement mEditComment;

    private PreparedStatement mGetComments;

    private PreparedStatement mMessagesByUser;

    private PreparedStatement mUserLikes;

    private PreparedStatement mInsertUser;

    private PreparedStatement mUserExists;

    private PreparedStatement mCommentAuthor;

    private PreparedStatement mInsertDocument;

    private PreparedStatement mInsertLink;

    private PreparedStatement checkFollow;

    private PreparedStatement newFollow;

    private PreparedStatement deleteFollow;

    private PreparedStatement deletePreferences;

    private PreparedStatement insertPreferences;

    private PreparedStatement getUserFromMsg;

    private PreparedStatement checkCommentPref;

    private PreparedStatement getUserEmail;

    private PreparedStatement getDisplayName;

    private PreparedStatement checkNewFollowPref;

    private PreparedStatement checkFollowPostPref;

    private PreparedStatement getFollowing;

    private PreparedStatement getPreferences;

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
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath()
                    + "?sslmode=require";
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
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO messages VALUES (default, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            db.mSelectOne = db.mConnection.prepareStatement(
                    "select messages.msgid, messages.userid, messages.datecreated, (select count(*) from likes where likes.mid = messages.msgid and likes = 1) as likes, (select count(*) from likes where likes.mid = messages.msgid and likes = -1) as dislikes, messages.message, (select count(*) from comments where comments.mid = messages.msgid) as comments, displayname, photourl, links.url as link, documents.fileid as fileid, documents.mime as mimetype from messages natural join users left join links on messages.msgid = links.msgid left join documents on messages.msgid = documents.msgid where messages.msgid = ?");
            db.mSelectAllNewest = db.mConnection.prepareStatement(
                    "select messages.msgid, messages.userid, messages.datecreated, (select count(*) from likes where likes.mid = messages.msgid and likes = 1) as likes, (select count(*) from likes where likes.mid = messages.msgid and likes = -1) as dislikes, messages.message, (select count(*) from comments where comments.mid = messages.msgid) as comments, displayname, photourl, links.url as link, documents.fileid as fileid, documents.mime as mimetype from messages natural join users left join links on messages.msgid = links.msgid left join documents on messages.msgid = documents.msgid ORDER BY datecreated DESC");
            db.mSelectAllOldest = db.mConnection.prepareStatement(
                    "select messages.msgid, messages.userid, messages.datecreated, (select count(*) from likes where likes.mid = messages.msgid and likes = 1) as likes, (select count(*) from likes where likes.mid = messages.msgid and likes = -1) as dislikes, messages.message, (select count(*) from comments where comments.mid = messages.msgid) as comments, displayname, photourl, links.url as link, documents.fileid as fileid, documents.mime as mimetype from messages natural join users left join links on messages.msgid = links.msgid left join documents on messages.msgid = documents.msgid ORDER BY datecreated ASC");
            db.mSelectAllPopular = db.mConnection.prepareStatement(
                    "select messages.msgid, messages.userid, messages.datecreated, (select count(*) from likes where likes.mid = messages.msgid and likes = 1) as likes, (select count(*) from likes where likes.mid = messages.msgid and likes = -1) as dislikes, messages.message, (select count(*) from comments where comments.mid = messages.msgid) as comments, displayname, photourl from messages natural join users ORDER BY (likes - dislikes) DESC");
            db.mInsertVote = db.mConnection.prepareStatement("INSERT INTO likes VALUES(?, ?, ?)");
            db.mRemoveVote = db.mConnection.prepareStatement("DELETE FROM likes where mid = ? and userid = ?");
            db.mGetVote = db.mConnection.prepareStatement("SELECT likes FROM likes WHERE userid = ? and mid = ?");
            db.mInsertComment = db.mConnection.prepareStatement("INSERT INTO comments VALUES (?, default, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            db.mEditComment = db.mConnection.prepareStatement("UPDATE comments SET comment = ? WHERE commentid = ?");
            db.mGetComments = db.mConnection
                    .prepareStatement("SELECT * from comments natural join users WHERE mid=? ORDER BY datecreated ASC");
            db.mMessagesByUser = db.mConnection.prepareStatement(
                    "select messages.msgid, messages.userid, messages.datecreated, (select count(*) from likes where likes.mid = messages.msgid and likes = 1) as likes, (select count(*) from likes where likes.mid = messages.msgid and likes = -1) as dislikes, messages.message, (select count(*) from comments where comments.mid = messages.msgid) as comments, displayname, photourl, links.url as link, documents.fileid as fileid, documents.mime as mimetype from messages natural join users left join links on messages.msgid = links.msgid left join documents on messages.msgid = documents.msgid WHERE userid = ? ORDER BY datecreated DESC");
            db.mUserLikes = db.mConnection.prepareStatement("SELECT mid, likes from likes where userid =?");
            db.mInsertUser = db.mConnection.prepareStatement("INSERT INTO users VALUES(?, ?, ?, ?)");
            db.mUserExists = db.mConnection.prepareStatement("SELECT * FROM users where userID = ?");
            db.mCommentAuthor = db.mConnection.prepareStatement("SELECT userid FROM comments WHERE commentid=?");
            db.mInsertDocument = db.mConnection.prepareStatement("INSERT INTO documents VALUES (?,?, ?)");
            db.mInsertLink = db.mConnection.prepareStatement("INSERT INTO links VALUES (?, ?)");
            db.checkFollow = db.mConnection.prepareStatement("SELECT * FROM following where usera = ? and userb = ?");
            db.newFollow = db.mConnection.prepareStatement("INSERT INTO following VALUES (?, ?)");
            db.deleteFollow = db.mConnection.prepareStatement("DELETE FROM following WHERE usera = ? and userb = ?");
            db.deletePreferences = db.mConnection.prepareStatement("DELETE FROM preferences where userid = ?");
            db.insertPreferences = db.mConnection.prepareStatement("INSERT INTO preferences VALUES(?, ?, ?, ?)");
            db.getUserFromMsg = db.mConnection.prepareStatement("SELECT userid FROM messages WHERE msgid = ?");
            db.checkCommentPref = db.mConnection
                    .prepareStatement("SELECT commentsonpost FROM preferences WHERE userid = ?");
            db.getUserEmail = db.mConnection.prepareStatement("SELECT email FROM users where userid = ?");
            db.getDisplayName = db.mConnection.prepareStatement("SELECT displayname FROM users where userid = ?");
            db.checkNewFollowPref = db.mConnection
                    .prepareStatement("SELECT followsme FROM preferences WHERE userid = ?");
            db.checkFollowPostPref = db.mConnection.prepareStatement(
                    "SELECT usera FROM following left join preferences on usera = userid where followingposts = true and userb = ?");
            db.getFollowing = db.mConnection.prepareStatement("SELECT userb FROM following where usera = ?");
            db.getPreferences = db.mConnection.prepareStatement("SELECT * FROM preferences where userid = ?");
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
     * Insert a row into the database
     *
     * @param message The message body in the new row
     * @param userId  The user ID of the person who is posting the message
     *
     * @return The number of rows that were inserted
     */
    int insertDocument(int msgid, String file_id, String mimeType) {
        int count = 0;
        try {
            mInsertDocument.setInt(1, msgid);
            mInsertDocument.setString(2, file_id);
            mInsertDocument.setString(3, mimeType);
            mInsertDocument.executeUpdate();
            count = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    int insertLink(int msgid, String url) {
        int count = 0;
        try {
            mInsertLink.setInt(1, msgid);
            mInsertLink.setString(2, url);
            mInsertLink.executeUpdate();
            count = 1;
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
                Message m = new Message(rs.getInt("msgId"), rs.getString("message"), rs.getString("userId"),
                        rs.getTimestamp("dateCreated"), rs.getInt("likes"), rs.getInt("dislikes"),
                        rs.getInt("comments"), rs.getString("displayName"), rs.getString("photoURL"),
                        rs.getString("link"));
                String fileID = rs.getString("fileID");
                if (fileID != null) {
                    m.setPhotoString(App.getFileEncoding(fileID), rs.getString("mimeType"));
                }
                res.add(m);
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
                Message m = new Message(rs.getInt("msgId"), rs.getString("message"), rs.getString("userId"),
                        rs.getTimestamp("dateCreated"), rs.getInt("likes"), rs.getInt("dislikes"),
                        rs.getInt("comments"), rs.getString("displayName"), rs.getString("photoURL"),
                        rs.getString("link"));
                String fileID = rs.getString("fileID");
                if (fileID != null) {
                    m.setPhotoString(App.getFileEncoding(fileID), rs.getString("mimeType"));
                }
                res.add(m);
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
                Message m = new Message(rs.getInt("msgId"), rs.getString("message"), rs.getString("userId"),
                        rs.getTimestamp("dateCreated"), rs.getInt("likes"), rs.getInt("dislikes"),
                        rs.getInt("comments"), rs.getString("displayName"), rs.getString("photoURL"),
                        rs.getString("link"));
                String fileID = rs.getString("fileID");
                if (fileID != null) {
                    m.setPhotoString(App.getFileEncoding(fileID), rs.getString("mimeType"));
                }
                res.add(m);
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
                Message m = new Message(rs.getInt("msgId"), rs.getString("message"), rs.getString("userId"),
                        rs.getTimestamp("dateCreated"), rs.getInt("likes"), rs.getInt("dislikes"),
                        rs.getInt("comments"), rs.getString("displayName"), rs.getString("photoURL"),
                        rs.getString("link"));
                String fileID = rs.getString("fileID");
                if (fileID != null) {
                    m.setPhotoString(App.getFileEncoding(fileID), rs.getString("mimeType"));
                }
                res = m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    int vote(int msgid, String userid, int like) {
        int val = 0;
        try {
            mGetVote.setString(1, userid);
            mGetVote.setInt(2, msgid);
            ResultSet rs = mGetVote.executeQuery();
            if (rs.next()) {
                val = rs.getInt("likes");
            }
            mRemoveVote.setInt(1, msgid); // Remove any votes if there are any
            mRemoveVote.setString(2, userid);
            mRemoveVote.executeUpdate();
            if (val != like) {
                mInsertVote.setString(1, userid);
                mInsertVote.setInt(2, like);
                mInsertVote.setInt(3, msgid);
                mInsertVote.executeUpdate();
            }
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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
            ResultSet rs = mInsertComment.getGeneratedKeys();
            if (rs.next()) {
                count += rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    int editComment(int commentid, String comment, String userid) {
        int count = 0;
        try {
            mCommentAuthor.setInt(1, commentid);
            ResultSet rs = mCommentAuthor.executeQuery();
            String author = "";
            if (rs.next()) {
                author = rs.getString("userid");
            }
            if (userid.equals(author)) {
                mEditComment.setInt(2, commentid);
                mEditComment.setString(1, comment);
                count = mEditComment.executeUpdate();
            }
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
            while (rs.next()) {
                comments.add(new Comment(rs.getInt("commentId"), rs.getInt("mid"), rs.getString("comment"),
                        rs.getString("userId"), rs.getTimestamp("dateCreated"), rs.getString("displayName"),
                        rs.getString("photoURL")));
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
                Message m = new Message(rs.getInt("msgId"), rs.getString("message"), rs.getString("userId"),
                        rs.getTimestamp("dateCreated"), rs.getInt("likes"), rs.getInt("dislikes"),
                        rs.getInt("comments"), rs.getString("displayName"), rs.getString("photoURL"),
                        rs.getString("link"));
                String fileID = rs.getString("fileID");
                if (fileID != null) {
                    m.setPhotoString(App.getFileEncoding(fileID), rs.getString("mimeType"));
                }
                res.add(m);
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    ArrayList<MyLikes> getMyLikes(String userID) {
        ArrayList<MyLikes> res = new ArrayList<MyLikes>();
        try {
            mUserLikes.setString(1, userID);
            ResultSet rs = mUserLikes.executeQuery();
            while (rs.next()) {
                res.add(new MyLikes(rs.getInt("mid"), rs.getInt("likes")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    int insertUser(String userID, String displayName, String photoURL, String email) {
        int count = 0;
        try {
            mUserExists.setString(1, userID);
            ResultSet rs = mUserExists.executeQuery();
            if (rs.next()) {
                count = 1;
            } else {
                mInsertUser.setString(1, userID);
                mInsertUser.setString(2, displayName);
                mInsertUser.setString(3, photoURL);
                mInsertUser.setString(4, email);
                mInsertUser.executeUpdate();
                count = 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return count;
    }

    int follow(String userA, String userB) {
        int count = 0;
        try {
            checkFollow.setString(1, userA);
            checkFollow.setString(2, userB);
            ResultSet rs = checkFollow.executeQuery();
            if (rs.next()) { // Already there
                deleteFollow.setString(1, userA);
                deleteFollow.setString(2, userB);
                deleteFollow.executeUpdate();
                count = 1;
            } else { // Not there
                newFollow.setString(1, userA);
                newFollow.setString(2, userB);
                newFollow.executeUpdate();
                count = 2;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    int changePreferences(String userid, boolean followsMe, boolean commentsOnPost, boolean followingPosts) {
        int count = 0;
        try {
            deletePreferences.setString(1, userid);
            deletePreferences.executeUpdate();
            insertPreferences.setString(1, userid);
            insertPreferences.setBoolean(2, followsMe);
            insertPreferences.setBoolean(3, commentsOnPost);
            insertPreferences.setBoolean(4, followingPosts);
            insertPreferences.executeUpdate();
            count = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    String newCommentEmail(int msgid) {
        try {
            getUserFromMsg.setInt(1, msgid);
            ResultSet rs = getUserFromMsg.executeQuery();
            rs.next();
            String userid = rs.getString(1);
            checkCommentPref.setString(1, userid);
            rs = checkCommentPref.executeQuery();
            if (rs.next()) {
                if (rs.getBoolean(1)) {
                    getUserEmail.setString(1, userid);
                    rs = getUserEmail.executeQuery();
                    rs.next();
                    return rs.getString(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    String newFollowEmail(String userB) {
        try {
            checkNewFollowPref.setString(1, userB);
            ResultSet rs = checkNewFollowPref.executeQuery();
            if (rs.next()) {
                if (rs.getBoolean(1)) {
                    getUserEmail.setString(1, userB);
                    rs = getUserEmail.executeQuery();
                    rs.next();
                    return rs.getString(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    ArrayList<String> followingPostsEmails(String userB) {
        ArrayList<String> emails = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        try {
            checkFollowPostPref.setString(1, userB);
            ResultSet rs = checkFollowPostPref.executeQuery();
            while (rs.next()) {
                ids.add(rs.getString(1));
            }
            for (int i = 0; i < ids.size(); i++) {
                getUserEmail.setString(1, ids.get(i));
                rs = getUserEmail.executeQuery();
                rs.next();
                emails.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emails;
    }

    String getDisplayName(String userid) {
        try {
            getDisplayName.setString(1, userid);
            ResultSet rs = getDisplayName.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    ArrayList<String> getFollowing(String usera) {
        ArrayList<String> ids = new ArrayList<>();
        try {
            getFollowing.setString(1, usera);
            ResultSet rs = getFollowing.executeQuery();
            while (rs.next()) {
                ids.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    Preference getPreferences(String userid) {
        try {
            getPreferences.setString(1, userid);
            ResultSet rs = getPreferences.executeQuery();
            if (rs.next()) {
                return new Preference(rs.getBoolean(2), rs.getBoolean(3), rs.getBoolean(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}