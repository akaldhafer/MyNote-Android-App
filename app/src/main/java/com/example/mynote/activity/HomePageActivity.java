package com.example.mynote.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynote.R;
import com.example.mynote.adapter.ViewNotesAdapter;
import com.example.mynote.auth.LoginPage;
import com.example.mynote.noteapi.NoteFetchData;
import com.example.mynote.noteapi.NoteModel;
import com.example.mynote.noteapi.NoteViewFetchMessage;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class HomePageActivity extends AppCompatActivity implements NoteViewFetchMessage {

    private RecyclerView ListDataView;
    private ViewNotesAdapter viewNotesAdapter;

    ArrayList<NoteModel> noteModelArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide (); //This Line hides the action bar
        setContentView(R.layout.activity_home_page);

        ListDataView = findViewById(R.id.ListView);

        NoteFetchData noteFetchData = new NoteFetchData(this, this);

        RecyclerViewMethod();

        noteFetchData.onSuccessUpdate(this);
    }
    public void RecyclerViewMethod() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        ListDataView.setLayoutManager(manager);
        ListDataView.setHasFixedSize(true);

        viewNotesAdapter = new ViewNotesAdapter(this, noteModelArrayList);
        ListDataView.setAdapter(viewNotesAdapter);
        ListDataView.invalidate();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onUpdateSuccess(NoteModel message) {
        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        if(message != null && message.getUserEmail().equals(email)){
            NoteModel noteModel = new NoteModel(message.getId(),message.getUserEmail(),
                    message.getTitle(),message.getBody(),message.getToken());
            noteModelArrayList.add(noteModel);

        }
        viewNotesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(HomePageActivity.this, message, Toast.LENGTH_LONG).show();

    }

    public void onClickProfile(View view) {
        Intent intent = new Intent(HomePageActivity.this, UserProfile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    public void onClickWriteNoteButton(View view) {
        Intent intent = new Intent(HomePageActivity.this, WriteNoteActivity.class);
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
