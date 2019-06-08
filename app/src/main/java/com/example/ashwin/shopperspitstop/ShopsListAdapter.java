package com.example.ashwin.shopperspitstop;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ashwin on 5/3/2019.
 */
public class ShopsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    int size = 1;
    private Context mContext;
    private DatabaseHandler databaseHandler;
    private List<Shop_Name> Data = null;
    private List<Items> Data_1 = null;
    private int height;
    private Shop_Name _data;
    private SQLiteDatabase db;
    private int resource_layout;
    private String _tableName = null;
    private Items data_1;


    public ShopsListAdapter(Context context) {
        //Adapter for Main Activity to show Cards in the view.
        this.mContext = context;
        this.databaseHandler = new DatabaseHandler(mContext);
        this.Data = this.databaseHandler.getAllShops();
        resource_layout = R.layout.shop_list_database;

    }

    public ShopsListAdapter(Context context, String TableName) {
        // Adapter for ShopCart Activity to shopping items
        this.mContext = context;
        this.databaseHandler = new DatabaseHandler(mContext);
        this._tableName = TableName;
        resource_layout = R.layout.item_container;
        this.Data_1 = this.databaseHandler.getAllitems(this._tableName);
    }

//

    @Override
    public int getItemCount() {

        if (resource_layout == R.layout.shop_list_database) {
            return Data.size();  // Data for Main Activity
        } else {
            return Data_1.size(); //Data for ShopCArt Activity
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("ViewHolderPosition", " " + position);
        if (holder instanceof ViewHolder) {

                //CardView for each Shop
                _data = Data.get(position);
                final String _shop_Name = String.valueOf(_data.getShopName());
                ((ViewHolder) holder)._textView.setText(_shop_Name);
                //holder._textView.setText(_shop_Name);
                databaseHandler.New_Table(_shop_Name);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, ShopCart.class);
                        intent.putExtra("Shop Name", _shop_Name);
                        mContext.startActivity(intent);
                    }
                });

        } else if (holder instanceof ViewHolder1) {
            data_1 = Data_1.get(position);

            int _item_no = data_1.getId();
            String _item_Name = data_1.getName();
            int _quantity = data_1.getQuantity();
            String _weight = data_1.getWeight();
            double _price = data_1.getPrice();

            ((ViewHolder1) holder).item_no.setText(String.valueOf(_item_no));
            ((ViewHolder1) holder).item_Name.setText(String.valueOf(_item_Name) + "\n"); //New line to avoid clipping of texts at bottom
            ((ViewHolder1) holder).quantity.setText(String.valueOf(_quantity));
            ((ViewHolder1) holder).weight.setText(String.valueOf(_weight));
            ((ViewHolder1) holder).price.setText(String.valueOf(_price));
            ((ViewHolder1) holder).price.setFocusable(true);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemview = LayoutInflater.from(mContext).inflate(resource_layout, parent, false);

        if (resource_layout == R.layout.shop_list_database) {

            return new ViewHolder(itemview);

        } else {

            return new ViewHolder1(_tableName, itemview, mContext);
        }

    }

    public void set_card_height(int Height) {

        this.height = Height; //Setting card height based on screen size

    }

    public void LoadNewData() {

        if (resource_layout == R.layout.shop_list_database) {

            this.Data = databaseHandler.getAllShops();

        } else if (resource_layout == R.layout.item_container) {

            this.Data_1 = databaseHandler.getAllitems(_tableName);

        }

    }

    public double Calculate() {
        LoadNewData();
        double total = 0;

        for (Items it : Data_1) {

            total += it.getPrice() * it.getQuantity();
        }
        return total;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View v1;
        TextView _textView;

        public ViewHolder(View itemview) {
            //ViewHolder for MainActivity
            super(itemview);
            v1 = itemview;
            v1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
            _textView = v1.findViewById(R.id.Table_name);

        }


    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {
        //ViewHolder for ShopCart
        View v1;
        EditText price;
        TextView item_no;
        TextView item_Name;
        TextView quantity;
        TextView weight;
        DatabaseHandler handler;


        public ViewHolder1(final String str, View itemview, Context context) {
            super(itemview);
            v1 = itemview;
            item_no = itemview.findViewById(R.id.Item_id);
            item_Name = itemview.findViewById(R.id.Item_Name);
            quantity = itemview.findViewById(R.id.Quantity);
            weight = itemview.findViewById(R.id.Weight);
            price = itemview.findViewById(R.id.Price);
            handler = new DatabaseHandler(context);


            price.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        Log.d("FocusListener", "Item no. is " + item_no.getText().toString());

                        price.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                ContentValues value = new ContentValues();
                                double Price;
                                try {
                                    Price = Double.parseDouble(price.getText().toString().trim());
                                } catch (Exception e) {
                                    Price = 0.0;
                                }
                                value.put("Price", Price);
                                int pos = Integer.parseInt(item_no.getText().toString().trim());
                                handler.UpdatePrice(str, value, pos);
                                Calculate();

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });

                    } else {

                        price.removeTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                    }


                }
            });


        }

    }
}
