package edu.lehigh.cse216.slj222;

import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

public class Profile extends BaseActivity {

    ArrayList<Message> mData = new ArrayList<>();
    private String profiledUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profiledUserID = getIntent().getExtras().getString("profiledUser");

        getMessages();

    }

    private void getMessages() {
        String url = "http://subzer0.herokuapp.com/messages/user/" + profiledUserID;
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
        ItemListAdapter adapter = new ItemListAdapter(this, mData);
        rv.setAdapter(adapter);

    }

}
