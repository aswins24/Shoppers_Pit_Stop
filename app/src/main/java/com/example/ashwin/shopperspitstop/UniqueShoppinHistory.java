package com.example.ashwin.shopperspitstop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by ashwin on 5/30/2019.
 */
public class UniqueShoppinHistory extends MainActivity {
    private String shop_name;
    private String date;
    private String time;
    private TextView Shop_Name;
    private TextView total;
    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private ShopListHistoryAdapter shopListHistoryAdapter;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_shop_cart);

        Intent intent = getIntent();
        // Bundle extras = intent.getExtras();

        shop_name = intent.getStringExtra("Shop Name");
        date = intent.getStringExtra("Date");
        time = intent.getStringExtra("Time");

        Shop_Name = (TextView) findViewById(R.id.shop_name);
        Shop_Name.setText((shop_name != null) ? shop_name : "");
        databaseHandler = new DatabaseHandler(getApplicationContext());

        total = (TextView) findViewById(R.id.total);


        total.setText(String.valueOf(databaseHandler.gettotalPrice("[" + shop_name + "_" + date + "_" + time + "]")));

        Log.d("UniqueShoppingHistory", " " + shop_name + " " + date + " " + time);

        rv = (RecyclerView) findViewById(R.id.list_item);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);

        shopListHistoryAdapter = new ShopListHistoryAdapter(getApplicationContext(), shop_name + "_" + date + "_" + time);

        rv.setAdapter(shopListHistoryAdapter);


    }
}
