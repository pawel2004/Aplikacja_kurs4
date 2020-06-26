package com.example.grybos.aplikacjakurs4.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.grybos.aplikacjakurs4.Helpers.ImageData;
import com.example.grybos.aplikacjakurs4.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class CollageActivity extends AppCompatActivity {

    private FrameLayout frame1;
    private ArrayList<ImageData> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage);

        getSupportActionBar().hide();

        frame1 = findViewById(R.id.frame1);

        buildFrame();

    }

    private void buildFrame(){

        frame1.removeAllViews();

        list = (ArrayList<ImageData>) getIntent().getExtras().getSerializable("list");

        for (ImageData data : list){

            ImageView iv = new ImageView(CollageActivity.this);
            iv.setBackgroundColor(0xFFFFFFFF);
            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            iv.setX(data.getX());
            iv.setY(data.getY());
            iv.setImageResource(R.drawable.baseline_add_black_18dp);
            iv.setLayoutParams(new LinearLayout.LayoutParams(data.getW(), data.getH()));

            iv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    Intent intent = new Intent(CollageActivity.this, CameraActivity.class);
                    startActivityForResult(intent, 444);

                    return true;
                }
            });

            frame1.addView(iv);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



    }
}
