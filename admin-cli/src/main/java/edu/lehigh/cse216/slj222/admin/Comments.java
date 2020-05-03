package edu.lehigh.cse216.slj222.admin;

import java.io.BufferedReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Comments {
    /**
     * Inherit the database object
     */
    Database db;

    /**
     * Inherit the BufferedReader
     */
    BufferedReader br;
    /**
     * Create comments table
     */
    PreparedStatement createTable;
    /**
     * Select all rows from comments table
     */
    PreparedStatement selectAll;
    /**
     * Select a row from comments table
     */
    PreparedStatement selectOne;
    /**
     * Add a new comment
     */
    PreparedStatement addOne;
    /**
     * Delete a comment
     */
    PreparedStatement deleteOne;
    /**
     * Edit a comment
     */
    PreparedStatement updateOne;
    /**
     * Drop comments table
     */
    PreparedStatement dropTable;

    /**
     * Options that a user has to interact with comments table
     */
    final String options = "C*1+-~D?";

    public Comments(Database db, BufferedReader br) {
        this.db = db;
        this.br = br;

        try {
            createTable = db.Connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS comments(mid foreign key references messages(msgid) ON DELETE CASCADE, commentid SERIAL PRIMARY KEY, userid varchar(30) foreign key references users, datecreated timestamp, comment varchar(250))");
            selectAll = db.Connection.prepareStatement("SELECT * FROM comments ORDER BY commentid");
            selectOne = db.Connection.prepareStatement("SELECT * FROM comments where commentid = ?");
            addOne = db.Connection.prepareStatement("INSERT INTO comments VALUES (?, default, ?, ?, ?)");
            deleteOne = db.Connection.prepareStatement("DELETE FROM comments where commentid = ?");
            updateOne = db.Connection.prepareStatement("UPDATE comments SET comment = ? WHERE commentid = ?");
            dropTable = db.Connection.prepareStatement("DROP TABLE IF EXISTS comments");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create comments table
     */
    public void createTable() {
        try {
            createTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Select all rows from the comments table
     */
    public void selectAll() {
        try {
            ResultSet rs = selectAll.executeQuery();
            System.out.println("  Current Comments Table Contents");
            System.out.printf("%-5s %-8s %-25s %-25s Comment\n", "ID", "Msg ID", "User ID", "Date Created");
            System.out.println("  -------------------------");

            while (rs.next()) {
                System.out.printf("[%3d] %-8s %-25s %-25s %s\n", rs.getInt(2), rs.getInt(1), rs.getString(3),
                        rs.getString(4), rs.getString(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Select a specific row from the comments table
     */
    public void selectOne(int cid) {
        try {
            selectOne.setInt(1, cid);
            ResultSet rs = selectOne.executeQuery();
            System.out.printf("%-5s %-8s %-25s %-25s Comment\n", "ID", "Msg ID", "User ID", "Date Created");
            while (rs.next()) {
                System.out.printf("[%3d] %-8s %-25s %-25s %s\n", rs.getInt(2), rs.getInt(1), rs.getString(3),
                        rs.getString(4), rs.getString(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a message with a given msgid, user and comment
     */
    public void addOne(String userid, int msgid, String comment) {
        try {
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            addOne.setInt(1, msgid);
            addOne.setString(2, userid);
            addOne.setTimestamp(3, ts);
            addOne.setString(4, comment);
            addOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateOne(int cid, String comment) {
        try {
            updateOne.setString(1, comment);
            updateOne.setInt(2, cid);
            updateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete row with given cid
     */
    public void deleteOne(int cid) {
        try {
            deleteOne.setInt(1, cid);
            deleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Drop comments table
     */
    public void dropTable() {
        try {
            dropTable.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print menu for messages table
     */
    public void menu() {
        System.out.println("Comments Menu");
        System.out.println("  [C] Create comments table");
        System.out.println("  [*] Select all rows");
        System.out.println("  [1] Select a specific row");
        System.out.println("  [+] Add a comment");
        System.out.println("  [-] Delete a comment");
        System.out.println("  [~] Update a comment row");
        System.out.println("  [D] Drop comments table");
        System.out.println("  [?] Help - this menu");
    }

    /**
     * User interface for Messages Menu
     */
    public void execute() {
        char selection = App.prompt(br, options);
        int msgid, cid;
        switch (selection) {
            case 'C':
                createTable();
                break;
            case '*':
                selectAll();
                break;
            case '1':
                msgid = App.getInt(br, "Enter the message ID");
                selectOne(msgid);
                break;
            case '+':
                String user = App.getString(br, "Enter the user ID");
                msgid = App.getInt(br, "Enter associated message ID");
                String comment = App.getString(br, "Enter the new comment");
                addOne(user, msgid, comment);
                break;
            case '-':
                cid = App.getInt(br, "Enter the comment ID");
                deleteOne(cid);
                break;
            case '~':
                cid = App.getInt(br, "Enter the comment ID");
                String newComment = App.getString(br, "Enter your new comment");
                updateOne(cid, newComment);
                break;
            case 'D':
                dropTable();
                break;
            case '?':
                menu();
                execute();
                break;
            default:
                System.out.println("Invalid");
        }
    }
}