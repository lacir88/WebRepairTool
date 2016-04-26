package com.ksoft.webrepairtool.Activities.RemoteDirectoryBrowserPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ksoft.webrepairtool.R;

import java.util.ArrayList;

/**
 * Created by Laci on 2016.04.18..
 */
public class CustomListAdapter extends ArrayAdapter<FileListEntry> {

    private final Context context;
    private final ArrayList<FileListEntry> modelsArrayList;

    public CustomListAdapter(Context context, ArrayList<FileListEntry> modelsArrayList) {

        super(context, R.layout.list_row, modelsArrayList);

        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater

        View rowView = null;
        rowView = inflater.inflate(R.layout.list_row, parent, false);

            // 3. Get icon,title & counter views from the rowView
            ImageView imgView = (ImageView) rowView.findViewById(R.id.item_icon);
            TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
            //TextView counterView = (TextView) rowView.findViewById(R.id.item_counter);

            // 4. Set the text for textView
            imgView.setImageResource(modelsArrayList.get(position).getTypePicturePath());
            titleView.setText(modelsArrayList.get(position).getName());
            //counterView.setText(modelsArrayList.get(position).getCounter());


        // 5. retrn rowView
        return rowView;
    }
}
