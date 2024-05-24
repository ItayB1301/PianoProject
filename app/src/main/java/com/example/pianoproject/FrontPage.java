package com.example.pianoproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;

public class FrontPage extends AppCompatActivity implements View.OnClickListener {

    private ImageButton C4;
    private ImageButton C4sharp;
    private ImageButton D4;
    private ImageButton D4sharp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        ImageButton C4=findViewById(R.id.c4);


        C4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int x=v.getId();
        if (R.id.c4==x)
            finish();//produce note
        else if (R.id.c4sharp==x)
            finish();//produce note
        else if (R.id.d4==x)
            finish();//produce note
        else if (R.id.d4sharp==x)
            finish();//produce note
        else if (R.id.e4==x)
            finish();//produce note
        else if (R.id.f4==x)
            finish();//produce note
        else if (R.id.f4sharp==x)
            finish();//produce note
    }
}