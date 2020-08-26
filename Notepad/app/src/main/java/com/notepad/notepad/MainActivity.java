package com.notepad.notepad;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NoteRecyclerAdapter.NoteAdapterCallback {
    public static final int EDIT_ACTIVITY_NEW_REQUEST_CODE = 2;
    public static final int EDIT_ACTIVITY_EDIT_REQUEST_CODE = 3;
    private NoteRecyclerAdapter adapter;
    private ArrayList<Note> notes;
    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notes = loadNotes();

        RecyclerView recyclerView = findViewById(R.id.noteList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteRecyclerAdapter(this, notes);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        addButton = findViewById(R.id.addNoteButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNote = new Intent(MainActivity.this, EditActivity.class);
                addNote.putExtra("note", new Note());
                startActivityForResult(addNote, EDIT_ACTIVITY_NEW_REQUEST_CODE);
            }
        });
    }

    @Override
    public void noteClick(int position) {
        Intent editNote = new Intent(MainActivity.this, EditActivity.class);
        editNote.putExtra("note", adapter.getItem(position));
        editNote.putExtra("index", position);
        startActivityForResult(editNote, EDIT_ACTIVITY_EDIT_REQUEST_CODE);
    }

    @Override
    public void deleteNoteClick(int position) {
        notes.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, notes.size());
        saveNotes(notes);
        Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
    }

    private ArrayList<Note> loadNotes() {
        try {
            File source = new File(getBaseContext().getFilesDir(), "notelist");

            if (!source.exists()) {
                source.createNewFile();
                return new ArrayList<Note>();
            }

            FileInputStream fileSource = new FileInputStream(source);
            ObjectInputStream fileInput = new ObjectInputStream(fileSource);
            ArrayList<Note> noteList = (ArrayList<Note>) fileInput.readObject();
            fileInput.close();
            fileSource.close();
            return noteList;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading notes");
            e.printStackTrace();
            return null;
        }
    }

    private void saveNotes(ArrayList<Note> noteList) {
        File noteListFile = new File(getBaseContext().getFilesDir(), "notelist");

        try {
            FileOutputStream fileDestination = new FileOutputStream(noteListFile);
            ObjectOutputStream fileOutput = new ObjectOutputStream(fileDestination);
            fileOutput.writeObject(noteList);
            fileOutput.close();
            fileDestination.close();
        } catch ( Exception ex ) {
            System.out.println("Error saving notes");
            ex.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == EDIT_ACTIVITY_NEW_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Note newNote = (Note) data.getExtras().get("note");

                if (!(newNote.title.length() == 0 && newNote.content.length() == 0)) {
                    notes.add(0, newNote);
                    adapter.notifyItemInserted(0);
                    adapter.notifyDataSetChanged();
                    saveNotes(notes);
                    Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == EDIT_ACTIVITY_EDIT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Note editedNote = (Note) data.getExtras().get("note");
                int index = (int) data.getExtras().get("index");

                if (editedNote.title.length() == 0 && editedNote.content.length() == 0) {
                    notes.remove(index);
                    adapter.notifyItemRemoved(index);
                    adapter.notifyItemRangeChanged(index, notes.size());
                    saveNotes(notes);
                    Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
                } else {
                    if (!editedNote.title.equals(notes.get(index).title) || !editedNote.content.equals(notes.get(index).content)) {
                        notes.get(index).title = editedNote.title;
                        notes.get(index).content = editedNote.content;
                        adapter.notifyItemChanged(index);
                        saveNotes(notes);
                        Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

}
