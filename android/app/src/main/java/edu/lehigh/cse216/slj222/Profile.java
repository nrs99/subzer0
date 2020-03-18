package edu.lehigh.cse216.slj222;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    ArrayList<Message> mData = new ArrayList<>();
    GoogleSignInAccount currentUser;
    int profiledUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get messages for current user


    }
}
