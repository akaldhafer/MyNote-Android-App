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
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynote.R;
import com.example.mynote.encryption.SymmtCrypto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.Objects;

public class ViewNote extends AppCompatActivity {
    String vTitle, vbody, vToken,vEmail, vid;
    EditText edTitle, edBody;
    TextView updatedAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide (); //This Line hides the action bar
        setContentView(R.layout.activity_view_note);

        edTitle = findViewById(R.id.upTitle);
        edBody = findViewById(R.id.upBody);
        updatedAt =findViewById(R.id.lastUpdate);

        vTitle = getIntent().getStringExtra("title");
        vid = getIntent().getStringExtra("id");
        vbody = getIntent().getStringExtra("body");
        vEmail = getIntent().getStringExtra("email");
        vToken = getIntent().getStringExtra("token");

        updatedAt.setText(new StringBuilder().append(updatedAt.getText()).append(" ").append(vToken).toString());
        edTitle.setText(vTitle);
        edBody.setText(vbody);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onUpdateNoteClick(View view) {
        validate();
    }

    public void onDeleteNoteClick(View view) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference reference = firebaseFirestore.collection("NoteData").document(vid);
        reference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ViewNote.this, "Note Deleted !", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ViewNote.this, HomePageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewNote.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void validate(){
        String title = edTitle.getText().toString().trim();
        String body = edBody.getText().toString().trim();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String token = timestamp.toString().trim();

        if(!TextUtils.isEmpty(title)
                && !TextUtils.isEmpty(body)
                && !TextUtils.isEmpty(token)){
            SymmtCrypto s = new SymmtCrypto();

            try {

                update(vid, s.encrypt(title), s.encrypt(body), vEmail, token);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ViewNote.this, "Could not encrypt !",Toast.LENGTH_LONG).show();
            }
        }else{
            if(TextUtils.isEmpty(title)){
                edTitle.setError("Title is required");
                return;
            }
            if(TextUtils.isEmpty(body)){
                edBody.setError("Please type something");
            }
        }
    }
    void update(String uId, String uTitle,String uBody,String uEmail,String uToken){

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference reference = firebaseFirestore.collection("NoteData").document(uId);
        reference.update("title", uTitle, "body", uBody, "token",uToken)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ViewNote.this, "Note Updated !", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ViewNote.this, HomePageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewNote.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ViewNote.this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}