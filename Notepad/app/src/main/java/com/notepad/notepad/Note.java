package com.notepad.notepad;

import java.io.Serializable;

public class Note implements Serializable {
    String title;
    String content;

    Note() {
        title = "";
        content = "";
    }

    Note(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
