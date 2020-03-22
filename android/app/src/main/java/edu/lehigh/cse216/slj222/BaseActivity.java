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

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import static edu.lehigh.cse216.slj222.MainActivity.hideKeyboard;

public class BaseActivity extends AppCompatActivity {

    String sessionKey;
    String givenName;
    String userId;

    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionKey = getIntent().getExtras().getString("sessionKey");
        givenName = getIntent().getExtras().getString("givenName");
        userId = getIntent().getExtras().getString("userId");
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
        switch (item.getItemId()) {
            case R.id.logout:
                intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Get rid of activity stack
                intent.putExtra("logout", true);
                SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("sessionKey", "logout");
                editor.commit();
                startActivity(intent);
                return true;
            case R.id.myName:
                intent = new Intent(this, Profile.class);
                passInfo(intent, sessionKey, givenName, userId);
                startActivity(intent);
                return true;
            case R.id.goBack:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
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

    public static void passInfo(Intent intent, String sessionKey, String givenName, String userId) {
        intent.putExtra("sessionKey", sessionKey);
        intent.putExtra("givenName", givenName);
        intent.putExtra("userId", userId);
    }

}
