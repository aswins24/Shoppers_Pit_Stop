package com.example.ashwin.shopperspitstop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ashwin on 5/26/2019.
 */
public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int SIMPLE_TEXT_VIEW = 0;
    private static final int CARD_VIEW = 1;
    private Context mContext;
    private DatabaseHandler db;
    private int resource_layout;
    private List<HistoryItems> data = null;
    private HistoryItems _data;
    private int height;


    public HistoryAdapter(Context context) {
        //Adapter to show all the Shops with their shopping history
        //Two kind of layouts are required since we have to shop name as header
        this.mContext = context;
        this.db = new DatabaseHandler(mContext);
        this.data = db.getHistoryItems();

    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        _data = data.get(position);

        return _data.get_type();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SIMPLE_TEXT_VIEW) { //Header View
            View itemview = LayoutInflater.from(mContext).inflate(R.layout.history_container, parent, false);
            return new ViewHolder(itemview);
        } else if (viewType == CARD_VIEW) {// Shopping History of the corresponding header
            View itemview = LayoutInflater.from(mContext).inflate(R.layout.history_container_card, parent, false);
            return new ViewHolder1(itemview);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            _data = data.get(position);
            ((ViewHolder) holder).Shop_Name.setText(_data.get_header());
        } else if (holder instanceof ViewHolder1) {
            _data = data.get(position);
            final String header = _data.get_header();
            final String date = _data.getDate();
            final String time = _data.getTime();

            ((ViewHolder1) holder).name.setText(header);
            ((ViewHolder1) holder).date.setText(date);
            ((ViewHolder1) holder).time.setText(time);
            ((ViewHolder1) holder).price.setText(String.valueOf(_data.get_price()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, UniqueShoppinHistory.class);


                    intent.putExtra("Shop Name", String.valueOf(header));
                    intent.putExtra("Date", String.valueOf(date));
                    intent.putExtra("Time", String.valueOf(time));

                    mContext.startActivity(intent);
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void set_card_height(int Height) {

        this.height = Height;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //ViewHolder to show Shop name as header
        TextView Shop_Name;

        public ViewHolder(View itemview) {
            super(itemview);

            Shop_Name = itemview.findViewById(R.id.name);
        }
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {
        //ViewHolder to show Shopping History of the corresponding header
        TextView name;
        TextView date;
        TextView time;
        TextView price;

        public ViewHolder1(View itemview) {
            super(itemview);
            name = itemview.findViewById(R.id.shopName);
            date = itemview.findViewById(R.id.Date);
            time = itemview.findViewById(R.id.Time);
            price = itemview.findViewById(R.id.Total);
            itemview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));


        }
    }

    public void LoadNewHistoryData(){

        this.data = db.getHistoryItems();
        Log.d("LoadNewHistory", " New History is Loaded");
    }
}
