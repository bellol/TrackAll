package com.example.bellng.trackall;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import Classes.Checkpoint;

/**
 * Created by bellng on 3/06/2015.
 */
public class CheckpointAdapter extends BaseAdapter {

    Context context;
    ArrayList<Checkpoint> checkpoints;

    public CheckpointAdapter(Context context, ArrayList<Checkpoint> checkpoints){
        this.context = context;
        this.checkpoints = checkpoints;
    }

    public int getCount(){
        return checkpoints.size();
    }

    @Override
    public Checkpoint getItem(int i) {
        return checkpoints.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_checkpoint, null);
        }

        TextView time = (TextView) view.findViewById(R.id.timeLabel);
        TextView message = (TextView) view.findViewById(R.id.messageLabel);
        TextView location = (TextView) view.findViewById(R.id.locationLabel);

        Checkpoint item = checkpoints.get(i);

        message.setTypeface(null, Typeface.BOLD);

        time.setText(item.getCheckpointTime());
        message.setText(item.getMessage());
        location.setText(item.getCountryName());

        return view;
    }

}
