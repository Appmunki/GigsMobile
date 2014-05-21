package com.appmunki.gigsmobile.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appmunki.gigsmobile.R;

/**
 * Created by radzell on 5/11/14.
 */
public class DrawerAdapter extends ArrayAdapter<Pair<Integer,Integer>> {

    public DrawerAdapter(Context context, int resource, Pair<Integer, Integer>[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder(); // view lookup cache stored in tag
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.drawer_list_item, null);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.drawer_list_item_imageView);
            viewHolder.text = (TextView) convertView.findViewById(R.id.drawer_list_item_textView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Pair<Integer, Integer> item = getItem(position);
        viewHolder.image.setImageResource(item.first);
        viewHolder.text.setText(getContext().getResources().getString(item.second));
        return convertView;
    }
    private static class ViewHolder {
        public ImageView image;
        public TextView text;
    }
}