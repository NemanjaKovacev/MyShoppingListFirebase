package com.example.nemanja.myshoppinglistfirebase.adapter;


import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nemanja.myshoppinglistfirebase.R;
import com.example.nemanja.myshoppinglistfirebase.holder.ShoppingListViewHolder;
import com.example.nemanja.myshoppinglistfirebase.model.ShoppingList;

import java.util.List;

public class ListOfShoppingListsAdapter extends ArrayAdapter<ShoppingList> {
    private final Activity context;
    private final List<ShoppingList> shoppingLists;

    public ListOfShoppingListsAdapter(@NonNull Activity context, @NonNull List<ShoppingList> shoppingLists) {
        super(context, R.layout.card_shopping_list, shoppingLists);
        this.context = context;
        this.shoppingLists = shoppingLists;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ShoppingListViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.card_shopping_list, null, true);
            holder = new ShoppingListViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ShoppingListViewHolder) convertView.getTag();
        }

        ShoppingList shoppingList = shoppingLists.get(position);
        TextView textViewName = holder.textViewName;
        textViewName.setText(shoppingList.getShoppingListName());
        if (shoppingList.isAllChecked()) {
            textViewName.setPaintFlags(textViewName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            textViewName.setTextColor(ContextCompat.getColor(context, R.color.colorSecondaryText));
            textViewName.setTypeface(null, Typeface.NORMAL);
        } else {
            textViewName.setPaintFlags(textViewName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            textViewName.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            textViewName.setTypeface(null, Typeface.BOLD);
        }
        return convertView;
    }
}