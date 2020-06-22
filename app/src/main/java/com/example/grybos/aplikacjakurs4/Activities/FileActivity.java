package com.example.grybos.aplikacjakurs4.Activities;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.grybos.aplikacjakurs4.Helpers.DatabaseManager;
import com.example.grybos.aplikacjakurs4.R;
import com.example.grybos.aplikacjakurs4.Adapters.TestAdapter;

import java.io.File;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.grybos.aplikacjakurs4.Activities.AlbumsActivity.path_dir;

public class FileActivity extends AppCompatActivity {

    //zmienne
    private String path;
    private File dir;
    private File[] files;
    private String imagepath;
    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams params;
    private int id = 0;
    private Point size;
    private ListView listView;
    private TestAdapter adapter;
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        getSupportActionBar().hide();

        listView = findViewById(R.id.listView);

        path = path_dir;

        dir = new File(path);

        files = dir.listFiles();

        ArrayList<File> list = new ArrayList();

        for (File file : files) {
            //jeśli File jest plikiem
            if (file.isFile()) {

                list.add(file);

            }
        }

        Log.d("xxx", "Pliki: " + list.size());

        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        params = new LinearLayout.LayoutParams(size.x - 100, size.y / 3);

        db = new DatabaseManager(
                FileActivity.this, // activity z galerią zdjęć
                "NotatkiGrybosPawel2.db", // nazwa bazy
                null,
                6 //wersja bazy, po zmianie schematu bazy należy ją zwiększyć
        );

        adapter = new TestAdapter(
                FileActivity.this,
                R.layout.listview_layout2,
                list, db
        );
        listView.setAdapter(adapter);

        //refresh();

    }

    @Override
    protected void onResume() {
        super.onResume();

        path = path_dir;

        dir = new File(path);

        files = dir.listFiles();

        ArrayList<File> list = new ArrayList();

        for (File file : files) {
            //jeśli File jest plikiem
            if (file.isFile()) {

                list.add(file);

            }
        }

        adapter = new TestAdapter(
                FileActivity.this,
                R.layout.listview_layout2,
                list,db
        );

        Log.d("xxx", "Wracam");

        listView.setAdapter(adapter);

    }

    //    private Bitmap betterImageDecode(String filePath) {
//        Bitmap myBitmap;
//        BitmapFactory.Options options = new BitmapFactory.Options();    //opcje przekształcania bitmapy
//        options.inSampleSize = 4; // zmniejszenie jakości bitmapy 4x
//        //
//        myBitmap = BitmapFactory.decodeFile(filePath, options);
//        return myBitmap;
//    }

//    private void refresh(){
//
//        id = 0;
//
//        linearLayout.removeAllViews();
//
//        path = path_dir;
//
//        dir = new File(path);
//
//        files = dir.listFiles();
//
//        Log.d("pliki", Arrays.toString(files));
//
////        for (File file : files) {
////            //jeśli File jest plikiem
////            if (file.isFile()) {
////                imageview = new ImageView(FileActivity.this);
////                imagepath = file.getPath();                   // pobierz scieżkę z obiektu File
////                Bitmap bmp = betterImageDecode(imagepath);    // funkcja betterImageDecode opisana jest poniżej
////                imageview.setImageBitmap(bmp);// wstawienie bitmapy do ImageView
////                imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
////                imageview.setLayoutParams(params);
////                imageview.setPadding( size.x / 4, size.y / 100, 0, size.y / 100 );
////                linearLayout.addView(imageview);
////                imageview.setId(id);
////                id = id + 1;
////
////                imageview.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////
////                        int tmp = v.getId();
////
////                        Log.d("id: ", tmp + "");
////
////                        Intent intent = new Intent();
////                        intent.setClassName("com.example.grybos.aplikacjakurs4", "com.example.grybos.aplikacjakurs4.Activities.PhotoActivity");
////                        intent.putExtra("path", files[tmp].getPath());
////                        intent.putExtra("dir", dir.getPath());
////                        startActivityForResult(intent, 20);
////
////                    }
////                });
////            }
////        }
////
////    }
////
//    @Override
//   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 20){
//
//            if (resultCode == RESULT_OK){
//
//
//
//            }
//
//        }
//
//    }

}
