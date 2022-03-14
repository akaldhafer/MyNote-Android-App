package com.example.mynote.noteapi;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.mynote.encryption.SymmtCrypto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class NoteFetchData implements NoteFetchDataPresenter{
    private Context context;
    private NoteViewFetchMessage noteViewFetchMessage;

    public NoteFetchData(Context context, NoteViewFetchMessage noteViewFetchMessage) {
        this.context = context;
        this.noteViewFetchMessage = noteViewFetchMessage;
    }

    @Override
    public void onSuccessUpdate(Activity activity) {
        FirebaseFirestore.getInstance().collection("NoteData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (int i = 0; i < task.getResult().size(); i++) {
                        String id = task.getResult().getDocuments().get(i).getString("id");
                        String userEmail = task.getResult().getDocuments().get(i).getString("userEmail");
                        String title = task.getResult().getDocuments().get(i).getString("title");
                        String body = task.getResult().getDocuments().get(i).getString("body");
                        String token = task.getResult().getDocuments().get(i).getString("token");
                        SymmtCrypto de = new SymmtCrypto();

                        try {
                            NoteModel noteModel = new NoteModel(id, de.decrypt(userEmail),
                                    de.decrypt(title), de.decrypt(body),token);
                            noteViewFetchMessage.onUpdateSuccess(noteModel);

                        } catch (Exception e) {
                            e.printStackTrace();
                            noteViewFetchMessage.onUpdateFailure(e.getMessage().toString());

                        }

                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                noteViewFetchMessage.onUpdateFailure(e.getMessage().toString());

            }
        });
    }


}
