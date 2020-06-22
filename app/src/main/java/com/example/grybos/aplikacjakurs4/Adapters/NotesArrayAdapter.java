package com.example.grybos.aplikacjakurs4.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grybos.aplikacjakurs4.Helpers.Note;
import com.example.grybos.aplikacjakurs4.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NotesArrayAdapter extends ArrayAdapter {

    private ArrayList<Note> _list;
    private Context _context;
    private int _resource;

    public NotesArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList objects) {
        super(context, resource, objects);

        this._list = objects;
        this._context = context;
        this._resource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //inflater - klasa konwertująca xml na kod javy
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listview_layout3, null);
        //convertView = inflater.inflate(_resource, null);
        //szukamy każdego TextView w layoucie

        TextView id = (TextView) convertView.findViewById(R.id.id);
        id.setText(_list.get(position).getId());

        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(_list.get(position).getTitle());
        title.setTextColor(Color.parseColor(_list.get(position).getTicolor()));

        TextView text = (TextView) convertView.findViewById(R.id.text);
        text.setText(_list.get(position).getText());
        text.setTextColor(Color.parseColor(_list.get(position).getTecolor()));

        TextView file = (TextView) convertView.findViewById(R.id.file);
        file.setText(_list.get(position).getFile());

        //gdybyśmy chcieli klikać Imageview wewnątrz wiersza:
        ImageView iv1 = (ImageView) convertView.findViewById(R.id.image1);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // klik w obrazek
            }
        });

        return convertView;

    }
}
