package com.example.mynote.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynote.R;
import com.example.mynote.auth.LoginPage;
import com.example.mynote.encryption.SymmtCrypto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class UserProfile extends AppCompatActivity {
    TextView veEmail;
    EditText edName;
    ImageView imageView;
    String email, name, imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide (); //This Line hides the action bar
        setContentView(R.layout.activity_user_profile);
        veEmail = findViewById(R.id.profile_Email);
        edName = findViewById(R.id.profile_Name);
        imageView = findViewById(R.id.setImage);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        email = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail().trim();
        veEmail.setText(email);
        //fetch user name
        FirebaseFirestore.getInstance().collection("UserData").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String currentName = task.getResult().getString("name");
                    System.out.println(currentName);
                    SymmtCrypto symmtCrypto = new SymmtCrypto();
                    try {
                        name = symmtCrypto.decrypt(currentName);
                        System.out.println(name);
                        edName.setText(name);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickUpdateButton(View view) {
        validate();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    void validate(){
        String uEmail = veEmail.getText().toString().trim();
        String uName = edName.getText().toString().trim();

        if (!TextUtils.isEmpty(uEmail)
                && !TextUtils.isEmpty(uName)) {
            UpdateName(uEmail, uName);
        } else {
            if (TextUtils.isEmpty(uEmail)) {
                veEmail.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(uName)) {
                edName.setError("Name is required");
            }

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    void UpdateName(String uEmail, String uName){
        System.out.println(uEmail+ uName);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference record = firebaseFirestore.collection("UserData").document(uEmail);
        SymmtCrypto d = new SymmtCrypto();

        try {
            String updatedName = d.encrypt(uName);
            record.update("name",updatedName)
                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UserProfile.this, "Name Update !", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserProfile.this, "Could not update now !",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void onClickLogOutButton(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserProfile.this, LoginPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserProfile.this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}