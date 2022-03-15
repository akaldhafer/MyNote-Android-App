package com.example.mynote.noteapi;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.mynote.userapi.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class NoteUploadData implements NoteDataPresenter, NoteViewMessage {

    NoteViewMessage noteViewMessage;

    public NoteUploadData(NoteViewMessage noteViewMessage) {
        this.noteViewMessage = noteViewMessage;
    }

    //to upload to the firebase
    @Override
    public void onSuccessUpdate(Activity activity, String id, String userEmail, String title, String body, String token) {

        NoteModel noteModel = new NoteModel(id,userEmail,title,body,token);

        FirebaseFirestore.getInstance().collection("NoteData").document(token)
                .set(noteModel, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            noteViewMessage.onUpdateSuccess("Uploaded Successfully");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                noteViewMessage.onUpdateFailure("Something went wrong");
            }
        });
    }

    @Override
    public void onUpdateFailure(String message) {
        noteViewMessage.onUpdateFailure(message);
    }

    @Override
    public void onUpdateSuccess(String message) {
        noteViewMessage.onUpdateSuccess(message);
    }
}
