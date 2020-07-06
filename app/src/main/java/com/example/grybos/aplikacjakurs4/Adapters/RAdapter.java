package com.example.grybos.aplikacjakurs4.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grybos.aplikacjakurs4.Helpers.Item;
import com.example.grybos.aplikacjakurs4.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RAdapter extends RecyclerView.Adapter<RAdapter.ViewHolder> {

    private ArrayList<Item> list;

    public RAdapter(ArrayList<Item> list){
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Item listItem = list.get(position);

        holder.timeTxt.setText(listItem.getTime());
        holder.sizeTxt.setText(listItem.getSize());
        Picasso.get().load("http://kurs4.spec.pl.hostingasp.pl/test_uploadu/small_collages/" + listItem.getName()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        Log.d("xxxx", list.size() + "");
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView timeTxt;
        private TextView sizeTxt;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            timeTxt = (TextView) itemView.findViewById(R.id.timeTxt);
            sizeTxt = (TextView) itemView.findViewById(R.id.sizeTxt);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

        }

    }
}
