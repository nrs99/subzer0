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
    int profiledUserID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        currentUser = getIntent().getExtras().getParcelable("Account");

    }

}
