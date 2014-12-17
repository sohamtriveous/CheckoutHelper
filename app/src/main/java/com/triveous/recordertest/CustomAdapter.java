package com.triveous.recordertest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Shivam on 10/28/2014.
 */
public class CustomAdapter extends BaseAdapter {

    private ArrayList<SkuUi> skuArrayList;
    private static LayoutInflater layoutInflater = null;
    public static ViewHolder holder;

    public CustomAdapter(final Context context, ArrayList data) {
        skuArrayList = data;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return skuArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return skuArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if (view == null) {
            vi = layoutInflater.inflate(R.layout.custom_layout, null);
            holder = new ViewHolder();
            holder.title = (TextView) vi.findViewById(R.id.title);
            holder.desc = (TextView) vi.findViewById(R.id.desc);
            holder.price = (TextView) vi.findViewById(R.id.price);
            holder.state = (TextView) vi.findViewById(R.id.state);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        if (skuArrayList.size() > 0) {
            holder.title.setText(skuArrayList.get(i).sku.title);
            holder.desc.setText(skuArrayList.get(i).sku.description);
            holder.price.setText(skuArrayList.get(i).sku.price);
        }

        if(!TextUtils.isEmpty(skuArrayList.get(i).token)) {
            holder.title.setTextColor(Color.GRAY);
            setState("Purchased");
        } else {
            holder.title.setTextColor(Color.GREEN);
            setState("Not Purchased");
        }

        return vi;
    }

    public static void setState(String state) {
        holder.state.setText(state);
    }

    public static class ViewHolder {
        public TextView title;
        public TextView desc;
        public TextView price;
        public TextView state;

    }
}
