package com.example.grybos.aplikacjakurs4.Activities;

import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import com.example.grybos.aplikacjakurs4.Helpers.ImageData;
import com.example.grybos.aplikacjakurs4.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseCollageActivity extends AppCompatActivity {

    //zmienne
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private Point size;
    private ArrayList<ImageData> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_collage);

        getSupportActionBar().hide();

        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);

        image1.setOnClickListener(funkcja);
        image2.setOnClickListener(funkcja);
        image3.setOnClickListener(funkcja);

        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        Log.d("xxx","szerokość ekranu " + size.x);
        Log.d("xxx","wysokość ekranu " + size.y);

    }

    private View.OnClickListener funkcja = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){

                case R.id.image1:

                    list.add(new ImageData(0,0,size.x/2, size.y));
                    list.add(new ImageData(size.x/2 + 1, 0, size.x/2, size.y));
                    Intent intent = new Intent(ChooseCollageActivity.this, CollageActivity.class);
                    intent.putExtra("list", list);
                    startActivity(intent);

                    list.clear();

                    break;
                case R.id.image2:

                    list.add(new ImageData(0,0,size.x,size.y/2));
                    list.add(new ImageData(0, size.y/2 + 1, size.x, size.y/2));
                    Intent intent2 = new Intent(ChooseCollageActivity.this, CollageActivity.class);
                    intent2.putExtra("list", list);
                    startActivity(intent2);

                    list.clear();

                    break;
                case R.id.image3:

                    list.add(new ImageData(0,0,size.x/2, size.y));
                    list.add(new ImageData(size.x/2 + 1, 0, size.x/2, size.y/3));
                    list.add(new ImageData(size.x/2 + 1, size.y/3 + 1, size.x/2, size.y/3));
                    list.add(new ImageData(size.x/2 + 1, (size.y/3 + 1) * 2, size.x/2, size.y/3));
                    Intent intent3 = new Intent(ChooseCollageActivity.this, CollageActivity.class);
                    intent3.putExtra("list", list);
                    startActivity(intent3);

                    list.clear();

                    break;
            }

        }
    };

}
