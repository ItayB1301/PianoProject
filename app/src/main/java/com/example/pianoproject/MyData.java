package com.example.pianoproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyData {

    public static ArrayList<String> song;
    public static Map<Integer,String> createTavsMap(){
        Map<Integer,String> notesMap = new HashMap<>();
        notesMap.put(0,"c3");
        notesMap.put(1,"d3");
        notesMap.put(2,"e3");
        notesMap.put(3,"f3");
        notesMap.put(4,"g3");
        notesMap.put(5,"a3");
        notesMap.put(6,"b3");

        notesMap.put(7,"c4");
        notesMap.put(8,"d4");
        notesMap.put(9,"e4");
        notesMap.put(10,"f4");
        notesMap.put(11,"g4");
        notesMap.put(12,"a4");
        notesMap.put(13,"b4");

        notesMap.put(14,"db3");
        notesMap.put(15,"eb3");
        notesMap.put(16,"gb3");
        notesMap.put(17,"ab3");
        notesMap.put(18,"bb3");

        notesMap.put(19,"db4");
        notesMap.put(20,"eb4");
        notesMap.put(21,"gb4");
        notesMap.put(22,"ab4");
        notesMap.put(23,"bb4");

        return notesMap;
    }

    public static Map<String, Integer> createNotesMap(){
        Map<String,Integer> notesMap = new HashMap<>();
        notesMap.put("c3",0);
        notesMap.put("d3",1);
        notesMap.put("e3",2);
        notesMap.put("f3",3);
        notesMap.put("g3",4);
        notesMap.put("a3",5);
        notesMap.put("b3",6);

        notesMap.put("c4",7);
        notesMap.put("d4",8);
        notesMap.put("e4",9);
        notesMap.put("f4",10);
        notesMap.put("g4",11);
        notesMap.put("a4",12);
        notesMap.put("b4",13);

        notesMap.put("db3",14);
        notesMap.put("eb3",15);
        notesMap.put("gb3",16);
        notesMap.put("ab3",17);
        notesMap.put("bb3",18);

        notesMap.put("db4",19);
        notesMap.put("eb4",20);
        notesMap.put("gb4",21);
        notesMap.put("ab4",22);
        notesMap.put("bb4",23);

        return notesMap;
    }
}
