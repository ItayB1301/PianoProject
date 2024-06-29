package com.example.pianoproject;

import java.util.ArrayList;

public class Song {
    private String title;
    private ArrayList<String> notes;

    public Song() {

    }

    public Song(String title, ArrayList<String> notes) {
        this.title = title;
        this.notes = notes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", notes=" + notes +
                '}';
    }
}
