package com.example.grybos.aplikacjakurs4.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.grybos.aplikacjakurs4.Activities.FileActivity;
import com.example.grybos.aplikacjakurs4.Activities.PhotoActivity;
import com.example.grybos.aplikacjakurs4.Helpers.DatabaseManager;
import com.example.grybos.aplikacjakurs4.R;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;


public class TestAdapter extends ArrayAdapter{

    private ArrayList<File> _list;
    private Context _context;
    private int _resource;
    private String Title;
    private String Text;
    private int editText_switch = 0;
    private String[] colors_array = {"#000000", "#c8c8c8", "#FFFF00", "#64DD17", "#0091EA"};
    private DatabaseManager db;
    private String color1 = colors_array[0];
    private String color2 = colors_array[0];

    public TestAdapter(@NonNull Context context, int resource, @NonNull ArrayList objects, DatabaseManager db) {
        super(context, resource, objects);
        this._list= objects;
        this._context = context;
        this._resource = resource;
        this.db = db;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(_resource, null);

        Uri uri = Uri.fromFile(new File(this._list.get(position).getPath()));

        Log.d("xxx", this._list.get(0).getPath());

        ImageView iv1 = (ImageView) convertView.findViewById(R.id.image1);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("xxx", "Klik w obrazek");

                Intent intent = new Intent(_context, PhotoActivity.class);
                intent.putExtra("path", _list.get(position).getPath());
                _context.startActivity(intent);

            }
        });

        iv1.setImageURI(uri);
        iv1.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ImageView iv2 = (ImageView) convertView.findViewById(R.id.remove);
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("xxx", "Klik w usunięcie");

                AlertDialog.Builder alert = new AlertDialog.Builder(_context);
                alert.setTitle("Usunięcie zdjecia");
                alert.setMessage("Czy na pewno?");

                alert.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        _list.get(position).delete();
                        _list.remove(position);
                        notifyDataSetChanged();

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

        ImageView iv3 = (ImageView) convertView.findViewById(R.id.info);
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("xxx", "Klik w info");
                AlertDialog.Builder alert = new AlertDialog.Builder(_context);
                alert.setTitle("Informacje o zdjęciu:");
                alert.setCancelable(false);
                alert.setMessage("Nazwa: " + _list.get(position).getName());
                alert.setMessage("Ścieżka: " + _list.get(position).getPath());
                alert.setNeutralButton("OK", null);  // null to pusty click
                alert.show();
            }
        });

        ImageView iv4 = (ImageView) convertView.findViewById(R.id.edit);
        iv4.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View view) {
                Log.d("xxx", "Klik w edycję");
                AlertDialog.Builder alert = new AlertDialog.Builder(_context);
                alert.setTitle("NOTATKA DO ZDJĘCIA");
                alert.setMessage("podaj tytuł i treść notatki oraz wybierz kolor tekstu");
                View editView = View.inflate(_context, R.layout.note_inputs_xml, null);
                final EditText title = (EditText) editView.findViewById(R.id.title);
                final EditText text = (EditText) editView.findViewById(R.id.text);
                LinearLayout colors = (LinearLayout) editView.findViewById(R.id.colors);

                title.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        editText_switch = 0;

                        Log.d("xxx", "Switch: " + editText_switch);

                        return false;
                    }
                });

                text.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        editText_switch = 1;

                        Log.d("xxx", "Switch: " + editText_switch);

                        return false;
                    }
                });

                for (int i = 0; i < colors_array.length; i++){

                    ImageView frame = new ImageView(_context);

                    frame.setLayoutParams(new LinearLayout.LayoutParams(100,100));

                    frame.setBackgroundColor(Color.parseColor(colors_array[i]));

                    frame.setId(i);

                    Log.d("xxx", "Id: " +
                            frame.getId());

                    frame.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Log.d("xxx", "Kliknięte: " + view.getId());

                            if (editText_switch == 0){

                                title.setTextColor(Color.parseColor(colors_array[view.getId()]));
                                color1 = colors_array[view.getId()];
                                Log.d("xxx", "kolor1: " + color1);

                            }
                            else {

                                text.setTextColor(Color.parseColor(colors_array[view.getId()]));
                                color2 = colors_array[view.getId()];
                                Log.d("xxx", "kolor2: " + color2);

                            }

                        }
                    });

                    colors.addView(frame);

                }
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        db.insert(title.getText().toString(), text.getText().toString(), color1, color2, _list.get(position).getPath());

                    }
                });
                alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.setView(editView);
                alert.show();
            }
        });

        return convertView;
    }
}
