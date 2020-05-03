package edu.lehigh.cse216.slj222.admin;

import java.io.BufferedReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Likes {
    /**
     * Inherit the database object
     */
    Database db;

    /**
     * Inherit the BufferedReader
     */
    BufferedReader br;

    /**
     * Create likes table
     */
    PreparedStatement createTable;
    /**
     * Select all from likes table
     */
    PreparedStatement selectAll;
    /**
     * Select one row from likes table
     */
    PreparedStatement selectOne;
    /**
     * Add a like or dislike to the table
     */
    PreparedStatement addOne;
    /**
     * Switch from a like to a dislike, or vice-versa
     */
    PreparedStatement updateOne;
    /**
     * Delete a like/dislike
     */
    PreparedStatement deleteOne;
    /**
     * Drop likes table
     */
    PreparedStatement dropTable;
    /**
     * Options that a user has to interact with likes table
     */
    final String options = "C*1+-~D?";

    public Likes(Database db, BufferedReader br) {
        this.db = db;
        this.br = br;

        try {
            createTable = db.Connection.prepareStatement(
                    "CREATE TABLE likes (userid varchar(30) references users, likes int, mid int foreign key references messages(msgid), primary key (userid, mid))");
            selectAll = db.Connection.prepareStatement("SELECT * FROM likes ORDER BY mid, userid");
            selectOne = db.Connection.prepareStatement("SELECT * FROM likes where userid = ? and mid = ?");
            addOne = db.Connection.prepareStatement("INSERT INTO likes values(");
            updateOne = db.Connection
                    .prepareStatement("UPDATE likes SET likes = likes * -1 WHERE userid = ? and mid = ?");
            deleteOne = db.Connection.prepareStatement("DELETE from likes where userid = ? and mid = ?");
            dropTable = db.Connection.prepareStatement("DROP TABLE IF EXISTS likes");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create likes table
     */
    public void createTable() {
        try {
            createTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Select all rows from likes table
     */
    public void selectAll() {
        try {
            ResultSet rs = selectAll.executeQuery();
            System.out.println("  Current Likes Table Contents");
            System.out.printf("%-25s %-10s Like\n", "User ID", "Message ID");
            System.out.println("  -------------------------");

            while (rs.next()) {
                System.out.printf("%-25s %10d %s\n", rs.getString(1), rs.getInt(3), rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Select one row from likes table
     */
    public void selectOne(int msgid, String userid) {
        try {
            selectOne.setString(1, userid);
            selectOne.setInt(2, msgid);
            ResultSet rs = selectOne.executeQuery();
            System.out.printf("%-25s %-10s Like\n", "User ID", "Message ID");
            while (rs.next()) {
                System.out.printf("%-25s %10d %s\n", rs.getString(1), rs.getInt(3), rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add one row to likes table
     */
    public void addOne(int msgid, String userid, int like) {
        try {
            addOne.setString(1, userid);
            addOne.setInt(2, like);
            addOne.setInt(3, msgid);
            addOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Switch a like to a dislike or vice versa
     * 
     * @param msgid
     * @param userid
     */
    public void updateOne(int msgid, String userid) {
        try {
            updateOne.setString(1, userid);
            updateOne.setInt(2, msgid);
            updateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a row from likes table (removing a like/dislike)
     */
    public void deleteOne(int msgid, String userid) {
        try {
            deleteOne.setString(1, userid);
            deleteOne.setInt(2, msgid);
            deleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Drop likes table
     */
    public void dropTable() {
        try {
            dropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print menu for likes table
     */
    public void menu() {
        System.out.println("Likes Menu");
        System.out.println("  [C] Create likes table");
        System.out.println("  [*] Select all rows");
        System.out.println("  [1] Select a specific row");
        System.out.println("  [+] Add a like/dislike");
        System.out.println("  [-] Delete a like/dislike");
        System.out.println("  [~] Switch a like/dislike");
        System.out.println("  [D] Drop likes table");
        System.out.println("  [?] Help - this menu");
    }

    public void execute() {
        char selection = App.prompt(br, options);
        int msgid;
        String userid;
        switch (selection) {
            case 'C':
                createTable();
                break;
            case '*':
                selectAll();
                break;
            case '1':
                msgid = App.getInt(br, "Enter the message ID");
                userid = App.getString(br, "Enter the user ID");
                selectOne(msgid, userid);
                break;
            case '+':
                msgid = App.getInt(br, "Enter the message ID");
                userid = App.getString(br, "Enter the user ID");
                int like = App.getInt(br, "Enter 1 for a like, -1 for a dislike");
                addOne(msgid, userid, like);
                break;
            case '-':
                msgid = App.getInt(br, "Enter the message ID");
                userid = App.getString(br, "Enter the user ID");
                deleteOne(msgid, userid);
                break;
            case '~':
                msgid = App.getInt(br, "Enter the message ID");
                userid = App.getString(br, "Enter the user ID");
                updateOne(msgid, userid);
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