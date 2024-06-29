package com.example.pianoproject;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements MyAdapter.IEvents {

    int i=0;
    int last=-1;
    DatabaseReference songRef;
    MyAdapter adapter;
    ListView listView;
    ArrayList<String> clickedRec;
    private ArrayList<Song> mySongs=new ArrayList<>();
    Handler timerHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        readSongsFromDB();

        listView=findViewById(R.id.lv);

        Button back = findViewById(R.id.backToPiano);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this,FrontPageActivity.class));
            }
        });


    }

    private void readSongsFromDB(){
        songRef= FirebaseDatabase.getInstance().getReference("Songs");
        songRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap:snapshot.getChildren()) {
                    Song song=snap.getValue(Song.class);
                    mySongs.add(song);
                }
                adapter=new MyAdapter(HistoryActivity.this,R.layout.myrow,0,mySongs);
                adapter.setOnClickListener(HistoryActivity.this);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onClickPlay(int pos) {
        Toast.makeText(HistoryActivity.this,"Will now Play",Toast.LENGTH_SHORT).show();
        clickedRec=mySongs.get(pos).getNotes();
        Log.d("ssss Clicked Record",clickedRec.toString());
        finish();
        Intent intent=new Intent(HistoryActivity.this,FrontPageActivity.class);
        intent.putExtra("song",clickedRec);
        intent.putExtra("function",1);
        startActivity(intent);
    }


    @Override
    public void onClickGuide(int pos) {
        Toast.makeText(HistoryActivity.this,"Your Turn!",Toast.LENGTH_SHORT).show();
        clickedRec=mySongs.get(pos).getNotes();
        finish();
        Intent intent=new Intent(HistoryActivity.this,FrontPageActivity.class);
        intent.putExtra("song",clickedRec);
        intent.putExtra("function",2);
        startActivity(intent);

    }


}