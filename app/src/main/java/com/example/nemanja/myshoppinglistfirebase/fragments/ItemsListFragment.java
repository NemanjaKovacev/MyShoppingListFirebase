package com.example.nemanja.myshoppinglistfirebase.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nemanja.myshoppinglistfirebase.R;
import com.example.nemanja.myshoppinglistfirebase.adapter.ListOfItemsAdapter;
import com.example.nemanja.myshoppinglistfirebase.constants.Constants;
import com.example.nemanja.myshoppinglistfirebase.firebase.Firebase;
import com.example.nemanja.myshoppinglistfirebase.listeners.OnCheckedListener;
import com.example.nemanja.myshoppinglistfirebase.model.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ItemsListFragment extends Fragment {

    private Button buttonAddItem;
    private TextInputEditText editTextShoppingListName;
    private TextInputEditText editTextItemName;
    private TextInputEditText editTextItemAmount;
    private ListView listViewItems;
    private Firebase firebase;
    private FloatingActionButton fab;
    private DatabaseReference databaseShoppingLists;
    private List<Item> items;
    @Nullable
    private String shoppingListId, shoppingListName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_items_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = this.getArguments();
        shoppingListId = bundle.getString(Constants.SHOPPINGLIST_ID);
        shoppingListName = bundle.getString(Constants.SHOPPINGLIST_NAME);

        View view = getView();
        DatabaseReference databaseItems = FirebaseDatabase.getInstance().getReference("items").child(shoppingListId);
        buttonAddItem = (Button) view.findViewById(R.id.buttonAddItem);
        editTextItemName = (TextInputEditText) view.findViewById(R.id.editTextItemName);
        TextInputEditText editTextItemNameUpdate = (TextInputEditText) view.findViewById(R.id.editTextItemNameUpdate);
        TextInputEditText editTextShoppingListNameUpdate = (TextInputEditText) view.findViewById(R.id.editTextShoppingListNameUpdate);
        editTextItemAmount = (TextInputEditText) view.findViewById(R.id.editTextAmount);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        TextView textViewShoppingList = (TextView) view.findViewById(R.id.textViewShoppingList);
        listViewItems = (ListView) view.findViewById(R.id.listViewItems);
        fab = (FloatingActionButton) view.findViewById(R.id.fabItemsList);
        items = new ArrayList<>();
        firebase = new Firebase(editTextShoppingListName, editTextItemName, editTextItemAmount,
                                databaseShoppingLists, databaseItems);

        textViewShoppingList.setText(shoppingListName);

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editTextItemName.setVisibility(View.VISIBLE);
                editTextItemAmount.setVisibility(View.VISIBLE);
                buttonAddItem.setVisibility(View.VISIBLE);
                fab.setVisibility(View.INVISIBLE);
            }
        });

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                String name = editTextItemName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    firebase.saveItem();
                    fab.setVisibility(View.VISIBLE);
                    editTextItemName.setVisibility(View.GONE);
                    editTextItemAmount.setVisibility(View.GONE);
                    buttonAddItem.setVisibility(View.GONE);
                    Toast.makeText(getContext(), R.string.item_added, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), R.string.item_enter, Toast.LENGTH_SHORT).show();
                }
            }
        });

        listViewItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item item = items.get(i);
                showUpdateDeleteDialogItem(item.getItemId(), item.getItemName(), item.getAmount(), item.isChecked());
                return true;
            }
        });

        databaseItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!isAdded()) {
                    return;
                }
                items.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    items.add(item);
                }
                checkIfAllChecked();
                if (getActivity() != null) {
                    ListOfItemsAdapter listOfItemsAdapterAdapter = new ListOfItemsAdapter(getActivity(), items, new OnCheckedListener() {
                        @Override
                        public void onCheckedChanged(boolean isChecked, @NonNull Item item) {
                            firebase.updateItem(item.getItemId(), item.getItemName(), item.getAmount(), isChecked);
                        }
                    });
                    listViewItems.setAdapter(listOfItemsAdapterAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void checkIfAllChecked() {
        boolean allChecked = items.size() > 0;
        for (Item item : items) {
            allChecked = allChecked && item.isChecked();
        }
        firebase.updateShoppingList(shoppingListId, shoppingListName, allChecked);
    }

    private void showUpdateDeleteDialogItem(@NonNull final String id, String itemName, String amount, final boolean isChecked) {
        if (getContext() == null) {
            return;
        }

        AlertDialog.Builder dialogBuilderItem = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog_item, null);
        dialogBuilderItem.setView(dialogView);

        final EditText editTextItemNameUpdate = (EditText) dialogView.findViewById(R.id.editTextItemNameUpdate);
        final EditText editTextAmount = (EditText) dialogView.findViewById(R.id.editTextAmount);
        final Button buttonUpdateItem = (Button) dialogView.findViewById(R.id.buttonUpdateItem);
        final Button buttonDeleteItem = (Button) dialogView.findViewById(R.id.buttonDeleteItem);

        dialogBuilderItem.setTitle(itemName);
        final AlertDialog alertDialogItem = dialogBuilderItem.create();
        alertDialogItem.show();

        buttonUpdateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextItemNameUpdate.getText().toString().trim();
                String amount = editTextAmount.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    firebase.updateItem(id, name, amount, isChecked);
                    alertDialogItem.dismiss();
                    Toast.makeText(getContext(), R.string.item_updated, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), R.string.item_enter, Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebase.deleteItem(id);
                alertDialogItem.dismiss();
                Toast.makeText(getContext(), R.string.item_deleted, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
