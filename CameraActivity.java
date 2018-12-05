package com.example.piotrskorupa.raspiweatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class CameraActivity extends AppCompatActivity {

    private ImageView cameraImage;

    public static String imageString;
    public static boolean isImageChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraImage = (ImageView) findViewById(R.id.camera_picture);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                if (isImageChanged == true) {
                    byte[] decodeString = Base64.decode(imageString, Base64.DEFAULT);
                    Bitmap decoded = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    cameraImage.setImageBitmap(decoded);
                    isImageChanged = false;
                }
                return null;
            }
        }.execute();
    }
}
