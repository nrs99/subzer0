package edu.lehigh.cse216.slj222;

// Taken from StackOverflow, do not change

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Taken from StackOverflow, probably should not modify
 */
public class urlImage extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ImageView imageView;
    int resize;

    /**
     *
     * @param url The URL string you are taking the image from
     * @param imageView The view where your image will be displayed
     * @param resize The bitmap dimension of your square photos
     */
    public urlImage(String url, ImageView imageView, int resize) {
        this.url = url;
        this.imageView = imageView;
        this.resize = resize;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        imageView.setImageBitmap(Bitmap.createScaledBitmap(result, resize, resize, false));
    }

}