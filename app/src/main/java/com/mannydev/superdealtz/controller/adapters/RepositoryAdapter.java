package com.mannydev.superdealtz.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mannydev.superdealtz.R;
import com.mannydev.superdealtz.model.Organization;
import com.mannydev.superdealtz.model.Repository;

import java.util.ArrayList;

/**
 * Адаптер для отображения репозитория
 */

public class RepositoryAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Repository> objects;

    public RepositoryAdapter(Context context, ArrayList<Repository> itemsList) {
        ctx = context;
        objects = itemsList;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.repository_item, parent, false);
        }
        final Repository item = getRepository(position);
        ((TextView) view.findViewById(R.id.txtName)).setText(item.getName().toString());
        ((TextView) view.findViewById(R.id.txtDescription)).setText(item.getDescription());
        return view;
    }

    public Repository getRepository(int position) {
        return ((Repository) getItem(position));
    }
}
