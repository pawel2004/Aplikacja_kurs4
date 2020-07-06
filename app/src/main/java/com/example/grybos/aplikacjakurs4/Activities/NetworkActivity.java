package com.example.grybos.aplikacjakurs4.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.grybos.aplikacjakurs4.Adapters.RAdapter;
import com.example.grybos.aplikacjakurs4.Helpers.Item;
import com.example.grybos.aplikacjakurs4.Helpers.Networking;
import com.example.grybos.aplikacjakurs4.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.grybos.aplikacjakurs4.Helpers.Settings.JSON_URL;


public class NetworkActivity extends AppCompatActivity {

    //zmienne
    private ArrayList<Item> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private RAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerView);

        if (Networking.checkConnection(NetworkActivity.this)){

            layoutManager = new LinearLayoutManager(NetworkActivity.this);
            // LinearLayoutManager.HORIZONTAL, false
            recyclerView.setLayoutManager(layoutManager);

            loadJSON();

        }

        else {

            AlertDialog.Builder alert = new AlertDialog.Builder(NetworkActivity.this);
            alert.setTitle("Nie ma połączenia z Internetem!");
            alert.setCancelable(false);
            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    finish();

                }
            });
            alert.show();

        }

    }

    private void loadJSON() {

        //request z Volleya

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(NetworkActivity.this, response, Toast.LENGTH_LONG).show();

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("ImagesList"); // nazwa tablicy w obiekcie zwracanym przez spec-a
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                Item listItem = new Item(
                                        obj.getString("IMAGE_NAME"),
                                        "Czas zapisu: " + obj.getString("IMAGE_SAVE_TIME"),
                                        "Wielkość zdjęcia: " + obj.getString("IMAGE_SIZE")

                                );
                                list.add(listItem);
                            }
                            adapter = new RAdapter(list);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("xxx", "error" + error.getMessage());
                    }
                }
        );
        //
        RequestQueue requestQueue = Volley.newRequestQueue(NetworkActivity.this);
        requestQueue.add(stringRequest);
    }

}
