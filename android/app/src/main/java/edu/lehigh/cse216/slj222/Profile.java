package edu.lehigh.cse216.slj222;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Profile extends BaseActivity {

    ArrayList<Message> mData = new ArrayList<>();
    private String profiledUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Grab key attributes of the user whose profile this is from calling activity
        profiledUserID = getIntent().getExtras().getString("profiledUser");
        String profiledName = getIntent().getExtras().getString("profiledName");
        String profiledPhoto = getIntent().getExtras().getString("profiledPhoto");

        TextView displayName = findViewById(R.id.profileName);
        displayName.setText(profiledName);

        Button b = findViewById(R.id.profile_button);

        if(userId.equals(profiledUserID)) {
            b.setText("Preferences");
            b.setOnClickListener(view ->{
                moveToPreferencesActivity();
            });
        } else {
            b.setText("Follow");
            b.setOnClickListener(view -> {
               followUser();
            });
        }



        ImageButton profilePic = findViewById(R.id.profilePic);
        new urlImage(profiledPhoto, profilePic, 250).execute();

        getMessages();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getMessages();
        setLikes();
    }

    // Get messages that the current user posted
    private void getMessages() {
        String url = "https://subzer0.herokuapp.com/messages/user/" + profiledUserID;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    mData = MainActivity.getMData(response);
                    populateListFromVolley(findViewById(R.id.Recycler2));
                }, error -> {
            Log.e("slj222", "That didn't work!");
            Log.e("slj222", error.toString());
        });
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void populateListFromVolley(RecyclerView rv) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        ItemListAdapter adapter = new ItemListAdapter(this, mData, likes);
        rv.setAdapter(adapter);

    }

    private void moveToPreferencesActivity() {
        Intent intent = new Intent(Profile.this, Preferences.class);
        startActivity(intent);
    }

    private void followUser() {
        String url = "https://subzer0.herokuapp.com/follow";
        Map<String, String> params = new HashMap<>();

        params.put("userA", userId);
        params.put("userB", profiledUserID);

        JSONObject request = new JSONObject(params);

        // Request a string response from the provided URL.
        JsonObjectRequest getReq = new JsonObjectRequest(Request.Method.PUT, url, request,
                response -> {
                    try {
                        response.getString("mStatus");  //if its working or not
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
        VolleySingleton.getInstance(this).addToRequestQueue(getReq);

    }
}
