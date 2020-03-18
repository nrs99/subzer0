package edu.lehigh.cse216.slj222;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class BaseActivity extends AppCompatActivity {

    GoogleSignInAccount currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = getIntent().getExtras().getParcelable("Account"); // Retrieve account passed
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.myName);
        item.setTitle(currentUser.getGivenName());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            case R.id.logout:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.myName:
                intent = new Intent(this, Profile.class);
                intent.putExtra("Account", currentUser);
                startActivity(intent);
                return true;
            case R.id.goBack:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
