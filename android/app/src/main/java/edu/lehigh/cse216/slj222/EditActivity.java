package edu.lehigh.cse216.slj222;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        EditText comment = findViewById(R.id.oldComment);
        comment.setText(getIntent().getExtras().getString("comment"));

        int cId = getIntent().getExtras().getInt("cId");

        Button fixButton = findViewById(R.id.fixedComment);
        fixButton.setOnClickListener(b -> {

            String url = "https://subzer0.herokuapp.com/comments/edit";
            Map<String, Object> params = new HashMap<>();

            params.put("cid", cId);
            params.put("comment", comment.getText().toString());

            JSONObject request = new JSONObject(params);

            // Request a string response from the provided URL.
            JsonObjectRequest putReq = new JsonObjectRequest(Request.Method.PUT, url, request,
                    response -> {
                        try {
                            response.getString("mStatus");  //if its working or not
                            finish();
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

        });
    }
}
