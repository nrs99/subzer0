package edu.lehigh.cse216.slj222;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageComments extends BaseActivity {

    private ArrayList<Message> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_comments);

        int msgId = getIntent().getExtras().getInt("msgid");

        final EditText textToSend = findViewById(R.id.newComment);

        String url = "http://subzer0.herokuapp.com/messages/" + msgId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    mData = getMessage(response);
                    populateListFromVolley(findViewById(R.id.recycler3));
                }, error -> {
            Log.e("slj222", "That didn't work!");
            Log.e("slj222", error.toString());
        });
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

        // TODO: Next request based on comments

    }

    private void populateListFromVolley(RecyclerView rv) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        ItemListAdapter adapter = new ItemListAdapter(this, mData);
        rv.setAdapter(adapter);

    }

    public static ArrayList<Message> getMessage(String response) {

        ArrayList<Message> mData = new ArrayList<>();

        try {
            JSONObject responseObj = new JSONObject(response);
            JSONObject json = responseObj.getJSONObject("mData");
            int msgId = json.getInt("msgId");
            int userId = json.getInt("userId");
            String message = json.getString("message");
            int likes = json.getInt("likes");
            int dislikes = json.getInt("dislikes");
            mData.add(new Message(msgId, message, userId, likes, dislikes));
        } catch (final JSONException e) {
            Log.d("slj222", "Error parsing JSON file: " + e.getMessage());
            e.printStackTrace();
            return mData;
        }
        Log.d("slj222", "Successfully parsed JSON file.");

        return mData;
    }

    public static ArrayList<Comment> getComments(String response) {
        ArrayList<Comment> cData = new ArrayList<>();

        return null;
    }
}
