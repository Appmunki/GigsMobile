package com.appmunki.gigsmobile.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appmunki.gigsmobile.models.Gig;
import com.google.android.gms.internal.ge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by radzell on 5/2/14.
 */
public class GigAdapter extends ArrayAdapter<Gig> {
    static class ViewHolder {
        public TextView titleTextView;
    }

    public GigAdapter(Context context, int textViewResourceId,
                              ArrayList<Gig> objects) {
        super(context, textViewResourceId, objects);


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = getContext().getLayoutInflater();
            rowView = inflater.inflate(android.R.layout.simple_list_item_1, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) rowView.findViewById(android.R.id.text1);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        String s = getItem(position).getTitle();
        holder.titleTextView.setText(s);


        return rowView;

    }
}
