package com.ahn.vehiclerentapp.adaptes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ahn.vehiclerentapp.R;

import java.util.ArrayList;

public class Sp_adapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> itemList;

    public Sp_adapter(Context context, ArrayList<String> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.spinner_item, parent, false);
        }

        TextView itemTextView = convertView.findViewById(R.id.tv_txt);
        itemTextView.setText(itemList.get(position));

        return convertView;
    }
}
