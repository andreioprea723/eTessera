package com.example.etessera20.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.etessera20.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText et_email;
    private EditText et_password;
    private Button login;
    private TextView forgotPass;
    private TextView register;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        login = findViewById(R.id.bt_login);
        forgotPass = findViewById(R.id.tv_forgotpas);
        register = findViewById(R.id.tv_register);
        progressBar = findViewById(R.id.progressBar_login);
        progressBar.setVisibility(View.INVISIBLE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpas();
            }
        });


    }

    private void userLogin(){
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

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

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this, "Verificati emailul pentru a valida adresa", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }else {
                    Toast.makeText(LoginActivity.this, "Autentificarea nu a avut succes, " +
                            "verificati daca emailul si parola sunt corecte", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void forgotpas(){
        String email = et_email.getText().toString().trim();

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

        progressBar.setVisibility(View.VISIBLE);

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Verificati emailul pentru a va reseta parola", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }else{
                    Toast.makeText(LoginActivity.this, "Mai incercati", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
