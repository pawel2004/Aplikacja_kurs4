package com.example.grybos.aplikacjakurs4.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.grybos.aplikacjakurs4.Adapters.NotesArrayAdapter;
import com.example.grybos.aplikacjakurs4.Helpers.DatabaseManager;
import com.example.grybos.aplikacjakurs4.Helpers.Note;
import com.example.grybos.aplikacjakurs4.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class NoteActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseManager db;
    private ArrayList<Note> notes;
    private int editText_switch = 0;
    private String[] colors_array = {"#000000", "#c8c8c8", "#FFFF00", "#64DD17", "#0091EA"};
    private String color1;
    private String color2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        getSupportActionBar().hide();

        db = new DatabaseManager(
                NoteActivity.this, // activity z galerią zdjęć
                "NotatkiGrybosPawel2.db", // nazwa bazy
                null,
                6 //wersja bazy, po zmianie schematu bazy należy ją zwiększyć
        );

        listView = findViewById(R.id.list1);

        notes = db.getAll();

        final NotesArrayAdapter adapter = new NotesArrayAdapter(
                NoteActivity.this,
                R.layout.listview_layout3,
                notes
        );
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alert = new AlertDialog.Builder(NoteActivity.this);
                alert.setTitle("Wybierz opcję: ");
                String[] opcje = {"usuń", "edytuj", "sortuj wg tytułu", "sortuj wg koloru"};
                alert.setItems(opcje, new DialogInterface.OnClickListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which){

                            case 0:
                                db.delete(notes.get(position).getId());
                                refresh(false);
                                break;
                            case 1:
                                AlertDialog.Builder alert = new AlertDialog.Builder(NoteActivity.this);
                                alert.setTitle("EDYCJA NOTATKI:");
                                alert.setMessage("podaj tytuł i treść notatki oraz wybierz kolor tekstu");
                                View editView = View.inflate(NoteActivity.this, R.layout.note_inputs_xml, null);
                                final EditText title = (EditText) editView.findViewById(R.id.title);
                                final EditText text = (EditText) editView.findViewById(R.id.text);
                                LinearLayout colors = (LinearLayout) editView.findViewById(R.id.colors);

                                color1 = notes.get(position).getTicolor();
                                color2 = notes.get(position).getTecolor();

                                title.setText(notes.get(position).getTitle());
                                title.setTextColor(Color.parseColor(notes.get(position).getTicolor()));

                                text.setText(notes.get(position).getText());
                                text.setTextColor(Color.parseColor(notes.get(position).getTecolor()));

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

                                    ImageView frame = new ImageView(NoteActivity.this);

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

                                        db.edit(notes.get(position).getId(), title.getText().toString(), text.getText().toString(), color1, color2);
                                        refresh(false);

                                    }
                                });
                                alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                alert.setView(editView);
                                alert.show();
                                break;
                            case 2:
                                Collections.sort(notes, new Comparator<Note>() {
                                    @Override
                                    public int compare(Note a, Note b) {
                                        return a.getTitle().compareTo(b.getTitle());
                                    }
                                });
                                refresh(true);
                                break;
                            case 3:
                                Collections.sort(notes, new Comparator<Note>() {
                                    @Override
                                    public int compare(Note a, Note b) {
                                        return a.getTicolor().compareTo(b.getTicolor());
                                    }
                                });
                                refresh(true);
                                break;

                        }
                    }
                });
                alert.show();

                return true;
            }
        });

    }

    private void refresh(boolean isSorted){

        if (isSorted){

        }else {

            notes = db.getAll();

        }

        final NotesArrayAdapter adapter = new NotesArrayAdapter(
                NoteActivity.this,
                R.layout.listview_layout3,
                notes
        );
        listView.setAdapter(adapter);

    }

}
