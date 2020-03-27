package edu.lehigh.cse216.slj222;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
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
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {

    /**
     * mData holds the data we get from Volley
     */
    ArrayList<Message> mData = new ArrayList<>();
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getMessages(); // Run the script to get messages

        Button postButton = findViewById(R.id.post_button);

        final EditText textToSend = findViewById(R.id.textView);
        textToSend.setCursorVisible(false); // Disable blinking cursor

        postButton.setOnClickListener(view -> {
            if (!textToSend.getText().toString().equals("")) {
                message = textToSend.getText().toString().trim();
                postMessage();
                textToSend.getText().clear(); //Remove whatever's in there
                hideKeyboard(this); // Hides the keyboard if clicked
            }
        });

        SwipeRefreshLayout refreshLayout = findViewById(R.id.RefreshLayout);
        refreshLayout.setOnClickListener(view -> {
            hideKeyboard(this); // Hiding keyboard if you click outside the edittext area
        });

        final SwipeRefreshLayout swipeContainer = findViewById(R.id.RefreshLayout);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(() -> {
            mData.clear();
            getMessages();
            swipeContainer.setRefreshing(false);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* I want to hide the go back if they are on the main page.
        This makes it so they can only sign out if signout button is clicked.
         */
        super.onCreateOptionsMenu(menu);
        MenuItem goBack = menu.findItem(R.id.goBack);
        goBack.setVisible(false);

        return true;
    }

    public static ArrayList<Message> getMData(String response) {

        ArrayList<Message> mData = new ArrayList<>();

        try {
            JSONObject responseObj = new JSONObject(response);
            JSONArray json = responseObj.getJSONArray("mData");
            for (int i = 0; i < json.length(); i++) {
                int msgId = json.getJSONObject(i).getInt("msgId");
                int userId = json.getJSONObject(i).getInt("userId");
                String message = json.getJSONObject(i).getString("message");
                int likes = json.getJSONObject(i).getInt("likes");
                int dislikes = json.getJSONObject(i).getInt("dislikes");
                mData.add(new Message(msgId, message, userId, likes, dislikes));
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
        ItemListAdapter adapter = new ItemListAdapter(this, mData);
        rv.setAdapter(adapter);

    }

    public void postMessage() {
        String url = "https://subzer0.herokuapp.com/messages";
        Map<String, String> params = new HashMap<>();
        params.put("message", message);
        params.put("userID", "37"); // TODO: Replace with actual user ID

        JSONObject request = new JSONObject(params);

        // Request a string response from the provided URL.
        JsonObjectRequest getReq = new JsonObjectRequest(Request.Method.POST, url, request,
                response -> {
                    try {
                        response.getString("mStatus");  //if its working or not
                        mData.clear();
                        getMessages();
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