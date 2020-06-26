package com.example.grybos.aplikacjakurs4.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.grybos.aplikacjakurs4.Helpers.ImageData;
import com.example.grybos.aplikacjakurs4.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.grybos.aplikacjakurs4.Activities.MainActivity.dir;

public class CollageActivity extends AppCompatActivity {

    private FrameLayout frame1;
    private ArrayList<ImageData> list = new ArrayList<>();
    private Bitmap imageBitmap;
    private int id;
    private ArrayList<ImageView> images= new ArrayList<ImageView>();
    private int tmp;
    private ImageView rotate;
    private ImageView flip;
    private ImageView ok;
    private int tmp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage);

        getSupportActionBar().hide();

        frame1 = findViewById(R.id.frame1);

        frame1.setDrawingCacheEnabled(true);

        rotate = findViewById(R.id.rotate);

        flip = findViewById(R.id.flip);

        ok = findViewById(R.id.ok);

        buildFrame();

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Matrix matrix = new Matrix();
                matrix.postRotate(90);
//
                Bitmap oryginal = ((BitmapDrawable) images.get(tmp2).getDrawable()).getBitmap();
                Bitmap rotated = Bitmap.createBitmap(oryginal, 0, 0, oryginal.getWidth(), oryginal.getHeight(), matrix, true);
//
                images.get(tmp2).setImageBitmap(rotated);

            }
        });

        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Matrix matrix = new Matrix();
                matrix.postScale(-1.0f, 1.0f);
//
                Bitmap oryginal = ((BitmapDrawable) images.get(tmp2).getDrawable()).getBitmap();
                Bitmap fliped = Bitmap.createBitmap(oryginal, 0, 0, oryginal.getWidth(), oryginal.getHeight(), matrix, true);
//
                images.get(tmp2).setImageBitmap(fliped);

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(CollageActivity.this);
                alert.setMessage("Czy chcesz zapisać kolaż?");
                alert.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bitmap b = frame1.getDrawingCache(true);

                        File dir1 = new File(dir, "kolaże");

                        if (!dir1.exists()){

                            dir1.mkdir();

                        }

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        b.compress(Bitmap.CompressFormat.JPEG, 100, stream); // kompresja, typ pliku jpg, png
                        byte[] byteArray = stream.toByteArray();

                        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        String d = df.format(new Date());

                        FileOutputStream fs = null;
                        try {
                            fs = new FileOutputStream(dir1.getPath() + "/" + d);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            fs.write(byteArray);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            fs.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(CollageActivity.this, PhotoActivity.class);
                        intent.putExtra("path", dir1.getPath() + "/" + d);
                        startActivity(intent);

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

    private void buildFrame(){

        images.clear();

        frame1.removeAllViews();
        id = 0;

        list = (ArrayList<ImageData>) getIntent().getExtras().getSerializable("list");

        for (ImageData data : list){

            final ImageView iv = new ImageView(CollageActivity.this);
            iv.setBackgroundColor(0xFFFFFFFF);
            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            iv.setX(data.getX());
            iv.setY(data.getY());
            iv.setId(id);
            iv.setImageResource(R.drawable.baseline_add_black_18dp);
            iv.setLayoutParams(new LinearLayout.LayoutParams(data.getW(), data.getH()));
            images.add(iv);

            iv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    Intent intent = new Intent(CollageActivity.this, CameraActivity.class);
                    intent.putExtra("key", 1);
                    startActivityForResult(intent, 444);

                    Log.d("yyy", "id: " + view.getId());

                    tmp = view.getId();

                    return true;
                }
            });

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tmp2 = v.getId();

                }
            });

            frame1.addView(iv);

            id = id + 1;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 444 && resultCode == 888) {
            Bundle extras = data.getExtras();
            byte[] fotoData = (byte[]) extras.get("fotoData"); // na tym etapie warto sprawdzić długość tablicy
            // teraz konwersja byte[] na bitmap
            imageBitmap = BitmapFactory.decodeByteArray(fotoData, 0, fotoData.length);
            // powyższą bitmapę można już wyświetlić w ImageView

            images.get(tmp).setImageBitmap(imageBitmap);
            images.get(tmp).setScaleType(ImageView.ScaleType.CENTER_CROP);

        }

    }
}
