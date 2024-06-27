package com.example.pianoproject;

import androidx.appcompat.app.AppCompatActivity;

import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import java.util.HashMap;
import java.util.Map;

public class FrontPageActivity extends AppCompatActivity implements View.OnClickListener {

    private float volumeLeft=0.5f;
    private float volumeRight=0.5f;
    private SoundPool soundPool;
    private float floatSpeed = 1.0f;

    private Integer[] arrBtnId = {R.id.c3, R.id.d3,
            R.id.e3,R.id.f3,R.id.g3,R.id.a3,R.id.b3,R.id.c4,R.id.d4,R.id.e4,R.id.f4,R.id.g4,R.id.a4,R.id.b4};
    private Integer[] arrTones = {R.raw.c3, R.raw.d3,
            R.raw.e3,R.raw.f3,R.raw.g3,R.raw.a3,R.raw.b3,R.id.c4,R.id.d4,R.id.e4,R.id.f4,R.id.g4,R.id.a4,R.id.b4};

    private Integer[] arrSoundId = new Integer[7];

    private Map<Integer,Integer> map=new HashMap<>();
    private Map<String,Integer> songMap=new HashMap<>();

    String[] notes;
    int i = 0;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (i < notes.length){
                int k = songMap.get(notes[i]);
                soundPool.play(arrSoundId[songMap.get(notes[i])],1,1,1,0,floatSpeed);
                i++;
                timerHandler.postDelayed(this, 500);
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
        }

        for(int i=0;i<arrBtnId.length;i++){
            map.put(arrBtnId[i],i);
        }
        songMap.put("c3",0);
        songMap.put("d3",1);
        songMap.put("e3",2);
        notes=new String[]{"c3","d3","e3"};
        timerHandler.postDelayed(timerRunnable, 0);


        for(int i=0;i< notes.length;i++){
            int k = songMap.get(notes[i]);
            soundPool.play(arrSoundId[songMap.get(notes[i])],1,1,1,0,floatSpeed);
        }

        SeekBar sb = findViewById(R.id.seekBar);
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
        soundPool.play(arrSoundId[ map.get(v.getId())],volumeLeft,volumeRight,1,0,floatSpeed);

    }
}