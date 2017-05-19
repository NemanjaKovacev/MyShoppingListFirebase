package com.example.nemanja.myshoppinglistfirebase.holder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.nemanja.myshoppinglistfirebase.R;

public class ShoppingListViewHolder {
    public final TextView textViewName;

    public ShoppingListViewHolder(@NonNull View view) {
        textViewName = (TextView) view.findViewById(R.id.textViewItemName);
    }
}