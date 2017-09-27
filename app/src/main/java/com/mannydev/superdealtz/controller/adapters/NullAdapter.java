package com.mannydev.superdealtz.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mannydev.superdealtz.R;


/**
 * Пустой адаптер
 */

public class NullAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;

    public NullAdapter(Context context) {
        ctx = context;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.organization_null, parent, false);
        }
        ((TextView) view.findViewById(R.id.textView2)).setText("");
        return view;

    }
}
