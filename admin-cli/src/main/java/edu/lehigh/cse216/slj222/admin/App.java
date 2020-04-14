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
        System.out.println("  [N] Create documents table");
        System.out.println("  [B] Create link table");                                  //B
        System.out.println("  [D] Drop table is inactive");
        System.out.println("  [1] Query for a specific row from Messages");
        System.out.println("  [2] Query for a specific row from Likes");
        System.out.println("  [3] Query for a specific row from Comments");
        System.out.println("  [4] Query for a specific row from Documents");
        System.out.println("  [5] Query for a specific row from Documents");            //5
        System.out.println("  [*] Query for all message rows");
        System.out.println("  [&] Query for all like rows");
        System.out.println("  [$] Query for all comment rows");
        System.out.println("  [%] Query for all document rows");
        System.out.println("  [_] Query for all link rows");                           //_
        System.out.println("  [-] Delete a message row");
        System.out.println("  [#] Delete a like row");
        System.out.println("  [^] Delete a comment row");
        System.out.println("  [(] Delete the last document row");
        System.out.println("  [V] Delete a document row");
        System.out.println("  [=] Delete the last link row");                      //=
        System.out.println("  [K] Delete a link row");                             //K
        System.out.println("  [+] Insert a new message row");
        System.out.println("  [@] Insert a new like row");
        System.out.println("  [!] Insert a new comment row");
        System.out.println("  [)] Insert a new document row");
        System.out.println("  [I] Insert a new link row");                              //I
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
    static char prompt(final BufferedReader in) {
        // The valid actions:
        final String actions = "MLCND1234*&$%-#^(+@!)~XZVqB5_KI=?";

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


    /**x
     * Ask the user to enter a String message
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The string that the user provided.  May be "".
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
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The integer that the user provided.  On error, it will be -1
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
     * The main routine runs a loop that gets a request from the user and
     * processes it
     * 
     * @param argv Command-line options.  Ignored by this program.tryna smashyes should we leavr car at my house
     */
    public static void main(final String[] argv) {
        // get the Postgres configuration from the environment
        //Map<String, String> env = System.getenv();
        // String ip = env.get("POSTGRES_IP");
        // String port = env.get("POSTGRES_PORT");
        // String user = env.get("POSTGRES_USER");
        // String pass = env.get("POSTGRES_PASS");

        //String db_url = env.get("DATABASE_URL"); 
        // Get a fully-configured connection to the database, or exit 
        // immediately
        final Database db = Database.getDatabase("postgres://wbobgqxniofljr:0feb75c4741735e14f18ab72f07b94562d59741b2db3aae7ffbddbf2d4dd3e43@ec2-52-203-160-194.compute-1.amazonaws.com:5432/d7uf5dueelngct");
        if (db == null)
            return;

        // Start our basic command-line interpreter:
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); 
//messages
        try {


        while (true) {

            // Get the user's request, and do it
            //
            // NB: for better testability, each action should be a separate
            //     function call
            try {
                
            final char action = prompt(in);               //get the option
            
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
            }else if (action == 'B') {
                db.createTableLink();
            }else if (action == 'D') {
                db.dropTable();
            } else if (action == '1') {
                final int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                final Database.RowData res = db.selectOneMessages(id);

                if (res != null) {
                    System.out.println("  [" + res.mMsgid + "] " + res.mMessage);
                    System.out.println("  --> " + res.mMessage);
                }
            } else if (action == '2') {
                final String id = getString(in, "Enter the user ID");
                if (id.equals(""))
                    continue;
                final Database.RowData res = db.selectOneLikes(id);
                if (res != null) {
                    System.out.println("  [" + res.lMsgid + "] " +  "user id: " + res.lUserid + " like: "+res.lLikes);
                    System.out.println("  --> " + res.lLikes);
                }
            } else if (action == '3') {
                final String comment = getString(in, "Enter Comment");
                if (comment.equals(""))
                    continue; 
                final Database.RowData res = db.selectOneComments(comment);
                if (res != null) {
                    System.out.println("  [" + res.cMsgid + "] " + res.cComment);
                    System.out.println("  --> " + res.cComment);
                }
            } else if (action == '4') {
                final int id = getInt(in, "Enter the user ID");         //???? userid or row id??
                if (id == -1)
                    continue;
                final Database.RowData res = db.selectOneDocument(id);

                if (res != null) {
                    System.out.println("  [" + res.dMsgid + "] " + res.documentURL);
                    System.out.println("  --> " + res.documentURL);
                }
            }else if (action == '*') {
                final ArrayList<Database.RowData> res = db.selectAllMessgaes();
                if (res == null)
                    continue;
                System.out.println("  Current Message Table Contents");
                System.out.println("  -------------------------");
                for (final Database.RowData rd : res) {
                    System.out.println("  [" + rd.mMsgid + "] " + "message: " + rd.mMessage + " date: " + rd.mDatecreated + " user id: " + rd.mUserid);
                }
            }else if (action == '&') {
                final ArrayList<Database.RowData> res = db.selectAllLikes();
                if (res == null)
                    continue;
                System.out.println("  Current Like Table Contents");
                System.out.println("  -------------------------");
                for (final Database.RowData rd : res) {
                    System.out.println("  [" + rd.lMsgid + "] " +  "user id: " + rd.lUserid + " like: " + rd.lLikes);
                }
            }else if (action == '$') {
                final ArrayList<Database.RowData> res = db.selectAllComments();
                if (res == null)
                    continue;
                System.out.println("  Current Comment Table Contents");
                System.out.println("  -------------------------");
                for (final Database.RowData rd : res) {
                    System.out.println("  [" + rd.cMsgid + "] " + "comment: " + rd.cComment + " date: " + rd.cDatecreated + " user id: " + rd.cUserid);
                }
            } else if (action == '-') {
                //...same here 
                final int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                final int res = db.deleteRowMessages(id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows deleted");
            } else if (action == '#') {
                //...same here 
                final int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                final int res = db.deleteRowLikes(id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows deleted");
            } else if (action == '^') {
                //...same here 
                final int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                final int res = db.deleteRowComments(id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows deleted");
            } else if (action == '+') {
                //insert new row in messages table
                final String id = getString(in, "Enter the userid");
                final String message = getString(in, "Enter the message");
                if (message.equals(""))
                    continue;
                final int res = db.insertRowMessages(id, message);
                System.out.println(res + " row added");
            }else if (action == '@') {
                //insert new row in likes table
                final String id = getString(in, "Enter the userid");
                final int like = getInt(in, "Enter like or dislike? (0 or 1)");
                // int mid = getInt(in, "Enter Msg id");
                final int res = db.insertRowLikes(id,like);
                System.out.println(res + " row added");
            }else if (action == '!') {
                 //insert new row in comments table
                final String id = getString(in, "Enter the userid");
                final String comment = getString(in, " Enter the Comment");
                if (comment.equals(""))
                    continue;
                final int res = db.insertRowComments(id ,comment);
                System.out.println(res + " row added");
            } else if (action == '~') {
                    //...and here.
                    final int id = getInt(in, "Enter the row ID :> ");
                    if (id == -1)
                        continue;
                    final String newMessage = getString(in, "Enter the new message");
                    final int res = db.updateOneMessages(id, newMessage);
                    if (res == -1)
                        continue;
                    System.out.println("  " + res + " rows updated");
            }else if (action == 'X') {
                //...and here.
                final int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                final int newLike = getInt(in, "Enter the new like");
                final int res = db.updateOneLikes(id, newLike);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
            }else if (action == 'Z') {
                //...and here.
                final int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                final String newComment = getString(in, "Enter the new comment");
                final int res = db.updateOneComments(newComment, id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
            }
            
            else if(action == 'N') {           //Create a document table
                db.createTableDocuments();
            }else if(action == '%') {                                    //Query for all document rows     
                final ArrayList<Database.RowData> res = db.selectAllDocuments();
                if (res == null)
                    continue;
                System.out.println("  Current Document Table Contents");
                System.out.println("  -------------------------");
                for (final Database.RowData rd : res) {
                    System.out.println("  [" + rd.dMsgid + "] "  + " date: " + rd.dDatecreated + " user id: " + rd.dUserid + " document url: " + rd.documentURL);
                }
            }else if(action == '_') {                                    //Query for all link rows     
                final ArrayList<Database.RowData> res = db.selectAllLinks();
                if (res == null)
                    continue;
                System.out.println("  Current Link Table Contents");
                System.out.println("  -------------------------");
                for (final Database.RowData rd : res) {
                   System.out.println("  [" + rd.linkMsgid + "] "  + " date: " + rd.linkDateCreated + " user id: " + rd.linkUserid + " document url: " + rd.linkUrl);
                    //System.out.println("  [" + rd.mMsgid + "] "  + " date: " + rd.mDatecreated + " user id: " + rd.mUserid + " document url: " + rd.mMessage);
                }
            }else if(action == '(') {           //Delete a document row
                //int id = getInt(in, "Enter the row ID");
                //if (id == -1)
                //    continue;
                final int res = db.deleteRowDocument();
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows deleted");
            }else if(action == '=') {           //Delete a link row
                //int id = getInt(in, "Enter the row ID");
                //if (id == -1)
                //    continue;
                final int res = db.deleteRowLink();
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows deleted");
            }else if(action == ')') {           //Insert a document row
                //insert new row in documents table
                final String id = getString(in, "Enter the userid");
                final String document = getString(in, "Enter the document url");
                if (document.equals(""))
                    continue;
                final int res = db.insertRowDocument(id, document);
                System.out.println(res + " row added");
            }else if(action == 'I') {           //Insert a link row
                //insert new row in documents table
                final String id = getString(in, "Enter the userid");
                final String link = getString(in, "Enter the link url");
                if (link.equals(""))
                    continue;
                final int res = db.insertRowDocument(id, link);
                System.out.println(res + " row added");
            }else if(action == 'V') {           //delete a document row
                final int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                final int res = db.deleteRowDocument(id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows deleted");
            }else if(action == 'K') {           //delete a link row
                final int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                final int res = db.deleteRowLink(id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows deleted");
            }
            else  {
                System.out.println("Invalid");
            }
        } catch (final Exception e ) {
            System.out.println(e);
        }
        }

        // Always remember to disconnect from the database when the program 
        // exits
        db.disconnect();
    } catch(final Exception e) {
    System.out.println(e);
    }

}

}