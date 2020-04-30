package edu.lehigh.cse216.slj222;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.credentials.IdToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("363085709256-27bcmvdo6sqga5b2nsk0ks1g4uh9nf52.apps.googleusercontent.com")
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Created SharedPreferences object
        SharedPreferences sharedPref = this.getSharedPreferences("Shared", Context.MODE_PRIVATE);

        // Check if this was called (as in sign out), if so sign out

        Bundle b = getIntent().getExtras();

        if (b != null) { // Only true if this was called by clicking sign out
            mGoogleSignInClient.signOut();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("sessionKey", "logout"); // Get rid of stored session key, set to default
            editor.commit();
        }

        // Go forward with the last session key if not logged out
        String sessionKey = sharedPref.getString("sessionKey", "logout");

        if (!sessionKey.equals("logout")) { // This means the user signed in and never signed out
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent); // Take the user directly to the MainActivity
        }

        findViewById(R.id.sign_in_button).setOnClickListener(view -> { // Run Google sign-in
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            signInIntent.putExtra("userID", mGoogleSignInClient.getInstanceId());
            startActivityForResult(signInIntent, 1); // Starts Google sign-in process
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { // This happens after Google sign-in
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Send to login backend route
            String myToken = account.getIdToken(); // This is a post, backend responds that it is valid

            String url = "https://subzer0.herokuapp.com/login/" + myToken;

            JSONObject request = new JSONObject();

            // Request a string response from the provided URL.
            JsonObjectRequest loginReq = new JsonObjectRequest(Request.Method.POST, url, request,
                    response -> {
                        //try {
                            Log.d("slj222", "Token: " + myToken);
                            Log.d("slj222", "Response: " + response.toString());
                        /*} catch (final JSONException e) {
                            Log.d("slj222", "Error parsing JSON file: " + e.getMessage());
                        }*/
                    },
                    error -> {
                        // if there's an error
                        Log.d("slj222", "error:" + error.getMessage());
                        error.printStackTrace();
                    }) {
            };
            VolleySingleton.getInstance(this).addToRequestQueue(loginReq);

            // Write session key to SharedPreferences
            SharedPreferences sharedPref = this.getSharedPreferences("Shared", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("sessionKey", "bla");
            editor.putString("givenName", account.getGivenName());
            editor.putString("userId", account.getId());
            editor.putString("displayName", account.getDisplayName());
            editor.putString("photoURL", account.getPhotoUrl().toString());
            editor.commit();

            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(this, MainActivity.class);

            // Post user info to users table, if not there already
            newUser(account.getId(), account.getDisplayName(), account.getPhotoUrl().toString());

            startActivity(intent); // Start the MainActivity!
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            e.printStackTrace();
            Log.w("slj222", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    /**
     * HTTP PUT user information to the users table
     * @param userID
     * @param displayName
     * @param photoURL
     */
    private void newUser(String userID, String displayName, String photoURL) {
        String url = "https://subzer0.herokuapp.com/user";
        Map<String, String> params = new HashMap<>();

        params.put("userID", userID);
        params.put("displayName", displayName);
        params.put("photoURL", photoURL);

        JSONObject request = new JSONObject(params);

        // Request a string response from the provided URL.
        JsonObjectRequest putReq = new JsonObjectRequest(Request.Method.PUT, url, request,
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
        VolleySingleton.getInstance(this).addToRequestQueue(putReq);
    }

}
