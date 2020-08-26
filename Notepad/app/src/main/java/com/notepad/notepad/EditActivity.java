package com.notepad.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {
    private Button saveButton;
    private EditText title;
    private EditText content;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        saveButton = findViewById(R.id.saveButton);
        title = findViewById(R.id.noteEditTitle);
        content = findViewById(R.id.noteEditContent);

        note = (Note) getIntent().getExtras().get("note");

        title.setText(note.title, TextView.BufferType.EDITABLE);
        content.setText(note.content, TextView.BufferType.EDITABLE);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        saveNote();
        super.finish();
    }

    public void saveNote() {
        note.title = title.getText().toString();
        note.content = content.getText().toString();

        Intent noteData = new Intent();
        noteData.putExtra("note", note);
        if (getIntent().getExtras().containsKey("index")) {
            noteData.putExtra("index", (int) getIntent().getExtras().get("index"));
        }
        setResult(RESULT_OK, noteData);
    }

}
