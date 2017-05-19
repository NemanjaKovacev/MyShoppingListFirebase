package com.example.nemanja.myshoppinglistfirebase.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Item {
    private String itemId;
    private String itemName;
    private String amount;
    private boolean checked;

    public Item() {
    }

    public Item(String itemId, String itemName, String amount, boolean checked) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.amount = amount;
        this.checked = checked;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getAmount() {
        return amount;
    }

    public boolean isChecked() {
        return checked;
    }
}
