package com.example.mynote.noteapi;

public interface NoteViewMessage {
    void onUpdateFailure(String message);
    void onUpdateSuccess(String message);
}
