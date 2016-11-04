package com.example.rafaelmeyer.mycustomcamera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        ImageView imageView = (ImageView) findViewById(R.id.photoFullScreen);

        File imageURL = (File) getIntent().getExtras().get("image");

        Picasso.with(this).load(imageURL).into(imageView);
    }
}
