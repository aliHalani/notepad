package com.notepad.notepad;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {
    static final int MAX_TITLE_VIEW_LENGTH = 20;
    static final int MAX_TITLE_ABSENT_VIEW_LENGTH = 10;
    static final int MAX_CONTENT_VIEW_LENGTH = 15;

    private ArrayList<Note> notes;
    private LayoutInflater viewInflator;
    private NoteAdapterCallback noteAdapterCallback;

    private AlertDialog confirmDeletion;
    private int deletePos;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        Button delete;

        ViewHolder(View item) {
            super(item);
            title = item.findViewById(R.id.noteTitle);
            content = item.findViewById(R.id.noteContent);
            delete = item.findViewById(R.id.deleteNote);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noteAdapterCallback.noteClick(getAdapterPosition());
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletePos = getAdapterPosition();
                    confirmDeletion.show();
                }
            });
        }
    }

    NoteRecyclerAdapter(Context context, ArrayList<Note> notes) {
        this.viewInflator = LayoutInflater.from(context);
        this.notes = notes;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noteAdapterCallback.deleteNoteClick(deletePos);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        confirmDeletion = builder.create();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = viewInflator.inflate(R.layout.notelist_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int pos) {
        Note note = notes.get(pos);

        String content = note.content.split("\n", 0)[0];
        if (content.length() > MAX_CONTENT_VIEW_LENGTH) {
            content = content.substring(0, MAX_CONTENT_VIEW_LENGTH);
            content = content + "...";
        }
        viewHolder.content.setText(content);

        String title = note.title;
        if (title.length() == 0) {
            title = content;
            if (content.length() > MAX_TITLE_ABSENT_VIEW_LENGTH) {
                title = content.substring(0, MAX_TITLE_ABSENT_VIEW_LENGTH);
                title = title + "...";
            }
        } else if (title.length() > MAX_TITLE_VIEW_LENGTH) {
            title = title.substring(0, MAX_TITLE_VIEW_LENGTH);
            title = title + "...";
        }
        viewHolder.title.setText(title);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    Note getItem(int pos) {
        return notes.get(pos);
    }

    void setClickListener(NoteAdapterCallback callback) {
        this.noteAdapterCallback = callback;
    }

    public interface NoteAdapterCallback {
        void noteClick(int position);
        void deleteNoteClick(int position);
    }
}
