package com.example.nemanja.myshoppinglistfirebase.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ShoppingList {
    private String shoppingListId;
    private String shoppingListName;
    private boolean allChecked;

    public ShoppingList() {
    }

    public ShoppingList(String shoppingListId, String shoppingListName, boolean allChecked) {
        this.shoppingListId = shoppingListId;
        this.shoppingListName = shoppingListName;
        this.allChecked = allChecked;
    }

    public String getShoppingListId() {
        return shoppingListId;
    }

    public String getShoppingListName() {
        return shoppingListName;
    }

    public boolean isAllChecked() {
        return allChecked;
    }
}
