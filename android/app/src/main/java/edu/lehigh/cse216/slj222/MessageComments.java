package edu.lehigh.cse216.slj222;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static edu.lehigh.cse216.slj222.MainActivity.hideKeyboard;

public class MessageComments extends BaseActivity {

    private ArrayList<Message> mData; // The Message we are looking at, stored as an ArrayList to use methods from other classes
    private ArrayList<Comment> cData; // An ArrayList of the comments associate with the message
    private int msgId; // The id of the message we are looking at

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_comments);

        msgId = getIntent().getExtras().getInt("msgid");

        final EditText textToSend = findViewById(R.id.newComment);
        final Button sendComment = findViewById(R.id.button);
        //final Button

        // HTTP get request for the one message
        String url = "https://subzer0.herokuapp.com/messages/" + msgId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    mData = getMessage(response);
                    populateListFromVolley(findViewById(R.id.recycler3));
                }, error -> {
            Log.e("slj222", "That didn't work!");
            Log.e("slj222", error.toString());
        });
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

        getComments(); // Get the comments associated


        sendComment.setOnClickListener(b -> { // Set button to post a comment
            if (!textToSend.getText().toString().equals("")) {
                String message = textToSend.getText().toString().trim();
                postComment(message);
                textToSend.getText().clear(); //Remove whatever's in there
                hideKeyboard(this); // Hides the keyboard if clicked
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setLikes();
        getComments();
    }

    private void populateListFromVolley(RecyclerView rv) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        ItemListAdapter adapter = new ItemListAdapter(this, mData, likes);
        rv.setAdapter(adapter);

    }

    private void populateCommentFromVolley(RecyclerView rv) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        CommentAdapter adapter = new CommentAdapter(this, cData);
        rv.setAdapter(adapter);
    }

    // Getting a single message based on HTTP response
    public static ArrayList<Message> getMessage(String response) {

        ArrayList<Message> mData = new ArrayList<>();

        try {
            JSONObject responseObj = new JSONObject(response);
            JSONObject json = responseObj.getJSONObject("mData");
            int msgId = json.getInt("msgId");
            String userId = json.getString("userId");
            String message = json.getString("message");
            int likes = json.getInt("likes");
            int dislikes = json.getInt("dislikes");
            int comments = json.getInt("comments");
            String displayName = json.getString("displayName");
            String photoURL = json.getString("photoURL");
            String link = json.getString("link");
            String photoImage = json.getString("photoImage");
            String mimeType = json.getString("mimeType");
            mData.add(new Message(msgId, message, userId, likes, dislikes, comments, displayName, photoURL, link, photoImage, mimeType));
        } catch (final JSONException e) {
            Log.d("slj222", "Error parsing JSON file: " + e.getMessage());
            e.printStackTrace();
            return mData;
        }
        Log.d("slj222", "Successfully parsed JSON file.");

        return mData;
    }

    // Getting ArrayList of comments based on HTTP response
    public static ArrayList<Comment> getComments(String response) {
        ArrayList<Comment> cData = new ArrayList<>();

        try {
            JSONObject responseObj = new JSONObject(response);
            JSONArray json = responseObj.getJSONArray("mData");
            for (int i = 0; i < json.length(); i++) {
                int commentId = json.getJSONObject(i).getInt("commentId");
                int msgId = json.getJSONObject(i).getInt("msgId");
                String comment = json.getJSONObject(i).getString("comment");
                String userId = json.getJSONObject(i).getString("userId");
                String displayName = json.getJSONObject(i).getString("displayName");
                String photoURL = json.getJSONObject(i).getString("photoURL");
                cData.add(new Comment(commentId, msgId, comment, userId, displayName, photoURL));
            }
        } catch (final JSONException e) {
            Log.d("slj222", "Error parsing JSON file: " + e.getMessage());
            e.printStackTrace();
            return cData;
        }
        Log.d("slj222", "Successfully parsed JSON file.");

        return cData;
    }

    // get Comments based on current message
    void getComments() {
        String url = "https://subzer0.herokuapp.com/messages/" + msgId + "/comments";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            cData = getComments(response);
            populateCommentFromVolley(findViewById(R.id.recycler4));
        }, error -> {
            Log.e("slj222", "That didn't work!");
            Log.e("slj222", error.toString());
        });
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    // HTTP POST the new comments
    private void postComment(String comment) {
        String url = "https://subzer0.herokuapp.com/comments";
        Map<String, Object> params = new HashMap<>();

        params.put("msgId", msgId);
        params.put("comment", comment);
        params.put("userId", userId);

        JSONObject request = new JSONObject(params);

        // Request a string response from the provided URL.
        JsonObjectRequest postReq = new JsonObjectRequest(Request.Method.POST, url, request,
                response -> {
                    try {
                        response.getString("mStatus");  //if its working or not
                        cData.clear();
                        getComments();
                    } catch (final JSONException e) {
                        Log.d("slj222", "Error parsing JSON file: " + e.getMessage());
                    }
                },
                error -> {
                    // if there's an error
                    Log.d("slj222", "error:" + error.getMessage());
                    error.printStackTrace();
                }) {
        };
        VolleySingleton.getInstance(this).addToRequestQueue(postReq);
    }


}
