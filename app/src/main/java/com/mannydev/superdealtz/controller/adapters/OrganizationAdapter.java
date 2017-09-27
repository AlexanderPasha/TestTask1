package com.mannydev.superdealtz.controller.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mannydev.superdealtz.model.Organization;
import com.mannydev.superdealtz.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


/**
 * Адаптер для отобрацения Организации
 */

public class OrganizationAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Organization> objects;

    public OrganizationAdapter(Context context, ArrayList<Organization> itemsList) {
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
            view = lInflater.inflate(R.layout.organization_item, parent, false);
        }

        final Organization item = getOrganization(position);

        try{
            Picasso.with(ctx).load(item.getAvatar_url()).resize(128,128).into((ImageView) view.findViewById(R.id.imageView));
            ((TextView) view.findViewById(R.id.txtName)).setText(item.getName().toString());
            ((TextView) view.findViewById(R.id.txtLocation)).setText(item.getLocation());
            ((TextView) view.findViewById(R.id.txtBlog)).setText(Html.fromHtml("<u>"+item.getBlog()+"</u>"));
        }catch (Exception e){
            Log.v("myLogs", "Ошибка в адаптере");
        }



        return view;
    }

    public Organization getOrganization(int position) {
        return ((Organization) getItem(position));
    }
}
