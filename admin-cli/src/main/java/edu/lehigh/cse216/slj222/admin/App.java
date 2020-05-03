package edu.lehigh.cse216.slj222.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.ArrayList;

/**
 * App is our basic admin app. For now, it is a demonstration of the six key
 * operations on a database: connect, insert, update, query, delete, disconnect
 */
public class App {

    /**
     * Print the menu for our program
     */
    static void menu() {
        System.out.println("Main Menu");
        System.out.println("  [M] Messages Menu");
        System.out.println("  [L] Links Menu");
        System.out.println("  [C] Comments Menu");
        System.out.println("  [D] Documents Menu");
        System.out.println("  [P] Preferences Menu");
        System.out.println("  [F] Following Menu");
        System.out.println("  [K] Likes Menu");
        System.out.println("  [U] Users menu");
        System.out.println("  [E] Send an email with SendGrid");
        System.out.println("  [q] Quit Program");
        System.out.println("  [?] Help (this message)");
    }

    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * 
     * @return The character corresponding to the chosen menu option
     */
    static char prompt(final BufferedReader in, String actions) {

        // We repeat until a valid single-character option is selected
        while (true) {
            System.out.print("[" + actions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (final IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    /**
     * x Ask the user to enter a String message
     * 
     * @param in      A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The string that the user provided. May be "".
     */
    static String getString(final BufferedReader in, final String message) {
        String s;
        try {
            System.out.print(message + " :> ");
            s = in.readLine();
        } catch (final IOException e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }

    /**
     * Ask the user to enter an integer
     * 
     * @param in      A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The integer that the user provided. On error, it will be -1
     */
    static int getInt(final BufferedReader in, final String message) {
        int i = -1;
        try {
            System.out.print(message + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * The main routine runs a loop that gets a request from the user and processes
     * it
     * 
     * @param argv Command-line options. Ignored by this program.
     */
    public static void main(final String[] argv) {

        // Get a fully-configured connection to the database, or exit
        // immediately
        final Database db = Database.getDatabase(
                "postgres://wbobgqxniofljr:0feb75c4741735e14f18ab72f07b94562d59741b2db3aae7ffbddbf2d4dd3e43@ec2-52-203-160-194.compute-1.amazonaws.com:5432/d7uf5dueelngct");
        if (db == null)
            return;

        // Start our basic command-line interpreter:
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        // Add menus to the app
        Comments comments = new Comments(db, in);
        Documents documents = new Documents(db, in);
        Following following = new Following(db, in);
        Likes likes = new Likes(db, in);
        Links links = new Links(db, in);
        Messages messages = new Messages(db, in);
        Users users = new Users(db, in);
        Preferences preferences = new Preferences(db, in);
        try {

            while (true) {

                // Get the user's request, and do it
                //
                // NB: for better testability, each action should be a separate
                // function call
                try {

                    final char action = prompt(in, "MLCDPFUKEq?"); // get the option

                    if (action == '?') {
                        menu();
                    } else if (action == 'q') {
                        break;
                    } else if (action == 'M') {
                        messages.execute();
                    } else if (action == 'L') {
                        links.execute();
                    } else if (action == 'C') {
                        comments.execute();
                    } else if (action == 'D') {
                        documents.execute();
                    } else if (action == 'P') {
                        preferences.execute();
                    } else if (action == 'F') {
                        following.execute();
                    } else if (action == 'U') {
                        users.execute();
                    } else if (action == 'K') {
                        likes.execute();
                    } else if (action == 'E') {
                        final String email = getString(in, "Enter the email address you'd like to send to");
                        final String subject = getString(in, "Enter the subject of the message");
                        final String content = getString(in, "Enter the content of the message");
                        SendGridEmail.sendEmail(email, subject, content);
                    } else {
                        System.out.println("Invalid");
                    }
                } catch (final Exception e) {
                    System.out.println(e);
                }
            }

            // Always remember to disconnect from the database when the program
            // exits
            db.disconnect();
        } catch (final Exception e) {
            System.out.println(e);
        }

    }

}