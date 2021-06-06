package com.example.etessera20.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.etessera20.R;
import com.example.etessera20.models.Utilizator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText et_nume;
    private EditText et_password;
    private EditText et_email;
    private ProgressBar progressBar;
    private Button register_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth =FirebaseAuth.getInstance();

        et_nume = findViewById(R.id.et_nume_register);
        et_email = findViewById(R.id.et_email_register);
        et_password = findViewById(R.id.et_password_register);
        progressBar = findViewById(R.id.progressBar_register);
        register_button = findViewById(R.id.bt_register);
        progressBar.setVisibility(View.INVISIBLE);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser(){
        String nume = et_nume.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if(nume.isEmpty()){
            et_nume.setError("Numele este necesar!");
            et_nume.requestFocus();
            return;
        }

        if(email.isEmpty()){
            et_email.setError("emailul este necesar!");
            et_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Introduceti un email valid!");
            et_email.requestFocus();
            return;
        }

        if(password.isEmpty()){
            et_password.setError("Parola este necesara!");
            et_password.requestFocus();
            return;
        }

        if(password.length() < 6){
            et_password.setError("Parola trebuie sa contina minim 6 caractere!");
            et_password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Utilizator utilizator = new Utilizator(email,password,nume,0, "no card");

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(utilizator).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, "Utilizatorul a fost inregistrat!", Toast.LENGTH_LONG).show();
                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, "A aparut o eroare!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(RegisterActivity.this, "A aparut o eroare!", Toast.LENGTH_LONG);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}
