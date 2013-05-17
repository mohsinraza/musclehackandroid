package com.musclehack.musclehack;

import java.util.List;
import java.util.Map;

import android.content.Context;



public class SimpleRssAdapter extends SimpleCustomableAdapter {
    public SimpleRssAdapter(Context context, List<? extends Map<String, ?>> data,
            int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    
    //protected void bindView(int position, View view) {
        //super.bindView(position, view);
    //}

}