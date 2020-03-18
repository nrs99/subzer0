package edu.lehigh.cse216.slj222;

import android.os.Bundle;

import java.util.ArrayList;

public class Profile extends BaseActivity {

    ArrayList<Message> mData = new ArrayList<>();
    int profiledUserID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentUser = getIntent().getExtras().getParcelable("Account");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get messages for current user

    }
}
