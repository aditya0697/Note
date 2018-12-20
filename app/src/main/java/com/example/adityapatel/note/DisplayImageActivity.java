package com.example.adityapatel.note;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class  DisplayImageActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        imageView = findViewById(R.id.imageView2);
        Bundle extras = getIntent().getExtras();
        String imagePath = extras.getString("picture");

        Bitmap bitmap =  BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bitmap);

    }
}
