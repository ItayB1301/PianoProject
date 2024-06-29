package com.example.pianoproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends ArrayAdapter<Song> {

    Context context;
    int resource;
    int textViewResourceID;
    List<Song> list;
    IEvents listener;

    public MyAdapter(@NonNull Context context, int resource,int textViewResourceId, @NonNull ArrayList<Song> objects) {
        super(context, resource,textViewResourceId, objects);
        this.context=context;
        this.resource=resource;
        this.textViewResourceID=textViewResourceId;
        this.list=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater= ((Activity)context).getLayoutInflater();
        View view=layoutInflater.inflate(resource,parent,false);
        @SuppressLint({"MissingInflatedId","LocalSuppress"})
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        Song temp=list.get(position);
        tvTitle.setText(getItem(position).getTitle());

        ImageButton play=view.findViewById(R.id.imagePlay);
        ImageButton guide=view.findViewById(R.id.imageGuide);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClickPlay(position);
                }
            }
        });

        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClickGuide(position);
                }
            }
        });


        return view;
    }

    public void setOnClickListener(IEvents listener){
        this.listener=listener;
    }

    interface IEvents{
        void onClickPlay(int pos);
        void onClickGuide(int pos);
    }


}
