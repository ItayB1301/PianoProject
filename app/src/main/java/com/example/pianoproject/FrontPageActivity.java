package com.example.pianoproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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


    private ArrayList<String> recording=new ArrayList<>();
    private ArrayList<String> recFromHistory=new ArrayList<>();


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
                if(getIntent().getExtras()!=null){
                    recFromHistory=(ArrayList<String>) getIntent().getExtras().get("song");
                    if(recFromHistory!=null){
                        recording=recFromHistory;
                        playRecords();
                        timerHandler.postDelayed(this, 250);
                    }
                }

            }
        }
    };

    Runnable timerRunnable1 = new Runnable() {
        @Override
        public void run() {
            TextView textView = findViewById(R.id.tavOnTop);
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
                if(getIntent().getExtras()!=null){
                    textView.setText((CharSequence)((ArrayList<String>) getIntent().getExtras().get("song")).get(i));
                }
                else {
                    textView.setText((CharSequence)(recording.get(i)));
                }
                i++;
                timerHandler1.postDelayed(this, 250);

            }
            else{
                timerHandler1.removeCallbacks(timerRunnable1);
                ImageButton ib1=findViewById(last);
                ib1.setImageResource(0);
                textView.setText("");
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
        notesMap  = MyData.createNotesMap();
        tavsMap = MyData.createTavsMap();

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

        ImageButton history =findViewById(R.id.helpbutton);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FrontPageActivity.this,HistoryActivity.class));
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
                DatabaseReference songRef= FirebaseDatabase.getInstance().getReference("Songs");
                songRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String songTitle="song_"+String.valueOf (snapshot.getChildrenCount()+1);
                        Song song=new Song(songTitle,recording);
                        songRef.child(song.getTitle()).setValue(song);
                        img_dialog.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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
        TextView textView = findViewById(R.id.tavOnTop);
        timerHandler1.postDelayed(timerRunnable1, 250);
    }

    private void playTunes(String[] tune) {
        this.tune = tune;
        i = 0;

        timerHandler.postDelayed(timerRunnable, 300);
    }

    private SoundPool createSoundPool() {
        SoundPool soundPool;
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(6);
        soundPool = builder.build();
        for (int i = 0; i < arrSoundId.length; i++) {
            arrSoundId[i] = soundPool.load(this, arrTones[i], 1);
        }
        return soundPool;
    }



    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        if(getIntent().getExtras()!=null){

            TextView textView = findViewById(R.id.tavOnTop);
            //if user pressed red button
            if((int)getIntent().getExtras().get("function")==1){
                recFromHistory=(ArrayList<String>) getIntent().getExtras().get("song");
                if(recFromHistory!=null){
                    recording=recFromHistory;
                    playRecords();

                }

            }
            else if ((int)getIntent().getExtras().get("function")==2) {
                recFromHistory=(ArrayList<String>) getIntent().getExtras().get("song");
                textView.setText("Orange");
                if(recFromHistory!=null){
                    recording=recFromHistory;
                    //TextView textView = findViewById(R.id.textView);


//                    for(int i=0;i<recording.size();i++){
//                        textView.setText(recFromHistory.get(i));
//
//                    }


                }
            }
        }
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
        TextView textView = findViewById(R.id.tavOnTop);
        Log.d("TOUCH", event.toString());
        int pos = map.get(v.getId());
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            ImageButton aButton = (ImageButton)v;
            aButton.setImageResource(R.drawable.blue);
            soundPool.play(arrSoundId[pos],volumeLeft,volumeRight,1,0,floatSpeed);
            textView.setText((CharSequence)tavsMap.get(pos).toString());
            recording.add(tavsMap.get(pos));
        }
        else if (event.getAction()== MotionEvent.ACTION_UP){
            ImageButton aButton = (ImageButton)v;
            aButton.setImageResource(0);
        }


        return false;
    }


}