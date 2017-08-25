package com.mahendri.permata2017;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mahendri on 1/27/2017.
 * adapter untuk About
 */

class AboutAdapter extends ArrayAdapter<About> {

    AboutAdapter(Context context, ArrayList<About> arrayList) {
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if (listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_about, parent, false);
        }
        About current = getItem(position);

        if (current != null){
            TextView title = (TextView) listView.findViewById(R.id.titleAbout);
            title.setText(current.getTitle());

            TextView descrp = (TextView) listView.findViewById(R.id.descripAbout);
            descrp.setText(current.getDescription());
        }

        return listView;
    }
}
