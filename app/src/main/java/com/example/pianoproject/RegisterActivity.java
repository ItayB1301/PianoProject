package com.example.pianoproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etFName, etLName, etPhone, etEmail, etPass;
    DatabaseReference userRef;
    FirebaseAuth firebaseAuth;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        etFName = findViewById(R.id.firstname);
        etLName = findViewById(R.id.lastname);
        etPhone = findViewById(R.id.phone);
        etEmail = findViewById(R.id.email);
        etPass = findViewById(R.id.pass);
        ((Button) findViewById(R.id.btnRegister)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnRegCancel)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegister) {
            User user = userDataOk();
            if (user != null)
                regUser(user, etPass.getText().toString());
            else if (v.getId() == R.id.btnRegCancel) {
                finish();
            }
        }
    }

    public void regUser(User user, String pass) {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("REG", "createUserWithEmailAndPassword:success");
                    user.setUid(Objects.requireNonNull(firebaseAuth.getCurrentUser().getUid()));
                    saveUserDB(user);
                    Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private void saveUserDB(User user) {
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.child(user.getUid()).setValue(user);
        sp = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("User", user.getUid());
        editor.apply();
    }

    private User userDataOk() {
        if (etFName.getText().toString().isEmpty() ||
                etLName.getText().toString().isEmpty() ||
                etPhone.getText().toString().isEmpty() ||
                etEmail.getText().toString().isEmpty() ||
                etPass.getText().toString().isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Missing data", Toast.LENGTH_LONG).show();
            return null;
        }

        if (!etPhone.getText().toString().startsWith("0") ||
                etPhone.getText().toString().length() < 10) {
            Toast.makeText(RegisterActivity.this, "Invalid phone number", Toast.LENGTH_LONG).show();
            return null;
        }

        if (etPass.getText().toString().length() < 8) {
            Toast.makeText(RegisterActivity.this, "Password is too short", Toast.LENGTH_LONG).show();
            return null;
        }

        User user = new User((etFName.getText().toString()), etLName.getText().toString(), etPhone.getText().toString(), etEmail.getText().toString());
        return user;
    }

}