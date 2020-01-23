package com.myproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.myproject.R;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<String> {
    private ArrayList<String> data;
    private Context context;
    private LayoutInflater inflater;
    private View v;
    private TextView tv;

    public SpinnerAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.data = objects;
        this.inflater = LayoutInflater.from(context);
    }

    public SpinnerAdapter(Context context, int resource, ArrayList<String> objects, boolean isAddress) {
        super(context, resource, objects);
        this.context = context;
        this.data = objects;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        v = convertView;
        if (v == null) {
            v = inflater.inflate(R.layout.spinner_row_layout, parent, false);
        }
        tv = (TextView) v.findViewById(R.id.spinner_row_text);
        if (data.get(position).trim().isEmpty()) {
            tv.setText("-");
        } else {
            tv.setText(data.get(position));
        }

        return v;
    }
}

