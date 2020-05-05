package edu.lehigh.cse216.slj222.admin;

import java.io.BufferedReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Messages {

    /**
     * Inherit the database object
     */
    Database db;

    /**
     * Inherit the BufferedReader
     */
    BufferedReader br;

    /**
     * Create messages table
     */
    PreparedStatement createTable;
    /**
     * Select all messages in table
     */
    PreparedStatement selectAll;
    /**
     * Select one message from table
     */
    PreparedStatement selectOne;
    /**
     * Add a message to the table
     */
    PreparedStatement addOne;
    /**
     * Delete a message from the table
     */
    PreparedStatement deleteOne;
    /**
     * Update a message row
     */
    PreparedStatement updateOne;
    /**
     * Drop the messages table
     */
    PreparedStatement dropTable;

    /**
     * Options that a user has to interact with messages table
     */
    final String options = "C*1+-~D?";

    /**
     * Create a messages object and set up PreparedStatements
     */
    public Messages(Database db, BufferedReader br) {
        this.db = db;
        this.br = br;
        try {
            createTable = db.Connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS messages(msgid SERIAL PRIMARY KEY, userid VARCHAR(30) NOT NULL, datecreated TIMESTAMP, message VARCHAR(250) NOT NULL)");
            selectAll = db.Connection.prepareStatement("SELECT * from messages");
            selectOne = db.Connection.prepareStatement("SELECT * from messages where msgid = ?");
            addOne = db.Connection.prepareStatement(
                    "INSERT INTO messages(msgid, userid, datecreated, message) VALUES (default, ?, ?, ?)");
            deleteOne = db.Connection.prepareStatement("DELETE FROM messages WHERE msgid = ?");
            updateOne = db.Connection.prepareStatement("UPDATE messages SET message = ? WHERE msgid = ?");
            dropTable = db.Connection.prepareStatement("DROP TABLE if exists messages");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create messages table
     */
    public void createTable() {
        try {
            createTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Select all rows from the messages table
     */
    public void selectAll() {
        try {
            ResultSet rs = selectAll.executeQuery();
            System.out.println("  Current Message Table Contents");
            System.out.printf("%-5s %-30s %-25s Message\n", "ID", "User ID", "Date Created");
            System.out.println("  -------------------------");

            while (rs.next()) {
                System.out.printf("[%3d] %-30s %-25s %s\n", rs.getInt("msgid"), rs.getString("userid"),
                        rs.getString("dateCreated"), rs.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Select a specific row from the messages table
     */
    public void selectOne(int msgid) {
        try {
            selectOne.setInt(1, msgid);
            ResultSet rs = selectOne.executeQuery();
            System.out.printf("%-5s %-30s %-25s Message\n", "ID", "User ID", "Date Created");
            while (rs.next()) {
                System.out.printf("[%3d] %-30s %-25s %s\n", rs.getInt("msgid"), rs.getString("userid"),
                        rs.getString("dateCreated"), rs.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a message with a given user and message
     */
    public void addOne(String userid, String msg) {
        try {
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            addOne.setString(1, userid);
            addOne.setTimestamp(2, ts);
            addOne.setString(3, msg);
            addOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete row with given msgid
     */
    public void deleteOne(int msgid) {
        try {
            deleteOne.setInt(1, msgid);
            deleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Drop messages table
     */
    public void dropTable() {
        try {
            dropTable.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update a row with given msgid with new msg
     */
    public void updateOne(int msgid, String msg) {
        try {
            updateOne.setString(1, msg);
            updateOne.setInt(2, msgid);
            updateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print menu for messages table
     */
    public void menu() {
        System.out.println("Messages Menu");
        System.out.println("  [C] Create messages table");
        System.out.println("  [*] Select all rows");
        System.out.println("  [1] Select a specific row");
        System.out.println("  [+] Add a message");
        System.out.println("  [-] Delete a message");
        System.out.println("  [~] Update a message row");
        System.out.println("  [D] Drop messages table");
        System.out.println("  [?] Help - this menu");
    }

    /**
     * User interface for Messages Menu
     */
    public void execute() {
        char selection = App.prompt(br, options);
        int msgid;
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
                String message = App.getString(br, "Enter the new message");
                addOne(user, message);
                break;
            case '-':
                msgid = App.getInt(br, "Enter the message ID");
                deleteOne(msgid);
                break;
            case '~':
                msgid = App.getInt(br, "Enter the message ID");
                String newmsg = App.getString(br, "Enter your new message");
                updateOne(msgid, newmsg);
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