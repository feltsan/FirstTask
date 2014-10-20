package com.example.john.firsttask.support;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.john.firsttask.R;

import java.util.ArrayList;
import java.util.HashMap;




public class ListViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    LayoutInflater inflater;
    ViewHolder holder;

    public ListViewAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
        this.context = context;
        data = arrayList;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public HashMap<String, String> getItem(int positions) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {


        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_list_view_layout, null);
            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.first_name_text_view);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        resultp = data.get(position);
        holder.name.setText(resultp.get("text"));

        return convertView;
    }

    static class ViewHolder {
        TextView name;

    }

}
