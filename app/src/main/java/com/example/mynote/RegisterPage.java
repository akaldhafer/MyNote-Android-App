package com.example.mynote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mynote.encryption.SymmtCrypto;
import com.example.mynote.userapi.UserModel;
import com.example.mynote.userapi.UserUploadData;
import com.example.mynote.userapi.UserViewMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.sql.Timestamp;

public class RegisterPage extends AppCompatActivity {
    EditText email, name, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        email = findViewById(R.id.edit_email_register);
        name = findViewById(R.id.edit_name_register);
        password = findViewById(R.id.edit_password_register);


    }

    public void onClickRegisterButton(View view) {
        validate();
    }

    private void validate() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String token = timestamp.toString();
        String userName = name.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if (!TextUtils.isEmpty(userName)
                && !TextUtils.isEmpty(userEmail)
                && !TextUtils.isEmpty(userPassword)) {
            SignUpWithEmailAndPassword(userName, userEmail, userPassword, token);
        } else {
            if (TextUtils.isEmpty(userName)) {
                name.setError("Name is required");
                return;
            }
            if (TextUtils.isEmpty(userEmail)) {
                email.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(userPassword)) {
                password.setError("Password is required");
                return;
            }
        }

    }

    void SignUpWithEmailAndPassword(String uName, String uEmail, String uPassword, String uToken) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(uEmail, uPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        try {
                        SymmtCrypto en =new SymmtCrypto();

                        UserModel userModel = new UserModel(en.encrypt(uName), en.encrypt(uEmail),
                                    en.encrypt(uPassword), uToken);

                        FirebaseFirestore.getInstance().collection("UserData").document(uEmail)
                                .set(userModel, SetOptions.merge())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegisterPage.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterPage.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();

                            }
                        });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterPage.this, e.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void onClickLoginButton(View view) {
        Intent intent = new Intent(RegisterPage.this, LoginPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterPage.this, LoginPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}