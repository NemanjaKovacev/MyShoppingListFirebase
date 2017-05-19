package com.example.nemanja.myshoppinglistfirebase.adapter;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.example.nemanja.myshoppinglistfirebase.R;
import com.example.nemanja.myshoppinglistfirebase.holder.ItemViewHolder;
import com.example.nemanja.myshoppinglistfirebase.listeners.OnCheckedListener;
import com.example.nemanja.myshoppinglistfirebase.model.Item;

import java.util.List;

public class ListOfItemsAdapter extends ArrayAdapter<Item> {
    private final OnCheckedListener onCheckedListener;
    private final Activity context;
    private final List<Item> items;

    public ListOfItemsAdapter(@NonNull Activity context, @NonNull List<Item> items, OnCheckedListener onCheckedListener) {
        super(context, R.layout.card_item, items);
        this.context = context;
        this.items = items;
        this.onCheckedListener = onCheckedListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ItemViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.card_item, null, true);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ItemViewHolder) convertView.getTag();
        }

        final Item item = items.get(position);
        holder.textViewName.setText(item.getItemName());
        holder.textViewAmount.setText(item.getAmount());
        holder.checkBox.setChecked(item.isChecked());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                onCheckedListener.onCheckedChanged(isChecked, item);
            }
        });
        return convertView;
    }
}