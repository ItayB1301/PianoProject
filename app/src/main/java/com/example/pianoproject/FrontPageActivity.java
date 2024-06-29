package com.example.pianoproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.appcompat.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FrontPageActivity extends AppCompatActivity
        implements View.OnClickListener, SoundPool.OnLoadCompleteListener,
        View.OnTouchListener {

    private boolean off=true;
    private float volumeLeft=0.5f;
    private float volumeRight=0.5f;
    private SoundPool soundPool;
    private float floatSpeed = 1.0f;

    private int cnt = 0;

    private ArrayList<String> recording=new ArrayList<>();


    private Integer[] arrBtnId = {R.id.c3, R.id.d3,
            R.id.e3,R.id.f3,R.id.g3,R.id.a3,R.id.b3,R.id.c4,R.id.d4,R.id.e4,R.id.f4,R.id.g4,R.id.a4,R.id.b4,R.id.db3,R.id.eb3,R.id.gb3,R.id.ab3,R.id.bb3,R.id.db4,R.id.eb4,R.id.gb4,R.id.ab4,R.id.bb4};

    boolean[] pressed = new boolean[ arrBtnId.length];

    private Integer[] arrTones = {R.raw.c3, R.raw.d3,
            R.raw.e3,R.raw.f3,R.raw.g3,R.raw.a3,R.raw.b3,R.raw.c4,R.raw.d4,R.raw.e4,R.raw.f4,R.raw.g4,R.raw.a4,R.raw.b4,R.raw.db3,R.raw.eb3,R.raw.gb3,R.raw.ab3,R.raw.bb3,R.raw.db4,R.raw.eb4,R.raw.gb4,R.raw.ab4,R.raw.bb4};

    private Integer[] arrSoundId = new Integer[arrTones.length];

    private Map<Integer,Integer> map=new HashMap<>();
    private Map<String,Integer> notesMap=new HashMap<>();
    private Map<Integer,String> tavsMap=new HashMap<>();

    String[] tune;
    int i = 0;
    int last=-1;
    Handler timerHandler = new Handler();
    Handler timerHandler1 = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (i < tune.length){
                int k = notesMap.get(tune[i]);
                if(last!=-1){
                    ImageButton ib1=findViewById(last);
                    ib1.setImageResource(0);
                }
                int id=arrBtnId[k];
                ImageButton ib=findViewById(id);
                last=id;
                ib.setImageResource(R.drawable.blue);
                soundPool.play(arrSoundId[notesMap.get(tune[i])],1,1,1,0,floatSpeed);
                i++;
                timerHandler.postDelayed(this, 250);

            }
            else {
                timerHandler.removeCallbacks(timerRunnable);
                ImageButton ib1=findViewById(last);
                ib1.setImageResource(0);
            }
        }
    };

    Runnable timerRunnable1 = new Runnable() {
        @Override
        public void run() {
            if(i<recording.size()){
                int k = notesMap.get(recording.get(i));
                if(last!=-1) {
                    ImageButton ib1 = findViewById(last);
                    ib1.setImageResource(0);
                }
                int id=arrBtnId[k];
                ImageButton ib=findViewById(id);
                last=id;
                ib.setImageResource(R.drawable.blue);
                soundPool.play(arrSoundId[notesMap.get(recording.get(i))],1,1,1,0,floatSpeed);
                i++;
                timerHandler1.postDelayed(this, 250);

            }
            else{
                timerHandler1.removeCallbacks(timerRunnable1);
                ImageButton ib1=findViewById(last);
                ib1.setImageResource(0);
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
        tavsMap = createTavsMap();

        soundPool = createSoundPool();
        soundPool.setOnLoadCompleteListener(this);

        SeekBar sb = findViewById(R.id.seekBar);
        sb.setProgress((int)(volumeLeft * 100));

        ImageView volume=findViewById(R.id.volume);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("FRONT", ""+progress);
                volumeLeft = progress/100.0f;
                volumeRight = progress/100.0f;
                if(progress==0)
                    volume.setImageResource(R.drawable.volume_off);
                else
                    volume.setImageResource(R.drawable.volume);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ImageButton record=findViewById(R.id.rec);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton recordBtn = (ImageButton)v;
                if(off) {
                    recordBtn.setImageResource(R.drawable.rec_active);
                    Toast.makeText(FrontPageActivity.this,"Recording has Started", Toast.LENGTH_SHORT).show();

                    recording.clear();

                }

                else if (!off) {
                    recordBtn.setImageResource(R.drawable.rec);
                    if(recording.size()>0) {
                        Toast.makeText(FrontPageActivity.this, "Recording has Stopped", Toast.LENGTH_SHORT).show();
                        showImageSourceDialog();
                    }
                    else
                        Toast.makeText(FrontPageActivity.this, "Nothing was Recorded", Toast.LENGTH_SHORT).show();
                }

                off = !off;
            }
        });
    }
    private void showImageSourceDialog() {
        Dialog img_dialog = new Dialog(FrontPageActivity.this);
        img_dialog.setContentView(R.layout.img_upload_dialog);
        Objects.requireNonNull(img_dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        img_dialog.setCancelable(false);

        Button btn_dia_play = img_dialog.findViewById(R.id.btn_dia_play);
        ImageButton ib_dia_delete = img_dialog.findViewById(R.id.ib_dia_delete);
        ImageButton ib_dia_save = img_dialog.findViewById(R.id.ib_dia_save);
        btn_dia_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRecords();
            }
        });
        ib_dia_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_dialog.dismiss();
            }
        });
        ib_dia_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save to firebase/internal storage
                img_dialog.dismiss();
            }
        });
        img_dialog.show();

    }

    @Override
    public void onClick(View v) {


        int pos = map.get(v.getId());
        v.setBackgroundColor(Color.GRAY);


    }


    private void playRecords() {
        i = 0;
        last=-1;
        timerHandler1.postDelayed(timerRunnable1, 0);
    }

    private void playTunes(String[] tune) {
        this.tune = tune;
        i = 0;

        timerHandler.postDelayed(timerRunnable, 0);
    }

    private SoundPool createSoundPool() {
        SoundPool soundPool;
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(6);
        soundPool = builder.build();
        cnt = 0;
        for (int i = 0; i < arrSoundId.length; i++) {
            arrSoundId[i] = soundPool.load(this, arrTones[i], 1);
        }
        return soundPool;
    }

    private Map<Integer,String> createTavsMap(){
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
            ImageButton aButton = (ImageButton)v;
            aButton.setImageResource(R.drawable.blue);
            soundPool.play(arrSoundId[pos ],volumeLeft,volumeRight,1,0,floatSpeed);
            recording.add(tavsMap.get(pos));
        }
        else if (event.getAction()== MotionEvent.ACTION_UP){
            ImageButton aButton = (ImageButton)v;
            aButton.setImageResource(0);
        }


        return false;
    }


}