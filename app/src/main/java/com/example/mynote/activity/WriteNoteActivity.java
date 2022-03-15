package com.example.mynote.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mynote.R;
import com.example.mynote.auth.RegisterPage;
import com.example.mynote.encryption.SymmtCrypto;
import com.example.mynote.noteapi.NoteUploadData;
import com.example.mynote.noteapi.NoteViewMessage;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Timestamp;
import java.util.Objects;

public class WriteNoteActivity extends AppCompatActivity implements NoteViewMessage {
    EditText edTitle, edBody;
    NoteUploadData noteUploadData;
    String vTitle, vbody, view, vToken,vEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide (); //This Line hides the action bar
        setContentView(R.layout.activity_write_note);

        edTitle = findViewById(R.id.edTitle);
        edBody = findViewById(R.id.edBody);




        noteUploadData = new NoteUploadData(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onSaveNoteClick(View view) {
        validate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void validate(){
        String title = edTitle.getText().toString().trim();
        String body = edBody.getText().toString().trim();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String token = timestamp.toString().trim();
        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        if(!TextUtils.isEmpty(title)
        && !TextUtils.isEmpty(body)
        && !TextUtils.isEmpty(token)){
            SymmtCrypto s = new SymmtCrypto();

            try {
                noteUploadData.onSuccessUpdate(WriteNoteActivity.this,token,email,s.encrypt(title),s.encrypt(body),token);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(WriteNoteActivity.this, "Could not encrypt !",Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(WriteNoteActivity.this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(WriteNoteActivity.this, message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpdateSuccess(String message) {
        Toast.makeText(WriteNoteActivity.this, message,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(WriteNoteActivity.this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}