package com.example.nemanja.myshoppinglistfirebase.listeners;

import com.example.nemanja.myshoppinglistfirebase.model.Item;

public interface OnCheckedListener {
    void onCheckedChanged(boolean isChecked, Item item);
}
