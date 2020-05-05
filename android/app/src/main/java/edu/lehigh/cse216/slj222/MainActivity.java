package edu.lehigh.cse216.slj222;

import android.Manifest;
import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity {


    /**
     * mData holds the data we get from Volley
     */
    ArrayList<Message> mData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getMessages(); // Run the script to get messages
        Button postButton = findViewById(R.id.post_button);

        postButton.setOnClickListener(view -> {
            moveToNewMessageActivity();
        });


        final SwipeRefreshLayout swipeContainer = findViewById(R.id.RefreshLayout);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(() -> {
            mData.clear();
            getMessages();
            setLikes();
            swipeContainer.setRefreshing(false);
        });


    }
    public void moveToNewMessageActivity() {
        Intent intent = new Intent(MainActivity.this, NewMessage.class);
        startActivity(intent);
    }

    /**
     * Overriding the menu to hide the "Go Back" button on main screen.
     * The user should just logout instead. By forcing them to logout, the activity stack won't
     * get too large if there are multiple logins/logouts in a session
     * @param menu
     * @return
     */
    //@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* I want to hide the go back if they are on the main page.
        This makes it so they can only sign out if signout button is clicked.
         */
        super.onCreateOptionsMenu(menu);
        MenuItem goBack = menu.findItem(R.id.goBack);
        goBack.setVisible(false);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getMessages();
        setLikes();
    }

    /**
     * Takes HTTP response and parses out the Messages and stores in an ArrayList
     * @param response an HTTP response
     * @return The parsed messages
     */
    public static ArrayList<Message> getMData(String response) {

        ArrayList<Message> mData = new ArrayList<>();

        try {
            JSONObject responseObj = new JSONObject(response);
            JSONArray json = responseObj.getJSONArray("mData");
            for (int i = 0; i < json.length(); i++) {
                int msgId = json.getJSONObject(i).getInt("msgId");
                String userId = json.getJSONObject(i).getString("userId");
                String message = json.getJSONObject(i).getString("message");
                int likes = json.getJSONObject(i).getInt("likes");
                int dislikes = json.getJSONObject(i).getInt("dislikes");
                int comments = json.getJSONObject(i).getInt("comments");
                String displayName = json.getJSONObject(i).getString("displayName");;
                String photoURL = json.getJSONObject(i).getString("photoURL");
                String link = "";
                try {
                    link = json.getJSONObject(i).getString("link");
                } catch(JSONException e) {
                    link = "";
                }
                String photoImage, mimeType;
                try {
                    photoImage = json.getJSONObject(i).getString("photoImage");
                    mimeType = json.getJSONObject(i).getString("mimeType");
                } catch(JSONException e) {
                    photoImage = "";
                    mimeType = "";
                }
                mData.add(new Message(msgId, message, userId, likes, dislikes, comments, displayName, photoURL, link, photoImage, mimeType));
            }
        } catch (final JSONException e) {
            Log.d("slj222", "Error parsing JSON file: " + e.getMessage());
            e.printStackTrace();
            return mData;
        }
        Log.d("slj222", "Successfully parsed JSON file.");

        return mData;
    }

    private void populateListFromVolley(RecyclerView rv) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        ItemListAdapter adapter = new ItemListAdapter(this, mData, likes);
        rv.setAdapter(adapter);
    }

    /**
     * HTTP GET the current messages
     */
    private void getMessages() {
        String url = "http://subzer0.herokuapp.com/messages";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    mData = getMData(response);
                    populateListFromVolley(findViewById(R.id.Recycler));
                }, error -> {
            Log.e("slj222", "That didn't work!");
            Log.e("slj222", error.toString());
        });
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    /**
     * From StackOverflow DO NOT CHANGE
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}