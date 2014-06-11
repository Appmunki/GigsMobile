package com.appmunki.gigsmobile.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appmunki.gigsmobile.R;

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
            convertView = inflater.inflate(R.layout.gig_list_item, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.gigtitletextView);
            viewHolder.location = (TextView) convertView.findViewById(R.id.giglocationtextView);
            viewHolder.lastupdated = (TextView) convertView.findViewById(R.id.giglastupdatedtextView);

            viewHolder.price = (TextView) convertView.findViewById(R.id.gigpricetextView);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.gigimageView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }



        viewHolder.title.setText(gig.getTitle());
        viewHolder.location.setText(gig.getDescription());
        viewHolder.price.setText("$"+gig.getPrice());
        viewHolder.image.setImageBitmap(gig.getPicture(getContext()));

        return convertView;
    }

    private static class ViewHolder {
        public TextView title;
        public TextView location;
        public TextView lastupdated;
        public TextView price;
        public ImageView image;
    }
}
