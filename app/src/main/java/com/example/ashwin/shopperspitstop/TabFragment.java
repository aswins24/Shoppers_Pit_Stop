package com.example.ashwin.shopperspitstop;

import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by ashwin on 6/5/2019.
 */
public class TabFragment extends Fragment {

    private static HistoryAdapter historyAdapter;
    int position;
    private RecyclerView.LayoutManager layoutManager;
    private ShopsListAdapter shopsListAdapter;
    private DatabaseHandler handler;
    private Point size = new Point();
    private RecyclerView rv;
    private LinearLayoutManager linearLayoutManager;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        TabFragment tabFragment = new TabFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    public static HistoryAdapter getInstance() {
        if (historyAdapter != null) {
            return historyAdapter;
        }
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt("pos");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.shopnames_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (position == 0) {

            RecyclerView rv = view.findViewById(R.id.shop_name_list);

            layoutManager = new GridLayoutManager(getContext(), 2); //GridLayout with 2 cards in a row
            rv.setLayoutManager(layoutManager);

            handler = new DatabaseHandler(getContext()); //Connection to DataBase

            shopsListAdapter = new ShopsListAdapter(getContext()); //Adapter object dealing with creating views and binding data to the view.
            main_activity_card_height(); //Adjusting Card height based on screen mode (portrait/landscape).
            Log.d("Fragment", "First Tab Created");

            Log.d("Cursor", "Cursor length is " + shopsListAdapter.getItemCount());

            rv.setAdapter(shopsListAdapter); //Connect RecyclerView with Adapter.
        } else if (position == 1) {

            linearLayoutManager = new LinearLayoutManager(getContext());
            rv = view.findViewById(R.id.shop_name_list);
            Log.d("Fragment", "Second Tab Created");
            rv.setLayoutManager(linearLayoutManager);
            historyAdapter = new HistoryAdapter(getContext());
            history_activity_card_height();
            rv.setAdapter(historyAdapter);

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mymenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addbutton:
                View dialogue_view = LayoutInflater.from(getContext()).inflate(R.layout.add_file, null); //Dialogue view Inflater.
//
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                alertDialogBuilder.setView(dialogue_view); //Set dialogue view inflater to alertdialoguebox.

                final EditText userInput = dialogue_view.findViewById(R.id.shop_name);

                alertDialogBuilder.setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (String.valueOf(userInput.getText()).length() != 0) {
                            handler.Add_Shop(String.valueOf(userInput.getText())); //Adding shop name to main Table.
                            shopsListAdapter.LoadNewData();
                            shopsListAdapter.notifyDataSetChanged(); //Notifies the adapter and view is redrawn.
                        } else {
                            Toast.makeText(getContext(), "Length of the Shop Name should greater than 1", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void main_activity_card_height() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();

        display.getSize(size);

        if (size.x < size.y) {
            shopsListAdapter.set_card_height(size.y / 6);
        } else if (size.x > size.y) {
            shopsListAdapter.set_card_height(size.x / 6);
        }
    }

    public void history_activity_card_height() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();

        display.getSize(size);

        if (size.x < size.y) {
            historyAdapter.set_card_height(size.y / 6);
        } else if (size.x > size.y) {
            historyAdapter.set_card_height(size.x / 6);
        }
    }

}
