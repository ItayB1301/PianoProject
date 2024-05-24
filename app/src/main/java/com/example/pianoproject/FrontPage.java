package com.example.pianoproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;

public class FrontPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        ((ImageButton) findViewById(R.id.c4)).setOnClickListener(this::myClick);



    }

    public void myClick(View v){
        int id = v.getId();
    }
}