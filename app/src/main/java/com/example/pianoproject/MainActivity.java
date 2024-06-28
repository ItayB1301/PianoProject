package com.example.pianoproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences sp;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    MediaPlayer mediaPlayer;

    EditText email,pass;

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
        mediaPlayer.release();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth= FirebaseAuth.getInstance();

        sp = getSharedPreferences("User", MODE_PRIVATE);
        String json = sp.getString("User", "");
        if (!json.equals("")) {
            Intent intent = new Intent(MainActivity.this, FrontPageActivity.class);
            startActivity(intent);
            finish();
        }
        else {

            //media player with a song
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.opening_music);
            mediaPlayer.setVolume(0.2f, 0.2f);
            mediaPlayer.start();


            email = (EditText) findViewById(R.id.etEmail);
            pass = (EditText) findViewById(R.id.etPass);

            Button login = findViewById(R.id.login);
            Button register = findViewById(R.id.register);
            login.setOnClickListener(this);
            register.setOnClickListener(this);
        }
    }

    public void loginUser(){
        String m = email.getText().toString();
        String p = pass.getText().toString();
        if(p.equals("")||m.equals(""))
            return;
        firebaseAuth.signInWithEmailAndPassword(m, p)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    String uid=firebaseAuth.getCurrentUser().getUid();
                                    sp=getSharedPreferences("User",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=sp.edit();
                                    editor.putString("User",uid);
                                    editor.apply();
                                    Intent intent = new Intent(MainActivity.this,FrontPageActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
   }

    @Override
    public void onClick(View v){
        if (v.getId()==R.id.login)
            loginUser();
        else if(v.getId()==R.id.register){
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        }

    }

}