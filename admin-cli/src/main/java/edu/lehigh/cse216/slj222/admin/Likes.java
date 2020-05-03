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

    public Likes(Database db, BufferedReader br) {
        this.db = db;
        this.br = br;

        try {
            createTable = db.Connection.prepareStatement("");
            selectAll = db.Connection.prepareStatement("SELECT * FROM LIKES");
            selectOne = db.Connection.prepareStatement("");
            addOne = db.Connection.prepareStatement("");
            updateOne = db.Connection.prepareStatement("");
            deleteOne = db.Connection.prepareStatement("");
            dropTable = db.Connection.prepareStatement("DROP TABLE IF EXISTS likes");
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

    }
}