package com.example.mynote.noteapi;

import android.app.Activity;

public interface NoteDataPresenter {
    void onSuccessUpdate(Activity activity,String id, String userEmail, String title, String body, String token);

}
