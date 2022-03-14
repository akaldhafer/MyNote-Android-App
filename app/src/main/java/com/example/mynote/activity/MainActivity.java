package com.example.mynote.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mynote.R;
import com.example.mynote.auth.LoginPage;
import com.example.mynote.auth.RegisterPage;
import com.example.mynote.encryption.SymmtCrypto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar ().hide (); //This Line hides the action bar

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String email = user.getEmail();
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
            if(emailVerified){
                FirebaseFirestore docRef = FirebaseFirestore.getInstance();
                docRef.collection("UserData").document(email).
                        get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //decrypt the data
                                SymmtCrypto symmtCrypto = new SymmtCrypto();

                                // fetch the data
                                String name = document.getString("name");
                                String email= document.getString("email");
                                String password= document.getString("password");
                                String phone= document.getString("phone");
                                String address= document.getString("address");

                                Toast.makeText(MainActivity.this, "Find what is new !",Toast.LENGTH_LONG).show();

                                try {
                                    Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("name", symmtCrypto.decrypt(name));
                                    intent.putExtra("email",  email);
                                    intent.putExtra("address", symmtCrypto.decrypt(address));
                                    intent.putExtra("phone",  symmtCrypto.decrypt(phone));
                                    intent.putExtra("password",  symmtCrypto.decrypt(password));
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "Please re-login !",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(MainActivity.this, LoginPage.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }


                            } else {
                                Toast.makeText(MainActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, LoginPage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, LoginPage.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
            else{
                //call verify email ui
                Intent intent = new Intent(MainActivity.this, LoginPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
        else{
            setContentView(R.layout.activity_main);
        }


    }

    public void onRegisterActivity(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onLoginActivity(View view) {
        Intent intent = new Intent(MainActivity.this, LoginPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}