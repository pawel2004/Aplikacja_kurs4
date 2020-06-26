package com.example.grybos.aplikacjakurs4.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.grybos.aplikacjakurs4.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.grybos.aplikacjakurs4.Activities.MainActivity.dir;

public class AlbumsActivity extends AppCompatActivity {

    //zmienne
    private ListView listView;
    ArrayList <String> array = new ArrayList<String>();
    private ImageView add_new;
    private boolean isCreated;
    private EditText input;
    private int index;
    File[] files;
    public static String path_dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        getSupportActionBar().hide();

        listView = findViewById(R.id.list1);

        add_new = findViewById(R.id.add);

        files = dir.listFiles(); // tablica plików
        refresh();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Tag", "numer klikanego wiersza w ListView = " + i);
                index = i;
                Intent intent = new Intent(AlbumsActivity.this, FileActivity.class);
                Log.d("file", "" + files[index]);
                path_dir = files[index].getPath();
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Tag", "numer przytrzymywanego wiersza w ListView = " + i);
                index = i;

                AlertDialog.Builder alert = new AlertDialog.Builder(AlbumsActivity.this);
                alert.setTitle("Usunięcie katalogu");
                alert.setMessage("Czy na pewno?");
//ok
                alert.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //wyświetl zmienną which
                        Log.d("delete", files[index].getName());
                        if (files[index].length() != 0){
                            for (File file : files[index].listFiles()){
                                file.delete();
                            }
                            files[index].delete();
                        }
                        files = dir.listFiles();
                        refresh();
                    }

                });

//no
                alert.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //wyświetl which
                       files = dir.listFiles(); // tablica plików
                        refresh();
                    }
                });
//
                alert.show();
                return true;
            }
        });

        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(AlbumsActivity.this);
                alert.setTitle("Nowy folder");
                alert.setMessage("Wpisz nazwę:");
                //tutaj input
                final EditText input = new EditText(AlbumsActivity.this);
                input.setText("Podaj nazwę pliku");
                alert.setView(input);
                //teraz butony jak poprzednio i
                //Stwórz
                alert.setPositiveButton("Stwórz", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //wyświetl zmienną which
                        isCreated = true;

                        if(input.getText().toString().length() > 0) {

                            File new_dir = new File(dir, input.getText().toString());
                            if (!new_dir.exists()) {
                                new_dir.mkdir();
                            }

                        }
                        files = dir.listFiles(); // tablica plików
                        refresh();
                    }

                });

                //Anuluj
                alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isCreated = false;
                        files = dir.listFiles(); // tablica plików
                        refresh();
                    }
                });
                alert.show();
            }
        });

    }

    private void refresh (){

        array.clear();

        Log.d("tablica", Arrays.toString(files));

        for (File file : files){

            Log.d("name", file.getName());
            array.add(file.getName());

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                AlbumsActivity.this,
                R.layout.listview_layout,
                R.id.txt1,
                array
        );

        listView.setAdapter(adapter);

    }

}
