package edu.lehigh.cse216.slj222.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.ArrayList;
//import java.util.Map;

/**
 * App is our basic admin app.  For now, it is a demonstration of the six key 
 * operations on a database: connect, insert, update, query, delete, disconnect
 */
public class App {

    /**
     * Print the menu for our program
     */
    static void menu() {
        System.out.println("Main Menu");
        System.out.println("  [M] Create message table");
        System.out.println("  [L] Create like table");
        System.out.println("  [C] Create comments table");
        System.out.println("  [D] Drop table is inactive");
        System.out.println("  [1] Query for a specific row from Messages");
        System.out.println("  [2] Query for a specific row from Likes");
        System.out.println("  [3] Query for a specific row from Comments");
        System.out.println("  [*] Query for all message rows");
        System.out.println("  [&] Query for all like rows");
        System.out.println("  [$] Query for all comment rows");
        System.out.println("  [-] Delete a message row");
        System.out.println("  [#] Delete a like row");
        System.out.println("  [^] Delete a comment row");
        System.out.println("  [+] Insert a new message row");
        System.out.println("  [@] Insert a new like row");
        System.out.println("  [!] Insert a new comment row");
        System.out.println("  [~] Update a message row");
        System.out.println("  [X] Update a like row");
        System.out.println("  [Z] Update a comment row");
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
    static char prompt(BufferedReader in) {
        // The valid actions:
        String actions = "MLCD123*&$-#^+@!~XZq?";

        // We repeat until a valid single-character option is selected        
        while (true) {
            System.out.print("[" + actions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
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


    /**x
     * Ask the user to enter a String message
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The string that the user provided.  May be "".
     */
    static String getString(BufferedReader in, String message) {
        String s;
        try {
            System.out.print(message + " :> ");
            s = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }

    /**
     * Ask the user to enter an integer
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The integer that the user provided.  On error, it will be -1
     */
    static int getInt(BufferedReader in, String message) {
        int i = -1;
        try {
            System.out.print(message + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * The main routine runs a loop that gets a request from the user and
     * processes it
     * 
     * @param argv Command-line options.  Ignored by this program.tryna smashyes should we leavr car at my house
     */
    public static void main(String[] argv) {
        // get the Postgres configuration from the environment
        //Map<String, String> env = System.getenv();
        // String ip = env.get("POSTGRES_IP");
        // String port = env.get("POSTGRES_PORT");
        // String user = env.get("POSTGRES_USER");
        // String pass = env.get("POSTGRES_PASS");

        //String db_url = env.get("DATABASE_URL"); 
        // Get a fully-configured connection to the database, or exit 
        // immediately
        Database db = Database.getDatabase("postgres://wbobgqxniofljr:0feb75c4741735e14f18ab72f07b94562d59741b2db3aae7ffbddbf2d4dd3e43@ec2-52-203-160-194.compute-1.amazonaws.com:5432/d7uf5dueelngct");
        if (db == null)
            return;

        // Start our basic command-line interpreter:
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); 
//messages
        try {


        while (true) {


            // Get the user's request, and do it
            //
            // NB: for better testability, each action should be a separate
            //     function call
            try {
                
            char action = prompt(in);
            if (action == '?') {
                menu();
            } else if (action == 'q') {
                break;
            } else if (action == 'M') {
                db.createTableMessages();
            } else if (action == 'L') {
                db.createTableLikes();
            } else if (action == 'C') {
                db.createTableComments();
            }else if (action == 'D') {
                //db.dropTable();
            } else if (action == '1') {
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                Database.RowData res = db.selectOneMessages(id);

                if (res != null) {
                    System.out.println("  [" + res.mMsgid + "] " + res.mMessage);
                    System.out.println("  --> " + res.mMessage);
                }
            } else if (action == '2') {
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                Database.RowData res = db.selectOneLikes(id);
                if (res != null) {
                    System.out.println("  [" + res.lMsgid + "] " + res.lLike);
                    System.out.println("  --> " + res.lLike);
                }
            } else if (action == '3') {
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                Database.RowData res = db.selectOneComments(id);
                if (res != null) {
                    System.out.println("  [" + res.cMsgid + "] " + res.cComment);
                    System.out.println("  --> " + res.cComment);
                }
            } else if (action == '*') {
                ArrayList<Database.RowData> res = db.selectAllMessgaes();
                if (res == null)
                    continue;
                System.out.println("  Current Message Table Contents");
                System.out.println("  -------------------------");
                for (Database.RowData rd : res) {
                    System.out.println("  [" + rd.mMsgid + "] " + "message: " + rd.mMessage + " date: " + rd.mDatecreated + " user id: " + rd.mUserid);
                }
            }else if (action == '&') {
                ArrayList<Database.RowData> res = db.selectAllLikes();
                if (res == null)
                    continue;
                System.out.println("  Current Like Table Contents");
                System.out.println("  -------------------------");
                for (Database.RowData rd : res) {
                    System.out.println("  [" + rd.lMsgid + "] " +  "user id: " + rd.lUserid + " like: " + rd.lLike);
                }
            }else if (action == '$') {
                ArrayList<Database.RowData> res = db.selectAllComments();
                if (res == null)
                    continue;
                System.out.println("  Current Comment Table Contents");
                System.out.println("  -------------------------");
                for (Database.RowData rd : res) {
                    System.out.println("  [" + rd.cMsgid + "] " + "comment: " + rd.cComment + " date: " + rd.cDatecreated + " user id: " + rd.cUserid);
                }
            } else if (action == '-') {
                //...same here 
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                int res = db.deleteRowMessages(id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows deleted");
            } else if (action == '#') {
                //...same here 
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                int res = db.deleteRowLikes(id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows deleted");
            } else if (action == '^') {
                //...same here 
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                int res = db.deleteRowComments(id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows deleted");
            } else if (action == '+') {
                //insert new row in messages table
                String id = getString(in, "Enter the userid");
                String message = getString(in, "Enter the message");
                if (message.equals(""))
                    continue;
                int res = db.insertRowMessages(id, message);
                System.out.println(res + " row added");
            }else if (action == '@') {
                //insert new row in likes table
                String id = getString(in, "Enter the userid");
                int like = getInt(in, "Enter like or dislike? (0 or 1)");
                // int mid = getInt(in, "Enter Msg id");
                int res = db.insertRowLikes(id,like);
                System.out.println(res + " row added");
            }else if (action == '!') {
                 //insert new row in comments table
                String id = getString(in, "Enter the userid");
                String comment = getString(in, " Enter the Comment");
                if (comment.equals(""))
                    continue;
                int res = db.insertRowComments(id ,comment);
                System.out.println(res + " row added");
            } else if (action == '~') {
                //...and here.
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                String newMessage = getString(in, "Enter the new message");
                int res = db.updateOneMessages(id, newMessage);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
            }else if (action == 'X') {
                //...and here.
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                int newLike = getInt(in, "Enter the new like");
                int res = db.updateOneLikes(id, newLike);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
            }else if (action == 'Z') {
                //...and here.
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                String newComment = getString(in, "Enter the new comment");
                int res = db.updateOneComments(newComment, id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
            }
            else  {
                System.out.println("Invalid");
            }
        } catch (Exception e ) {
            System.out.println(e);
        }
        }

        // Always remember to disconnect from the database when the program 
        // exits
        db.disconnect();
    } catch(Exception e) {
    System.out.println(e);
    }

}

}