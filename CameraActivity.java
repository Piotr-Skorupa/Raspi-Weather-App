package com.example.piotrskorupa.raspiweatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;

public class CameraActivity extends AppCompatActivity {

    public static ImageView cameraImage;
    public static TextView mainText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraImage = (ImageView) findViewById(R.id.camera_picture);
        mainText = (TextView) findViewById(R.id.camera_text);
    }

    @Override
    public void onStop () {

        //sending STOP_REQUEST to camera
        MainActivity.PlaceholderFragment.mqttConnector.publishCamOnOff(false);
        super.onStop();
    }

}
