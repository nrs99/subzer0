package edu.lehigh.cse216.slj222.admin;

import java.io.BufferedReader;
import java.sql.SQLException;

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

    public void execute() {
        char selection = App.prompt(br, options);
        String message;
        switch (selection) {
            case 'C':
                break;
            case '*':
                break;
            case '1':
                break;
            case '+':
                break;
            case '-':
                break;
            case '~':
                break;
            case 'D':
                break;
            case '?':
                break;
            default:
                System.out.println("Invalid");
        }
    }
    
}