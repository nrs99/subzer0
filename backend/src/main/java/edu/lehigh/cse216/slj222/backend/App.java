package edu.lehigh.cse216.slj222.backend;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

// Import the Spark package, so that we can make use of the "get" function to
// create an HTTP GET route
import spark.Spark;

// Import Google's JSON library
import com.google.gson.*;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
// Google OAuth Imports
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.common.io.Files;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * For now, our app creates an HTTP server that can only get and add data.
 */
public class App {

    private static final String APPLICATION_NAME = "Subzer0";
    private static final com.google.api.client.json.JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    // private static final String CREDENTIALS_FILE_PATH =
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    public static Drive service;

    public static void main(String[] args) throws IOException, GeneralSecurityException {// easy fix. probably not good
                                                                                         // long term.

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME).build(); // Build a new authorized API client service

        // get the Postgres configuration from the environment
        Map<String, String> env = System.getenv();

        String db_url = env.get("DATABASE_URL");

        Hashtable<UUID, String> ht = new Hashtable<>();

        System.out.println("db_url value: " + db_url);

        // Get a fully-configured connection to the database, or exit
        // immediately
        Database db = Database.getDatabase(db_url);
        if (db == null)
            return;

        // gson provides us with a way to turn JSON into objects, and objects
        // into JSON.
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe. See
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();

        // dataStore holds all of the data that has been provided via HTTP
        // requests
        //
        // NB: every time we shut down the server, we will lose all data, and
        // every time we start the server, we'll have an empty dataStore,
        // with IDs starting over from 0.

        Spark.port(getIntFromEnv("PORT", 4567));
        // Set up the location for serving static files. If the STATIC_LOCATION
        // environment variable is set, we will serve from it. Otherwise, serve
        // from "/web"
        String static_location_override = System.getenv("STATIC_LOCATION");
        if (static_location_override == null) {
            Spark.staticFileLocation("/web");
        } else {
            Spark.staticFiles.externalLocation(static_location_override);
        }

        String cors_enabled = env.get("CORS_ENABLED");
        if (cors_enabled.equals("True")) {
            final String acceptCrossOriginRequestsFrom = "*";
            final String acceptedCrossOriginRoutes = "GET,PUT,POST,DELETE,OPTIONS";
            final String supportedRequestHeaders = "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin";
            enableCORS(acceptCrossOriginRequestsFrom, acceptedCrossOriginRoutes, supportedRequestHeaders);
        }

        // Set up a route for serving the main page
        Spark.get("/", (req, res) -> {
            res.redirect("/index.html");
            return "";
        });

        // GET route that returns all message titles and Ids.
        // Standard call will have newest messages first
        Spark.get("/messages", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            // response.type("application/json");
            // String key = "mSelectAllNewest";

            // StructuredResponse results = mc.get(key);
            // if (results == null) {
            // results = new StructuredResponse("ok", null, db.selectAllNewest());
            // mc.set(key, 0, results);//check values later.
            // }
            // return gson.toJson(results);
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllNewest()));
        });

        // GET route that returns all message titles and Ids.
        // Oldest messages first
        Spark.get("/messages/oldest", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllOldest()));
        });

        // GET route that returns all message titles and Ids.
        // Most popular messages first
        Spark.get("/messages/popular", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllPopular()));
        });

        // GET route that returns everything for a single row in the DataStore.
        // The ":id" suffix in the first parameter to get() becomes
        // request.params("id"), so that we can get the requested row ID. If
        // ":id" isn't a number, Spark will reply with a status 500 Internal
        // Server Error. Otherwise, we have an integer, and the only possible
        // error is that it doesn't correspond to a row with data.
        Spark.get("/messages/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            Object data = db.selectOne(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        // POST route for adding a new element to the DataStore. This will read
        // JSON from the body of the request, turn it into a SimpleRequest
        // object, extract the title and message, insert them, and return the
        // ID of the newly created row.
        Spark.post("/messages", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int newId = db.insertRow(req.message, req.userID);
            ArrayList<String> emails = db.followingPostsEmails(req.userID);
            String displayName = db.getDisplayName(req.userID);
            for (int i = 0; i < emails.size(); i++) {
                SendGridEmail.sendEmail(emails.get(i), displayName + " posted a new message!", req.message);
            }
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                if (req.link != null) {
                    db.insertLink(newId, req.link);
                }
                if (req.photoURL != null) {
                    String fileID = uploadImage(req.photoURL, service, newId, req.mimeType);
                    db.insertDocument(newId, fileID, req.mimeType);
                }

                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        // Spark put route to like a message with a given id
        Spark.put("/messages/:id/like/:user", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("id"));
            String user = request.params("user");
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = db.vote(idx, user, 1);
            if (result == 0) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, req));
            }
        });

        // Spark put route to dislike a message with a given id
        Spark.put("/messages/:id/dislike/:user", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("id"));
            String user = request.params("user");
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = db.vote(idx, user, -1);
            if (result == 0) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, req));
            }
        });

        Spark.get("/messages/:id/comments", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.getComments(idx)));
        });

        Spark.post("/comments", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            NewCommentRequest req = gson.fromJson(request.body(), NewCommentRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");

            // NB: createEntry checks for null title and message
            int newId = db.insertComment(req.msgId, req.comment, req.userId);// would i do something here?
            String email = db.newCommentEmail(req.msgId);
            if (email != null) {
                String displayName = db.getDisplayName(req.userId);
                SendGridEmail.sendEmail(email, displayName + " Commented On Your Post!", req.comment);
            }
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        Spark.put("/comments/edit", (request, response) -> {

            EditCommentRequest req = gson.fromJson(request.body(), EditCommentRequest.class);

            response.status(200);
            response.type("application/json");

            int result = db.editComment(req.cid, req.comment, req.userid);

            if (result == 1) {
                return gson.toJson(new StructuredResponse("ok", null, result));
            } else {
                return gson.toJson(new StructuredResponse("error", "error updating comment", null));
            }

        });

        Spark.get("/messages/user/:id", (request, response) -> {
            String userID = request.params("id");
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllByUser(userID)));
        });

        Spark.get("/likes/:id", (request, response) -> {
            String userID = request.params("id");
            response.status(200);
            response.type("applicaiton/json");
            return gson.toJson(new StructuredResponse("ok", null, db.getMyLikes(userID)));
        });

        Spark.get("/users/:id/following", (request, response) -> {
            String userID = request.params("id");
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.getFollowing(userID)));
        });

        Spark.get("/users/:id/preferences", (request, response) -> {
            String userID = request.params("id");
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.getPreferences(userID)));
        });
 
        Spark.post("/login/:token", (request, response) -> {
            final String CLIENT_ID = "363085709256-vl89523mj1pv792ngp4sin2e717motg7.apps.googleusercontent.com";
            // final String CLIENT_SECRET = "zXSIfOxfMUoHugSkfaPKdBtk";

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                    new JacksonFactory())
                            // Specify the CLIENT_ID of the app that accesses the backend:
                            .setAudience(Collections.singletonList(CLIENT_ID)).build();

            String idTokenString = request.params("token");

            GoogleIdToken idToken = verifier.verify(idTokenString);

            Payload payload = new Payload();

            if (idToken != null) {
                payload = idToken.getPayload();
            }
            if (payload.getHostedDomain().equals("lehigh.edu")) {
                // Print user identifier
                String userID = payload.getSubject();

                UUID sessionKey = UUID.randomUUID();

                if (ht.contains(userID)) {
                    ht.put(sessionKey, userID);
                } else {
                    ht.put(sessionKey, userID);
                }

                return gson.toJson(new StructuredResponse("ok", null, sessionKey));
            } else {
                System.out.println("Invalid domain");
                return gson.toJson(new StructuredResponse("error", "login error", null));
            }
        });

        Spark.put("/user", (request, response) -> {
            NewUserRequest req = gson.fromJson(request.body(), NewUserRequest.class);
            response.status(200);
            response.type("application/json");
            int newId = db.insertUser(req.userID, req.displayName, req.photoURL, req.email);
            if (newId == 0) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }

        });
        
        Spark.put("/follow", (request, response) -> {
            FollowRequest req = gson.fromJson(request.body(), FollowRequest.class);
            response.status(200);
            response.type("application/json");
            int attemptFollow = db.follow(req.userA, req.userB);
            String email = db.newFollowEmail(req.userB);
            if (email != null && attemptFollow == 2) {
                String displayName = db.getDisplayName(req.userA);
                SendGridEmail.sendEmail(email, displayName + " is now following you", displayName + " is now following you");
            }
            if (attemptFollow == 0) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + attemptFollow, null));
            }
        });

        Spark.put("/preferences", (request, response) -> {
            PreferencesRequest req = gson.fromJson(request.body(), PreferencesRequest.class);
            response.status(200);
            response.type("application/json");
            int newPrefs = db.changePreferences(req.userID, req.followsMe, req.commentsOnPost, req.followingPost);
            if (newPrefs == 0) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newPrefs, null));
            }
        });
    }

    static int getIntFromEnv(String envar, int defaultVal) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(envar) != null) {
            return Integer.parseInt(processBuilder.environment().get(envar));
        }
        return defaultVal;
    }

    private static String uploadImage(String encodedString, Drive service, int msgid, String mime) throws IOException {
        byte[] decodedImg = Base64.getDecoder().decode(encodedString);
        java.io.File thisFile = new java.io.File("image");
        Files.write(decodedImg, thisFile);
        File fileMetadata = new File();
        fileMetadata.setName(msgid + ".jpeg");
        FileContent mediaContent = new FileContent(mime, thisFile);
        File file = service.files().create(fileMetadata, mediaContent).setFields("id").execute();
        return file.getId();
    }
 
    private static String downloadImage(String file_ID, Drive service) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        service.files().get(file_ID).executeMediaAndDownloadTo(outputStream);
        return outputStream.toString();
    }

    /**
     * Set up CORS headers for the OPTIONS verb, and for every response that the
     * server sends. This only needs to be called once.
     *
     * @param origin  The server that is allowed to send requests to this server
     * @param methods The allowed HTTP verbs from the above origin
     * @param headers The headers that can be sent with a request from the above
     *                origin
     */
    private static void enableCORS(String origin, String methods, String headers) {
        // Create an OPTIONS route that reports the allowed CORS headers and methods
        Spark.options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        // 'before' is a decorator, which will run before any
        // get/post/put/delete. In our case, it will put three extra CORS
        // headers into the response
        Spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
        });
    }

    // * Creates an authorized Credential object.
    /*
     * @param HTTP_TRANSPORT The network HTTP Transport. /* @return An authorized
     * Credential object. /* @throws IOException If the credentials.json file cannot
     * be found.
     */

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        java.io.InputStream in = App.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        return GoogleCredential.fromStream(in).createScoped(Collections.singleton(DriveScopes.DRIVE));
    }

    public static String getFileEncoding(String fileID) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            service.files().get(fileID).executeMediaAndDownloadTo(outputStream);
            byte[] bytes = outputStream.toByteArray();
            outputStream.flush();
            outputStream.close();
            String encoded = Base64.getEncoder().encodeToString(bytes);
            return encoded;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
