package edu.lehigh.cse216.slj222;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.*;

public class MainActivity extends AppCompatActivity {

    /**
     * mData holds the data we get from Volley
     */
    ArrayList<Datum> mData = new ArrayList<>();
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://subzer0.herokuapp.com/messages";
        JSONObject request = new JSONObject();


        Button postButton = findViewById(R.id.post_button);
        final EditText textToSend = findViewById(R.id.textView);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!textToSend.getText().toString().equals("")) {
                    message = textToSend.getText().toString();
                    postMessage();
                }
            }
        });

        // Request a string response from the provided URL.
        JsonObjectRequest getResponse = new JsonObjectRequest(Request.Method.GET, url, request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        populateListFromVolley(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
        };
        // Add the request to the RequestQueue.
        queue.add(getResponse);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), SecondActivity.class);
            i.putExtra("label_contents", "CSE216 is the best");
            startActivityForResult(i, 789); // 789 is the number that will come back to us
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private void populateListFromVolley(JSONObject response){
        try {
            JSONArray json= response.getJSONArray("mData");
            for (int i = 0; i < json.length(); ++i) {
                int id = json.getJSONObject(i).getInt("userId");
                String message = json.getJSONObject(i).getString("message");
                String title = "title";          //json.getJSONObject(i).getString("dateCreated");
                mData.add(new Datum(message, title, id));
            }
        } catch (final JSONException e) {
            Log.d("slj222", "Error parsing JSON file: " + e.getMessage());
            return;
        }
        Log.d("slj222", "Successfully parsed JSON file.");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView rv = (RecyclerView) findViewById(R.id.Subzer0);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        ItemListAdapter adapter = new ItemListAdapter(this, mData);
        rv.setAdapter(adapter);

    }

    public void postMessage() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://subzer0.herokuapp.com/messages";
        Map<String, String> params = new HashMap<>();
        EditText theBox = findViewById(R.id.textView);
        params.put("message", message);
        //params.put("title", "cse216");
        JSONObject request = new JSONObject(params);

        // Request a string response from the provided URL.
        JsonObjectRequest getReq = new JsonObjectRequest(Request.Method.POST, url, request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            response.getString("mStatus");  //if its working or not
                            RecyclerView display = findViewById(R.id.datum_list_view);

                            mData.clear();

                            getMessage();
                        } catch (final JSONException e) {
                            Log.d("slj222", "Error parsing JSON file: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // if there's an error
                        Log.d("slj222", "error:" + error.getMessage());
                        error.printStackTrace();
                    }
                }) {
        };
        // Add the request to the RequestQueue.
        queue.add(getReq);
    }




    public void getMessage() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://subzer0.herokuapp.com/messages";
        JSONObject request = new JSONObject();

        // Request a string response from the provided URL.
        JsonObjectRequest getReq = new JsonObjectRequest(Request.Method.GET, url, request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        populateListFromVolley(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // if there is an error, show it
                        Log.d("slj222", "error:" + error.getMessage());
                        error.printStackTrace();
                    }
                }) {
        };
        queue.add(getReq);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 789) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the "extra" string of data
                Toast.makeText(MainActivity.this, data.getStringExtra("result"), Toast.LENGTH_LONG).show();
            }
        }
    }
}