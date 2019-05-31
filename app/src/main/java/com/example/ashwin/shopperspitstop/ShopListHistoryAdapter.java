package com.example.ashwin.shopperspitstop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ashwin on 5/30/2019.
 */
public class ShopListHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private String tablename;
    private DatabaseHandler databaseHandler;
    private List<Items> data;
    private Items _data;

    public ShopListHistoryAdapter(Context context, String tablename) {

        this.mContext = context;
        this.tablename = tablename;
        this.databaseHandler = new DatabaseHandler(mContext);
        data = this.databaseHandler.getAllitems(tablename);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_container_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        _data = data.get(position);
        if (holder instanceof ViewHolder) {

            ((ViewHolder) holder).id.setText(String.valueOf(_data.getId()));
            ((ViewHolder) holder).name.setText(String.valueOf(_data.getName() + "\n"));
            ((ViewHolder) holder).quantity.setText(String.valueOf(_data.getQuantity()));
            ((ViewHolder) holder).weight.setText(String.valueOf(_data.getWeight()));
            ((ViewHolder) holder).price.setText(String.valueOf(_data.getPrice()));

        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView id;
        TextView name;
        TextView quantity;
        TextView weight;
        TextView price;

        public ViewHolder(View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.Item_id);
            name = itemView.findViewById(R.id.Item_Name);
            quantity = itemView.findViewById(R.id.Quantity);
            weight = itemView.findViewById(R.id.Weight);
            price = itemView.findViewById(R.id.Price);

        }
    }
}
