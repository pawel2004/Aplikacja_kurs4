package com.example.grybos.aplikacjakurs4.Activities;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.grybos.aplikacjakurs4.Helpers.Networking;
import com.example.grybos.aplikacjakurs4.Helpers.UploadFoto;
import com.example.grybos.aplikacjakurs4.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.grybos.aplikacjakurs4.Helpers.Settings.URL_SERWERA;

public class PhotoActivity extends AppCompatActivity {

    //zmienne
    private ImageView image;
    private ImageView delete;
    private ImageView crop;
    private ImageView rotate;
    private ImageButton upload;
    private ImageButton share;
    private String photo_path;
    private File image_file;
    private File[] delete_array;
    private int size = 0;
    private int sizeX, sizeY;
    private Bitmap bmp;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        getSupportActionBar().hide();

        image = findViewById(R.id.image);
        delete = findViewById(R.id.delete);
        crop = findViewById(R.id.crop);
        rotate = findViewById(R.id.rotate);
        upload = findViewById(R.id.bt1);
        share = findViewById(R.id.bt2);

        pDialog = new ProgressDialog(PhotoActivity.this);
        pDialog.setMessage("Ładuję...");
        pDialog.setCancelable(false);

        Bundle bundle = getIntent().getExtras();

        photo_path = bundle.getString("path");

        image_file = new File(photo_path);

        bmp = betterImageDecode(photo_path, size);    // funkcja betterImageDecode opisana jest poniżej
        image.setImageBitmap(bmp);

        sizeX = bmp.getWidth();
        sizeY = bmp.getHeight();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Networking.checkConnection(PhotoActivity.this)){

                    new UploadFoto(PhotoActivity.this, pDialog, photo_path).execute(URL_SERWERA);

                }
                else {

                    AlertDialog.Builder alert = new AlertDialog.Builder(PhotoActivity.this);
                    alert.setTitle("Nie ma połączenia z Internetem!");
                    alert.setCancelable(false);
                    alert.setNeutralButton("OK", null);
                    alert.show();

                }

            }
        });

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

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(PhotoActivity.this);
                alert.setTitle("To nie działa na wszystkich urządzeniach!");
                alert.setCancelable(false);
                alert.setNeutralButton("OK", null);
                alert.show();

//                crop();

            }
        });

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Matrix matrix = new Matrix();
                matrix.postRotate(90);
//
                Bitmap oryginal = ((BitmapDrawable) image.getDrawable()).getBitmap();
                Bitmap rotated = Bitmap.createBitmap(oryginal, 0, 0, oryginal.getWidth(), oryginal.getHeight(), matrix, true);

                image.setImageBitmap(rotated);
//
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                rotated.compress(Bitmap.CompressFormat.JPEG, 100, stream); // kompresja, typ pliku jpg, png
                byte[] byteArray = stream.toByteArray();

                FileOutputStream fs = null;
                try {
                    fs = new FileOutputStream(photo_path);
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

    private void crop(){

        File foto = new File(photo_path);
        Uri uri = Uri.fromFile(foto);

        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", true);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, 222);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 222){

            Bundle extras = data.getExtras();
            Bitmap b = (Bitmap) extras.getParcelable("data");


        }

    }
}
