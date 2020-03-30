package edu.lehigh.cse216.slj222;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static edu.lehigh.cse216.slj222.MainActivity.hideKeyboard;

/**
 * There are certain methods that all of the activities should be able to access,
 * such as the menu bar and some methods that deal with the keyboard.
 * It also helps store some user details
 */

public class BaseActivity extends AppCompatActivity {

    String sessionKey;
    String givenName;
    String userId;

    String displayName;
    String myURL;

    HashMap<Integer, Integer> likes = new HashMap<Integer, Integer>();

    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read from SharedPreferences (a file that would be stored along with app on specific device)
        SharedPreferences sharedPref = this.getSharedPreferences("Shared", Context.MODE_PRIVATE);
        sessionKey = sharedPref.getString("sessionKey", "logout");
        givenName = sharedPref.getString("givenName", "Joe");
        userId = sharedPref.getString("userId", "0");
        displayName = sharedPref.getString("displayName", "Joe Schmoe");
        myURL = sharedPref.getString("photoURL", "");

        setLikes();

        Log.d("slj222", "Session key: " + sessionKey);
        Log.d("slj222", givenName);
        Log.d("slj222", userId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mOptionsMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.myName);
        item.setTitle(givenName);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        SharedPreferences sharedPref = this.getSharedPreferences("Shared", Context.MODE_PRIVATE);
        switch (item.getItemId()) {
            case R.id.logout:
                intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Get rid of activity stack
                intent.putExtra("logout", true); // Lets it know this was called rather than fresh app opening

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("sessionKey", "logout"); // When the app starts, it would take the user to a
                editor.commit();                          // signed in state unless the sessionKey is logout
                startActivity(intent);
                return true;
            case R.id.myName:
                intent = new Intent(this, Profile.class);
                // Pass my userID
                intent.putExtra("profiledUser", userId);    // Pass my info into the profile activity
                intent.putExtra("profiledName", displayName);
                intent.putExtra("profiledPhoto", myURL);
                startActivity(intent);
                return true;
            case R.id.goBack:
                finish(); // This ends the current activity and returns to state of previous
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) { // Method from StackOverflow that resolves keyboard hiding
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static HashMap<Integer, Integer> getLikes(String response) { // Creates a HashMap for
                             // the messages the logged in user has liked based on HTTP response
        HashMap<Integer, Integer> myLikes = new HashMap<>();

        try {
            JSONObject responseObj = new JSONObject(response);
            JSONArray json = responseObj.getJSONArray("mData");
            for (int i = 0; i < json.length(); i++) {
                int mid = json.getJSONObject(i).getInt("mid");
                int likes = json.getJSONObject(i).getInt("likes");
                myLikes.put(mid, likes);
            }

        } catch (final JSONException e) {
            e.printStackTrace();
        }

        return myLikes;

    }

     void setLikes() { // HTTP request that sets up adding my likes HashMap

        String url = "http://subzer0.herokuapp.com/likes/" + userId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    likes = getLikes(response);
                }, error -> {
            Log.e("slj222", "That didn't work!");
            Log.e("slj222", error.toString());
        });
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

}
