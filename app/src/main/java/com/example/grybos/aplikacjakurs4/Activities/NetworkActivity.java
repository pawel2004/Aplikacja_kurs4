package com.example.grybos.aplikacjakurs4.Activities;

import android.os.Bundle;

import com.example.grybos.aplikacjakurs4.R;

import androidx.appcompat.app.AppCompatActivity;


public class NetworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        getSupportActionBar().hide();

    }
}