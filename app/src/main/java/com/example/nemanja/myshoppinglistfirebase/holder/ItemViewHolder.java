package com.example.nemanja.myshoppinglistfirebase.holder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.nemanja.myshoppinglistfirebase.R;

public class ItemViewHolder {

    public final TextView textViewName;
    public final TextView textViewAmount;
    public final CheckBox checkBox;

    public ItemViewHolder(@NonNull View view) {
        textViewName = (TextView) view.findViewById(R.id.textViewItemName);
        textViewAmount = (TextView) view.findViewById(R.id.textViewItemAmount);
        checkBox = (CheckBox) view.findViewById(R.id.checkbox);
    }
}
