package com.example.grybos.aplikacjakurs4.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amitshekhar.DebugDB;
import com.example.grybos.aplikacjakurs4.Helpers.DatabaseManager;
import com.example.grybos.aplikacjakurs4.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //zmienne
    private String[] polish;
    private String[] english;
    private LinearLayout language_set;
    private TextView txt1;
    private TextView txt2;
    private TextView txt3;
    private TextView txt4;
    private TextView txt5;
    private TextView txt6;
    private TextView txt7;
    private SharedPreferences prefs;
    private int Lang;
    private LinearLayout camera;
    private LinearLayout albums;
    private LinearLayout collage;
    private LinearLayout network;
    private LinearLayout notes;
    private LinearLayout camera_api;
    public static File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    public static File dir = new File(pic, "Gryboś");
    File[] folders;
    private ArrayList <String> names = new ArrayList <String> ();
    String [] names_array;
    private ViewGroup.LayoutParams params;
    private byte[] byteArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        Log.d("xxx", DebugDB.getAddressLog());

        if (!Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).exists()){
            pic.mkdir();
        }

        File dir1 = new File(dir, "miejsca");
        File dir2 = new File(dir, "ludzie");
        File dir3 = new File(dir, "rzeczy");

        if (!dir.exists()){
            dir.mkdir();
            dir1.mkdir();
            dir2.mkdir();
            dir3.mkdir();
        }

        polish = getResources().getStringArray(R.array.polish);
        english = getResources().getStringArray(R.array.english);

        prefs = getSharedPreferences("language", Context.MODE_PRIVATE);

        Lang = prefs.getInt("lang", 0);

        language_set = findViewById(R.id.language_set);
        txt1 = findViewById(R.id.camera_text);
        txt2 = findViewById(R.id.albums_text);
        txt3 = findViewById(R.id.collage_text);
        txt4 = findViewById(R.id.network_text);
        txt6 = findViewById(R.id.notes_text);
        txt7 = findViewById(R.id.camera_api_text);
        txt5 = findViewById(R.id.language_text);

        camera = findViewById(R.id.camera_button);
        albums = findViewById(R.id.albums_button);
        collage = findViewById(R.id.collage_button);
        network = findViewById(R.id.network_button);
        notes = findViewById(R.id.notes_button);
        camera_api = findViewById(R.id.camera_api_button);

        setLanguage();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Wybierz metodę zrobienia zdjęcia: ");
                String[] opcje = {"aparat", "galeria"};
                alert.setItems(opcje, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("123", "Kliknięty: " + i);

                        switch (i)
                        {
                            case 0:

                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                //jeśli jest dostępny aparat
                                if (intent.resolveActivity(getPackageManager()) != null){

                                    startActivityForResult(intent, 200); // 200- stała wartość,
                                    //która posłuży do identyfikacji tej akcji

                                }
                                break;
                            case 1:
                                Intent intent1 = new Intent(Intent.ACTION_PICK);
                                intent1.setType("image/*");
                                startActivityForResult(intent1, 100);
                                break;
                        }

                    }
                });

                alert.show();
            }
        });

        albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newIntent(AlbumsActivity.class);

            }
        });

        collage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newIntent(ChooseCollageActivity.class);

            }
        });

        network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newIntent(NetworkActivity.class);

            }
        });

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newIntent(NoteActivity.class);

            }
        });

        camera_api.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.putExtra("key", 0);
                startActivity(intent);

            }
        });

        language_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Lang == 0){

                    Lang = 1;

                }

                else {

                    Lang = 0;

                }

                setLanguage();

            }
        });

    }

    private void setLanguage(){

        if (Lang == 0){

            txt1.setText(english[0]);
            txt2.setText(english[1]);
            txt3.setText(english[2]);
            txt4.setText(english[3]);
            txt6.setText(english[4]);
            txt7.setText(english[5]);
            txt5.setText(english[6]);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("lang", 0);
            editor.apply();

        }

        else{
            txt1.setText(polish[0]);
            txt2.setText(polish[1]);
            txt3.setText(polish[2]);
            txt4.setText(polish[3]);
            txt6.setText(polish[4]);
            txt7.setText(polish[5]);
            txt5.setText(polish[6]);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("lang", 1);
            editor.apply();
        }

    }

    private void newIntent(Class activity){

        startActivity(new Intent(MainActivity.this, activity));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200){
            if(resultCode == RESULT_OK){

                Bundle extras = data.getExtras();
                Bitmap b = (Bitmap) extras.get("data");

                save(b);

            }

        }

        else if (requestCode == 100){

            Uri imgData = data.getData();
            InputStream stream = null;
            try {
                stream = getContentResolver().openInputStream(imgData);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap b = BitmapFactory.decodeStream(stream);

            save(b);

        }
    }

    private void save(Bitmap b){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, stream); // kompresja, typ pliku jpg, png
        byteArray = stream.toByteArray();

        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Gdzie chcesz zapisać zdjęcie?");
        folders = dir.listFiles();
        for (File file : folders){

            names.add(file.getName());

        }

        names_array = names.toArray(new String[0]);

        names.clear();

        Log.d("names", Arrays.toString(names_array));

        alert.setItems(names_array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Log.d("path", "Path: " + folders[i]);

                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String d = df.format(new Date());

                FileOutputStream fs = null;
                try {
                    fs = new FileOutputStream(folders[i] + "/" + d + ".jpg");
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
        ImageView image = new ImageView(MainActivity.this);
        image.setImageBitmap(b);
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image.setLayoutParams(params);
        alert.setView(image);
        alert.show();

    }
}
