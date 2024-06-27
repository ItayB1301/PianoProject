package com.example.pianoproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //media player with a song

        EditText name=(EditText) findViewById(R.id.et);
        Button btn=findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().equals("")){
                    startActivity(new Intent(MainActivity.this, FrontPageActivity.class));
                    finish();

                }
                else{
                    Toast.makeText(MainActivity.this,"Enter Your Name",Toast.LENGTH_SHORT).show();
                };

            }

        });
    }
}