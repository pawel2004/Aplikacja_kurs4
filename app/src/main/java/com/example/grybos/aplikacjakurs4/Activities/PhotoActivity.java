package com.example.grybos.aplikacjakurs4.Activities;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.grybos.aplikacjakurs4.R;

import java.io.File;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class PhotoActivity extends AppCompatActivity {

    //zmienne
    private ImageView image;
    private ImageView delete;
    private ImageView edit;
    private String photo_path;
    private File image_file;
    private File[] delete_array;
    private int size = 0;
    private int sizeX, sizeY;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        getSupportActionBar().hide();

        image = findViewById(R.id.image);
        delete = findViewById(R.id.delete);
        edit = findViewById(R.id.edit);

        Bundle bundle = getIntent().getExtras();

        photo_path = bundle.getString("path");

        image_file = new File(photo_path);

        bmp = betterImageDecode(photo_path, size);    // funkcja betterImageDecode opisana jest poniżej
        image.setImageBitmap(bmp);

        sizeX = bmp.getWidth();
        sizeY = bmp.getHeight();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(PhotoActivity.this);
                alert.setTitle("Usunięcie zdjecia");
                alert.setMessage("Czy na pewno?");

                alert.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        image_file.delete();

                        //startActivity(new Intent(PhotoActivity.this, AlbumsActivity.class));

                        setResult(RESULT_OK);

                        finish();
                    }
                });

                alert.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alert.show();

            }
        });

    }

    private Bitmap betterImageDecode(String filePath, int size) {
        Bitmap myBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();    //opcje przekształcania bitmapy
        options.inSampleSize = size; // zmniejszenie jakości bitmapy 4x
        options.inScaled = true;
        //
        myBitmap = BitmapFactory.decodeFile(filePath, options);
        return myBitmap;
    }
}
