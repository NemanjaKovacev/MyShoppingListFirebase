package com.example.nemanja.myshoppinglistfirebase;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.nemanja.myshoppinglistfirebase.constants.Constants;
import com.example.nemanja.myshoppinglistfirebase.fragments.ItemsListFragment;
import com.example.nemanja.myshoppinglistfirebase.fragments.ShoppingListsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ShoppingListsFragment fragment = new ShoppingListsFragment();
        fragmentTransaction.add(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    public void openShoppingListFragment(String shoppingListId, String shoppingListName) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SHOPPINGLIST_ID, shoppingListId);
        bundle.putString(Constants.SHOPPINGLIST_NAME, shoppingListName);

        ItemsListFragment itemsListFragment = new ItemsListFragment();
        itemsListFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, itemsListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}