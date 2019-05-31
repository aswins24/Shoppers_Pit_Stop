package com.example.ashwin.shopperspitstop;

import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private ShopsListAdapter shopsListAdapter;
    private DatabaseHandler handler;
    private Point size = new Point();
    private Button add_shop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        RecyclerView rv = (RecyclerView) findViewById(R.id.shop_list);

        layoutManager = new GridLayoutManager(this, 2); //GridLayout with 2 cards in a row
        rv.setLayoutManager(layoutManager);

        handler = new DatabaseHandler(this); //Connection to DataBase

        shopsListAdapter = new ShopsListAdapter(this); //Adapter object dealing with creating views and binding data to the view.
        main_activity_card_height(); //Adjusting Card height based on screen mode (portrait/landscape).


        Log.d("Cursor", "Cursor length is " + shopsListAdapter.getItemCount());

        rv.setAdapter(shopsListAdapter); //Connect RecyclerView with Adapter.

        add_shop = (Button) findViewById(R.id.Add_Shop);
        add_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View dialogue_view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.add_file, null); //Dialogue view Inflater.

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                alertDialogBuilder.setView(dialogue_view); //Set dialogue view inflater to alertdialoguebox.

                final EditText userInput = dialogue_view.findViewById(R.id.shop_name);

                alertDialogBuilder.setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(String.valueOf(userInput.getText()).length() != 0) {
                            handler.Add_Shop(String.valueOf(userInput.getText())); //Adding shop name to main Table.
                            shopsListAdapter.LoadNewData();
                            shopsListAdapter.notifyDataSetChanged(); //Notifies the adapter and view is redrawn.
                        } else{
                            Toast.makeText(getApplicationContext(),"Length of the Shop Name should greater than 1",Toast.LENGTH_SHORT).show();
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
            }
        });

    }

    public void main_activity_card_height() {
        Display display = getWindowManager().getDefaultDisplay();

        display.getSize(size);

        if (size.x < size.y) {
            shopsListAdapter.set_card_height(size.y / 6);
        } else if (size.x > size.y) {
            shopsListAdapter.set_card_height(size.x / 6);
        }
    }
}
