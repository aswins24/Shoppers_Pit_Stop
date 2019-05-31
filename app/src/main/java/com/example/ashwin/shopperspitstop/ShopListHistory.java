package com.example.ashwin.shopperspitstop;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;

/**
 * Created by ashwin on 5/3/2019.
 */
public class ShopListHistory extends MainActivity {

    private RecyclerView rv;
    private LinearLayoutManager linearLayoutManager;
    private HistoryAdapter historyAdapter;
    private Point size = new Point();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoplisthistory);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv = (RecyclerView) findViewById(R.id.shop_list_history);

        rv.setLayoutManager(linearLayoutManager);
        historyAdapter = new HistoryAdapter(getApplicationContext());
        history_activity_card_height();
        rv.setAdapter(historyAdapter);
    }

    public void history_activity_card_height() {
        Display display = getWindowManager().getDefaultDisplay();

        display.getSize(size);

        if (size.x < size.y) {
            historyAdapter.set_card_height(size.y / 6);
        } else if (size.x > size.y) {
            historyAdapter.set_card_height(size.x / 6);
        }
    }
}
