package com.example.nemanja.myshoppinglistfirebase.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nemanja.myshoppinglistfirebase.MainActivity;
import com.example.nemanja.myshoppinglistfirebase.R;
import com.example.nemanja.myshoppinglistfirebase.adapter.ListOfShoppingListsAdapter;
import com.example.nemanja.myshoppinglistfirebase.firebase.Firebase;
import com.example.nemanja.myshoppinglistfirebase.model.ShoppingList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShoppingListsFragment extends Fragment {

    private TextInputEditText editTextShoppingListName;
    private TextInputEditText editTextItemName;
    TextInputEditText editTextItemNameUpdate;
    private TextInputEditText editTextItemAmount;
    CheckBox checkBox;
    private Button buttonAddShoppingList;
    private ListView listViewShoppingLists;
    private Firebase firebase;
    private FloatingActionButton fab;
    private List<ShoppingList> shoppingLists;
    private DatabaseReference databaseShoppingLists;
    private DatabaseReference databaseItems;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_lists, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        fab = (FloatingActionButton) view.findViewById(R.id.fabShopList);
        databaseShoppingLists = FirebaseDatabase.getInstance().getReference("shoppingLists");
        editTextShoppingListName = (TextInputEditText) view.findViewById(R.id.editTextShoppingListName);
        TextInputEditText editTextShoppingListNameUpdate = (TextInputEditText) view.findViewById(R.id.editTextShoppingListNameUpdate);
        listViewShoppingLists = (ListView) view.findViewById(R.id.listViewShoppingLists);
        buttonAddShoppingList = (Button) view.findViewById(R.id.buttonAddShoppingList);
        TextView textViewShoppingLists = (TextView) view.findViewById(R.id.textViewShoppingLists);
        shoppingLists = new ArrayList<>();
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.fragment_shopping_lists);

        firebase = new Firebase(editTextShoppingListName, editTextItemName,
                editTextItemAmount, databaseShoppingLists, databaseItems);

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editTextShoppingListName.setVisibility(View.VISIBLE);
                buttonAddShoppingList.setVisibility(View.VISIBLE);
                fab.setVisibility(View.INVISIBLE);
            }
        });

        buttonAddShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                String name = editTextShoppingListName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    firebase.addShoppingList();
                    fab.setVisibility(View.VISIBLE);
                    editTextShoppingListName.setVisibility(View.GONE);
                    buttonAddShoppingList.setVisibility(View.GONE);
                    Toast.makeText(getContext(), R.string.shopping_list_created, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), R.string.shopping_list_enter, Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewShoppingLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextShoppingListName.setVisibility(View.GONE);
                buttonAddShoppingList.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextShoppingListName.setVisibility(View.GONE);
                buttonAddShoppingList.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        });

        listViewShoppingLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShoppingList shoppingList = shoppingLists.get(i);
                String shoppingListId = shoppingList.getShoppingListId();
                String shoppingListName = shoppingList.getShoppingListName();
                ((MainActivity) getActivity()).openShoppingListFragment(shoppingListId, shoppingListName);
            }
        });

        listViewShoppingLists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShoppingList shoppingList = shoppingLists.get(i);
                showUpdateDeleteDialog(shoppingList.getShoppingListId(), shoppingList.getShoppingListName(), shoppingList.isAllChecked());
                return true;
            }
        });
    }

    private void showUpdateDeleteDialog(@NonNull final String shoppingListId, String shoppingListName, final boolean allChecked) {
        if (getContext() == null) {
            return;
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog_shopping_list, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextShoppingListNameUpdate = (EditText) dialogView.findViewById(R.id.editTextShoppingListNameUpdate);
        final Button buttonUpdateShoppingList = (Button) dialogView.findViewById(R.id.buttonUpdateShoppingList);
        final Button buttonDeleteShoppingList = (Button) dialogView.findViewById(R.id.buttonDeleteShoppingList);

        dialogBuilder.setTitle(shoppingListName);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdateShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextShoppingListNameUpdate.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    firebase.updateShoppingList(shoppingListId, name, allChecked);
                    alertDialog.dismiss();
                    Toast.makeText(getContext(), R.string.shopping_list_updated, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), R.string.shopping_list_enter, Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonDeleteShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebase.deleteShoppingList(shoppingListId);
                alertDialog.dismiss();
                Toast.makeText(getContext(), R.string.shopping_list_deleted, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseShoppingLists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getActivity() == null) {
                    return;
                }
                shoppingLists.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ShoppingList shoppingList = postSnapshot.getValue(ShoppingList.class);
                    shoppingLists.add(shoppingList);
                }

                Collections.sort(shoppingLists, new Comparator<ShoppingList>() {
                    @Override
                    public int compare(@NonNull ShoppingList s1, @NonNull ShoppingList s2) {
                        if (s1.isAllChecked() && !s2.isAllChecked()) {
                            return 1;
                        } else if (!s1.isAllChecked() && s2.isAllChecked()) {
                            return -1;
                        }
                        return 0;
                    }
                });

                ListOfShoppingListsAdapter shoppingListAdapter = new ListOfShoppingListsAdapter(getActivity(), shoppingLists);
                listViewShoppingLists.setAdapter(shoppingListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
