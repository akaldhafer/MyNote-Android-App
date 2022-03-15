package com.example.mynote.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynote.R;
import com.example.mynote.activity.ViewNote;
import com.example.mynote.activity.WriteNoteActivity;
import com.example.mynote.noteapi.NoteModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewNotesAdapter extends RecyclerView.Adapter<ViewNotesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<NoteModel> noteModelArrayList = new ArrayList<>();

    public ViewNotesAdapter(Context context, ArrayList<NoteModel> noteModelArrayList) {
        this.context = context;
        this.noteModelArrayList = noteModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_cardview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewNotesAdapter.ViewHolder holder, int position) {
        System.out.println("Get with position "+noteModelArrayList.get(position).getTitle());

        String title=noteModelArrayList.get(holder.getAdapterPosition()).getTitle();
        String body=noteModelArrayList.get(holder.getAdapterPosition()).getBody();
        String token=noteModelArrayList.get(holder.getAdapterPosition()).getToken();
        String email=noteModelArrayList.get(holder.getAdapterPosition()).getUserEmail();
        String id = noteModelArrayList.get(holder.getAdapterPosition()).getId();

        holder.vtitle.setText(title);
        holder.vbody.setText(body);
        holder.vToken.setText(token);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo fix the call and variable init
                Intent intent = new Intent(v.getContext(), ViewNote.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", id);
                intent.putExtra("title", title);
                intent.putExtra("body", body);
                intent.putExtra("email", email);
                intent.putExtra("token",token);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteModelArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView vtitle,vbody, vToken;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vtitle = itemView.findViewById(R.id.viewTitle);
            vbody = itemView.findViewById(R.id.viewBody);
            vToken = itemView.findViewById(R.id.viewToken);
            cardView = itemView.findViewById(R.id.CardView);

        }
    }


}
