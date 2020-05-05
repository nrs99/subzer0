package edu.lehigh.cse216.slj222;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class NewMessage extends BaseActivity {

    public String image = "";

    private static final int GALLERY_REQUEST_CODE = 216;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        Button addImage = findViewById(R.id.message_addimg);

        addImage.setOnClickListener(view -> {
            pickFromGallery();
            updateThumbnail();
        });

        Button submitButton = findViewById(R.id.message_submit);
        submitButton.setOnClickListener(view -> {
            postMessage();
            finish();
        });
    }

    private void updateThumbnail() {
        ImageView thumbnail = findViewById(R.id.message_thumbnail);
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        thumbnail.setImageBitmap(decodedByte);
    }

    public void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    Uri imageUri = data.getData();
                    InputStream imageStream;
                    try {
                        imageStream = getContentResolver().openInputStream(imageUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        break;
                    }
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    image = encodeImage(selectedImage);

                    break;
            }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

        /**
         * HTTP POST a new message
         */
    public void postMessage() {
        String url = "https://subzer0.herokuapp.com/messages";
        Map<String, String> params = new HashMap<>();

        EditText text = (EditText)findViewById(R.id.message_text);
        String message = text.getText().toString();

        EditText linkText = (EditText)findViewById(R.id.message_link);
        String link = linkText.getText().toString();



        if(message == "") {
            return;
        }

        params.put("message", message);
        params.put("userID", userId);
        params.put("link", link);
        params.put("photoURL", image);

        JSONObject request = new JSONObject(params);

        // Request a string response from the provided URL.
        JsonObjectRequest getReq = new JsonObjectRequest(Request.Method.POST, url, request,
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
        VolleySingleton.getInstance(this).addToRequestQueue(getReq);
    }
}
