package edu.lehigh.cse216.slj222;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Preferences extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        CheckBox cb1 = findViewById(R.id.preference_1);
        CheckBox cb2 = findViewById(R.id.preference_2);
        CheckBox cb3 = findViewById(R.id.preference_3);

        ArrayList<Boolean> prefs = getUserPreferences();

        while(prefs.size() < 3) {
            prefs.add(false);
        }
        cb1.setChecked(prefs.get(0));
        cb2.setChecked(prefs.get(1));
        cb3.setChecked(prefs.get(2));

        Button b = findViewById(R.id.preferences_submit);
        b.setOnClickListener(view -> {
            postPreferences();
            finish();
        });
    }

    public ArrayList<Boolean> getUserPreferences() {
        Log.d("preferences","getting user preferences");
        String url = "http://subzer0.herokuapp.com/users/" + userId + "/preferences";
        ArrayList<Boolean> prefs = new ArrayList<Boolean>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject responseObj = new JSONObject(response);
                        prefs.add(responseObj.getBoolean("followsMe"));
                        prefs.add(responseObj.getBoolean("commentsOnPost"));
                        prefs.add(responseObj.getBoolean("followingPost"));
                        Log.d("preferences", "prefs contains " + prefs.toString());
                    } catch (JSONException e) {
                        Log.d("slj222", "Error parsing JSON file: " + e.getMessage());
                        e.printStackTrace();
                    }
                }, error -> {
            Log.e("slj222", "That didn't work!");
            Log.e("slj222", error.toString());
        });
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        return prefs;
    }

    public void postPreferences() {
        boolean pref1 = ((CheckBox)findViewById(R.id.preference_1)).isChecked();
        boolean pref2 = ((CheckBox)findViewById(R.id.preference_2)).isChecked();
        boolean pref3 = ((CheckBox)findViewById(R.id.preference_3)).isChecked();

        String url = "http://subzer0.herokuapp.com/preferences";

        Map<String, Object> params = new HashMap<>();

        params.put("userID", userId);
        params.put("followsMe", pref1);
        params.put("commentsOnPost", pref2);
        params.put("followingPost", pref3);


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
