package com.example.mynote.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mynote.R;
import com.example.mynote.activity.HomePageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {
    EditText email , password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar ().hide (); //This Line hides the action bar
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.text_email);
        password = findViewById(R.id.password);
    }

    public void onRegisterActivity(View view) {
        Intent intent = new Intent(LoginPage.this, RegisterPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onLoginActivity(View view) {
        validate();
    }
    void validate(){
        String uEmail = email.getText().toString().trim();
        String uPassword = password.getText().toString().trim();

        if (!TextUtils.isEmpty(uEmail)
                && !TextUtils.isEmpty(uPassword)) {
            SignInWithEmailAndPassword(uEmail, uPassword);
        } else {
            if (TextUtils.isEmpty(uEmail)) {
                email.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(uPassword)) {
                password.setError("Password is required");
                return;
            }

        }
    }
    void SignInWithEmailAndPassword(String uEmail, String uPassword){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(uEmail,uPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LoginPage.this, "Welcome Back", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginPage.this, HomePageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        //fetch the current user info decrpt
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginPage.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginPage.this, RegisterPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

}