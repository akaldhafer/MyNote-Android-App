package com.example.mynote.noteapi;

import com.example.mynote.userapi.UserModel;

public interface NoteViewFetchMessage {
    void onUpdateSuccess(NoteModel message);
    void onUpdateFailure(String message);
}
