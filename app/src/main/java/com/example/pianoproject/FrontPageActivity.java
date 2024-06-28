package com.example.pianoproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;


import java.util.HashMap;
import java.util.Map;

public class FrontPageActivity extends AppCompatActivity
        implements View.OnClickListener, SoundPool.OnLoadCompleteListener,
        View.OnTouchListener {


    private float volumeLeft=0.5f;
    private float volumeRight=0.5f;
    private SoundPool soundPool;
    private float floatSpeed = 1.0f;

    private int cnt = 0;


    private Integer[] arrBtnId = {R.id.c3, R.id.d3,
            R.id.e3,R.id.f3,R.id.g3,R.id.a3,R.id.b3,R.id.c4,R.id.d4,R.id.e4,R.id.f4,R.id.g4,R.id.a4,R.id.b4,R.id.db3,R.id.eb3,R.id.gb3,R.id.ab3,R.id.bb3,R.id.db4,R.id.eb4,R.id.gb4,R.id.ab4,R.id.bb4};

    boolean[] pressed = new boolean[ arrBtnId.length];

    private Integer[] arrTones = {R.raw.c3, R.raw.d3,
            R.raw.e3,R.raw.f3,R.raw.g3,R.raw.a3,R.raw.b3,R.raw.c4,R.raw.d4,R.raw.e4,R.raw.f4,R.raw.g4,R.raw.a4,R.raw.b4,R.raw.db3,R.raw.eb3,R.raw.gb3,R.raw.ab3,R.raw.bb3,R.raw.db4,R.raw.eb4,R.raw.gb4,R.raw.ab4,R.raw.bb4};

    private Integer[] arrSoundId = new Integer[arrTones.length];

    private Map<Integer,Integer> map=new HashMap<>();
    private Map<String,Integer> notesMap=new HashMap<>();

    String[] tune;
    int i = 0;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (i < tune.length){
                int k = notesMap.get(tune[i]);
                soundPool.play(arrSoundId[notesMap.get(tune[i])],1,1,1,0,floatSpeed);
                i++;
                timerHandler.postDelayed(this, 250);
            }
            else {
                timerHandler.removeCallbacks(timerRunnable);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        SoundPool.Builder builder = new SoundPool.Builder();
        soundPool = builder.build();

        for (int i = 0; i < arrSoundId.length; i++) {
            arrSoundId[i] = soundPool.load(this, arrTones[i], 1);
        }
        for (int i = 0; i < arrBtnId.length; i++) {
            findViewById(arrBtnId[i]).setOnClickListener(this);
            findViewById(arrBtnId[i]).setOnTouchListener(this);
            map.put(arrBtnId[i],i);
        }


        for(int i=0;i<arrBtnId.length;i++){
            map.put(arrBtnId[i],i);
        }
        notesMap  = createNotesMap();

        soundPool = createSoundPool();
        soundPool.setOnLoadCompleteListener(this);

        SeekBar sb = findViewById(R.id.seekBar);
        sb.setProgress((int)(volumeLeft * 100));
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("FRONT", ""+progress);
                volumeLeft = progress/100.0f;
                volumeRight = progress/100.0f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        int pos = map.get(v.getId());
//        ImageButton ib = (ImageButton) v;
//
//        if (pressed[pos]) {
//            ib.setBackgroundColor(Color.GRAY);
//        }
//        else {
//            ib.setBackgroundColor(Color.WHITE);
//        }
//        pressed[pos] = !pressed[pos];
        v.setBackgroundColor(Color.GRAY);
        //soundPool.play(arrSoundId[pos ],volumeLeft,volumeRight,1,0,floatSpeed);
    }


    private void playTunes(String[] tune) {
        this.tune = tune;
        i = 0;

        timerHandler.postDelayed(timerRunnable, 0);
    }

    private SoundPool createSoundPool() {
        SoundPool soundPool;
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(4);
        soundPool = builder.build();
        cnt = 0;
        for (int i = 0; i < arrSoundId.length; i++) {
            arrSoundId[i] = soundPool.load(this, arrTones[i], 1);
        }
        return soundPool;
    }

    private Map<String, Integer> createNotesMap(){
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


    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        cnt++;
        if (cnt == 23)
            playTunes(new String[]{"c3","d3","e3","f3","g3","a3","b3","c4"});
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }



    public boolean onTouch(View v, MotionEvent event) {
        Log.d("TOUCH", event.toString());
        int pos = map.get(v.getId());
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            v.setBackgroundColor(Color.BLUE);
            soundPool.play(arrSoundId[pos ],volumeLeft,volumeRight,1,0,floatSpeed);
        }
        else if (event.getAction()== MotionEvent.ACTION_UP){
            v.setBackgroundColor(Color.WHITE);
        }
        return false;
    }


}