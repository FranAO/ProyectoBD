package com.example.proyectobd.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.proyectobd.Models.MenuItem;
import com.example.proyectobd.R;

import java.util.List;

public class MenuAdapter extends BaseAdapter {
    private Context context;
    private List<MenuItem> menuItems;

    public MenuAdapter(Context context, List<MenuItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_menu, parent, false);
            holder = new ViewHolder();
            holder.ivIcon = convertView.findViewById(R.id.ivIcon);
            holder.tvTitle = convertView.findViewById(R.id.tvTitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MenuItem item = menuItems.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.ivIcon.setImageResource(item.getIconResource());

        return convertView;
    }

    static class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
    }
}
