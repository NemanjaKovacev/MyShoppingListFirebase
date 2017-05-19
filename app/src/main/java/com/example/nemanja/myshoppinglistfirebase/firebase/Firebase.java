package com.example.nemanja.myshoppinglistfirebase.firebase;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.widget.EditText;

import com.example.nemanja.myshoppinglistfirebase.model.Item;
import com.example.nemanja.myshoppinglistfirebase.model.ShoppingList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase {

    public Firebase(TextInputEditText editTextShoppingListName, TextInputEditText editTextItemName,
                    TextInputEditText editTextItemAmount, DatabaseReference databaseShoppingLists,
                    DatabaseReference databaseItems) {
        this.editTextShoppingListName = editTextShoppingListName;
        this.editTextItemName = editTextItemName;
        this.databaseShoppingLists = databaseShoppingLists;
        this.databaseItems = databaseItems;
        this.editTextItemAmount = editTextItemAmount;
    }

    private final EditText editTextShoppingListName;
    private final EditText editTextItemName;
    private final EditText editTextItemAmount;
    private final DatabaseReference databaseShoppingLists;
    private final DatabaseReference databaseItems;

    public boolean updateShoppingList(@NonNull String id, String name, boolean allChecked) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("shoppingLists").child(id);
        ShoppingList shoppingList = new ShoppingList(id, name, allChecked);
        databaseReference.setValue(shoppingList);
        return true;
    }

    public boolean deleteShoppingList(@NonNull String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("shoppingLists").child(id);
        databaseReference.removeValue();
        DatabaseReference databaseReferenceItems = FirebaseDatabase.getInstance().getReference("items").child(id);
        databaseReferenceItems.removeValue();
        return true;
    }

    public void addShoppingList() {
        String name = editTextShoppingListName.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) {
            String id = databaseShoppingLists.push().getKey();
            ShoppingList shoppingList = new ShoppingList(id, name, false);
            databaseShoppingLists.child(id).setValue(shoppingList);
            FirebaseDatabase.getInstance().getReference("items").child(id);
            editTextShoppingListName.setText("");
        }
    }

    public boolean updateItem(@NonNull String id, String name, String amount, boolean checked) {
        String key = databaseItems.getKey();
        DatabaseReference databaseItems = FirebaseDatabase.getInstance().getReference("items").child(key).child(id);
        Item item = new Item(id, name, amount, checked);
        databaseItems.setValue(item);
        return true;
    }

    public boolean deleteItem(@NonNull String id) {
        String key = databaseItems.getKey();
        DatabaseReference databaseItems = FirebaseDatabase.getInstance().getReference("items").child(key).child(id);
        databaseItems.removeValue();
        return true;
    }

    public void saveItem() {
        String name = editTextItemName.getText().toString().trim();
        String amount = editTextItemAmount.getText().toString().trim();
        boolean checked = false;
        if (!TextUtils.isEmpty(name)) {
            String id = databaseItems.push().getKey();
            Item item = new Item(id, name, amount, checked);
            databaseItems.child(id).setValue(item);
            editTextItemName.setText("");
            editTextItemAmount.setText("");
        }
    }
}
