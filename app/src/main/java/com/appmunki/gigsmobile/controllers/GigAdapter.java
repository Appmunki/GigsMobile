package com.appmunki.gigsmobile.controllers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appmunki.gigsmobile.models.Gig;

import java.util.ArrayList;

/**
 * Created by radzell on 5/2/14.
 */
public class GigAdapter extends ArrayAdapter<Gig> {
    public GigAdapter(Context context, int textViewResourceId,
                      ArrayList<Gig> objects) {
        super(context, textViewResourceId, objects);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Gig gig = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
            viewHolder.title = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }



        viewHolder.title.setText(gig.getTitle());


        Log.i("Test",getItem(position).getTitle()+":"+getCount());

        return convertView;
    }

    static class ViewHolder {
        public TextView title;
    }
}
