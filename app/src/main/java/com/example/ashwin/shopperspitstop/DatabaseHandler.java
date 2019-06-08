package com.example.ashwin.shopperspitstop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ashwin on 5/5/2019.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Shopping Databse";
    //Table Names
    private static final String SHOP_TABLE_NAME = "Shop_Names";
    private static final String UNIQUE_HISTORY_NAME = "Unique_History_Name";
    //Shop Names Columns
    private static final String COLUMN_SHOP_NAME = "Shop_Name";
    private static final String COLUMN_LINK_ID = "Table_id";
    private static final String COLUMN_DATE = "Date";
    private static final String COLUMN_TIME = "Time";
    //Common Columns
    private static final String COLUMN_ID = "_id";
    //Shopping Table Columns
    private static final String COLUMN_NAME = "Item_name";
    private static final String COLUMN_QUANTITY = "Quantity";
    private static final String COLUMN_WEIGHT = "Weight";
    private static final String COLUMN_PRICE = "Price";
    private static final String TABLE_NAME = "Shop_Name";

    private String SHOP_SHOPPING_TABLE_NAME = null;
    private String HISTROY_TABLE_NAME = null;


    public DatabaseHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Table that holds list of all the shops the user had gone or will go for shoppping
        final String SHOP_TABLE = "CREATE TABLE IF NOT EXISTS " + SHOP_TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_SHOP_NAME + " TEXT UNIQUE NOT NULL" + ")";
        db.execSQL(SHOP_TABLE);
        Log.d("Database", "Created " + SHOP_TABLE_NAME + " Successfully");

        //Table that holds unique table name for each entry to history table
        final String UNIQUE_HISTORY_ID = "CREATE TABLE IF NOT EXISTS " + UNIQUE_HISTORY_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " + TABLE_NAME + " TEXT UNIQUE NOT NULL, " + COLUMN_LINK_ID + " INTEGER, " + COLUMN_DATE + " TEXT, " + COLUMN_TIME + " TEXT" + ")";
        Log.d("DataBase", " " + UNIQUE_HISTORY_ID);
        db.execSQL(UNIQUE_HISTORY_ID);
        Log.d("Database", "Created " + UNIQUE_HISTORY_NAME + " Successfully");

        //Creates Table for each Shop Name in SHOP_TABLE
        if (SHOP_SHOPPING_TABLE_NAME != null) {
            final String SHOPPING_TABLE = "CREATE TABLE IF NOT EXISTS " + SHOP_SHOPPING_TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT NOT NULL UNIQUE," + COLUMN_QUANTITY + " INTEGER NOT NULL," + COLUMN_WEIGHT + " REAL," + COLUMN_PRICE + " REAL" + ")";
            db.execSQL(SHOPPING_TABLE);
            Log.d("Database", "Created " + SHOP_SHOPPING_TABLE_NAME + " Successfully");
        }

        if (HISTROY_TABLE_NAME != null) {
            final String HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS " + HISTROY_TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_NAME + " TEXT NOT NULL UNIQUE," + COLUMN_QUANTITY + " INTEGER NOT NULL," + COLUMN_WEIGHT + " REAL," + COLUMN_PRICE + " REAL" + ")";
            db.execSQL(HISTORY_TABLE);
            Log.d("Database", "Created " + HISTROY_TABLE_NAME + " Successfully");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + SHOP_TABLE_NAME);
        //Updated Database
        onCreate(db);
    }


    public void Add_Shop(String shop_name) { //Part of Main Activity
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_SHOP_NAME, shop_name);
        db.insert(SHOP_TABLE_NAME, null, cv); //Adds shop name to SHOP_TABLE
        Log.d("Add Shop", "Shop Added is " + shop_name);

        db.close();
    }

    public List<Shop_Name> getAllShops() {//Part of Main Activity

        List<Shop_Name> shop = new ArrayList<>(); //To store all the shop names in SHOP_TABLE.

        SQLiteDatabase db = this.getReadableDatabase();
        onCreate(db);  //Fail safe mechanism in case if Database is created but Table isn't.
        String querry = "SELECT * FROM " + SHOP_TABLE_NAME;


        Cursor cursor = db.rawQuery(querry, null);

        if (cursor.moveToFirst()) {
            do {
                Shop_Name _shopName = new Shop_Name();

                _shopName.setId(cursor.getInt(0));
                _shopName.setShopName(cursor.getString(1));
                Log.d("Get All Shops", "Shop Name is " + cursor.getString(1));
                shop.add(_shopName);

            } while (cursor.moveToNext());


        }
        cursor.close();
        db.close();

        return shop;
    }

    public void New_Table(String table_Name) {//Part of Main Activity and ShopCart Activity
        //For each User Input a new Table is created.
        SQLiteDatabase db = this.getReadableDatabase();
        this.SHOP_SHOPPING_TABLE_NAME = "[" + table_Name + "]";
        onCreate(db);
        this.SHOP_SHOPPING_TABLE_NAME = null;
        db.close();

    }

    public void Additem(String TableName, String ItemName, int Quantity, String Weight, double Price) { //Part of ShopCart Activity

        SQLiteDatabase db = this.getWritableDatabase();

        //Used in UpdateDatbase Function

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, ItemName);
        values.put(COLUMN_QUANTITY, Quantity);
        values.put(COLUMN_WEIGHT, Weight);
        values.put(COLUMN_PRICE, Price);
        String _tableName = "[" + TableName + "]";

        // Inserting entry to Table

        db.insert(_tableName, null, values);
        db.close();// Close table after each entry
    }

    public void Additem(String TableName, String ItemName, int Quantity, double Weight, String Weight_Measurement, double Price) { //Part of ShopCart Activity

        SQLiteDatabase db = this.getWritableDatabase();

        //Used in Add item button

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, ItemName);
        values.put(COLUMN_QUANTITY, Quantity);

        if (Weight != 0) {

            values.put(COLUMN_WEIGHT, Weight + "" + Weight_Measurement);

        } else {

            values.put(COLUMN_WEIGHT, "-");

        }
        values.put(COLUMN_PRICE, Price);
        String _tableName = "[" + TableName + "]"; //White spaces are not allowed for Table Name, hence add "[" and "]" before and after user input for shop name.
        // Inserting entry to Table

        db.insert(_tableName, null, values);
        db.close();// Close table after each entry
    }


    public List<Items> getAllitems(String TableName) { //Used to store contents in table before delteing the table for Updating table.

        List<Items> ItemList = new ArrayList<Items>();
        String str = "[" + TableName + "]";
        SQLiteDatabase db = this.getReadableDatabase();

        String querry = "SELECT * FROM " + str;


        Cursor cursor = db.rawQuery(querry, null);

        if (cursor.moveToFirst()) {
            do {
                Items items = new Items();

                items.setId(Integer.parseInt(cursor.getString(0)));

                items.setName(cursor.getString(1));

                items.setQuantity(Integer.parseInt(cursor.getString(2)));

                items.setWeight(cursor.getString(3));

                items.setPrice(Double.parseDouble(cursor.getString(4)));

                ItemList.add(items);
            } while (cursor.moveToNext());


        }
        cursor.close();
        db.close();
        return ItemList;
    }


    public void UpdateDatabase(String TableName) {
        //Required once an item is removed from shopping list.
        List<Items> updatedItems = getAllitems(TableName);

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + "[" + TableName + "]");
        New_Table(TableName); //Need to create the Table again


        for (Items it : updatedItems) {

            Additem(TableName, it._item_Name, it.getQuantity(), it.getWeight(), it.getPrice());

        }
        db.close();
    }

    public void UpdatePrice(String TableName, ContentValues value, int pos) {

        SQLiteDatabase db = this.getReadableDatabase();
        db.update("[" + TableName + "]", value, "_id" + "=?", new String[]{String.valueOf(pos)});
        db.close();

    }

    public void NewHistoryTable(String TableName) {
        //Creating unique table name to hold list of items for a particular shop at specified time;
        List<Items> shopping_list = getAllitems(TableName);
        if (shopping_list.size() > 0) {
            //Date and time can be used to get unique name
            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

            this.HISTROY_TABLE_NAME = "[" + TableName + "_" + date + "_" + time + "]";
            Add_Unique_Table_Name(this.HISTROY_TABLE_NAME, TableName, date, time); //Adding unique table name to the table that holds all the unique table names


            for (Items it : shopping_list) {

                Additem(TableName + "_" + date + "_" + time, it.getName(), it.getQuantity(), it.getWeight(), it.getPrice());
            }


        }
    }

    private void Add_Unique_Table_Name(String uniqueTableName, String TableName, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();

        String tableName = "[" + TableName + "]";
        String querry = "SELECT " + SHOP_TABLE_NAME + "." + COLUMN_ID + " FROM " + SHOP_TABLE_NAME + " WHERE " + SHOP_TABLE_NAME + "." + COLUMN_SHOP_NAME + " =?";
        Cursor cursor = db.rawQuery(querry, new String[]{TableName});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            ContentValues contentValues = new ContentValues();
            contentValues.put(TABLE_NAME, uniqueTableName);
            contentValues.put(COLUMN_LINK_ID, id);
            contentValues.put(COLUMN_DATE, date);
            contentValues.put(COLUMN_TIME, time);
            db.insert(UNIQUE_HISTORY_NAME, null, contentValues);
            Log.d("Unique Table Names", "Added entries Table Name = " + uniqueTableName + ", id = " + id + ", Date = " + date + " Time = " + time);
        }
        cursor.close();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        New_Table(TableName);
        db.close();

    }

    public List<HistoryItems> getHistoryItems() {

        List<HistoryItems> historyItems = new ArrayList<>();
        List<Shop_Name> shop_name = getAllShops();
        Cursor cursor = null;




        HistoryItems _historyitems;

        for (Shop_Name shop : shop_name) {


            String query = "SELECT * FROM " + UNIQUE_HISTORY_NAME + " WHERE " + UNIQUE_HISTORY_NAME + "." + COLUMN_LINK_ID + " =?";
            SQLiteDatabase db = this.getReadableDatabase();
            try {

                Log.d("HistoryItems", "Before Query " + db + " " + cursor);
                cursor = db.rawQuery(query, new String[]{String.valueOf(shop.getId())});
                Log.d("HistoryItems", "After Query " + cursor.getCount());

                if (cursor.moveToFirst()) {
                    _historyitems = new HistoryItems();
                    _historyitems.set_header(shop.getShopName());
                    Log.d("HistoryItems", "Current Shop Name is " + shop.getShopName());
                    historyItems.add(_historyitems);
                    do {
                        String name = cursor.getString(1);
                        Log.d("HistoryItems", "ID is " + cursor.getInt(0) + " Name is " + cursor.getString(1) + " Link id is " + cursor.getInt(2) + " Date is " + cursor.getString(3) + " Time is " + cursor.getString(4));
                        _historyitems = new HistoryItems();
                        _historyitems.set_id(cursor.getInt(0));
                        _historyitems.set_table_name(name);
                        _historyitems.set_link_id(cursor.getInt(2));
                        _historyitems.set_date(cursor.getString(3)); //Better to use cursor than wasting memory for temporary variable
                        _historyitems.set_time(cursor.getString(4));
                        _historyitems.set_header_no_type(shop.getShopName());
                        _historyitems.set_price(gettotalPrice(name));
                        historyItems.add(_historyitems);
                    } while (cursor.moveToNext());
                    db.close();
                }

            } catch (Exception e) {
                Log.d("HistoryItems", e.getMessage());
                db.close();
            }
        }


        if(cursor!=null)
        cursor.close();
        return historyItems;
    }

    public double gettotalPrice(String tablename) { // Function to show total price in cards in Shopping History
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        double price = 0;
        try {
            String query = "SELECT " + COLUMN_PRICE + " FROM " + tablename;
            cursor = db.rawQuery(query, null);

            Log.d("Total Price", "Query Successful");
            if (cursor.moveToFirst()) {

                do {

                    price += cursor.getDouble(0);

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {

            Log.d("TotalPrice", " Error in query");
        }
        db.close();
        return price;
    }

    public boolean anyNullPrice(String tablename) {
        //Avoiding adding a shopping list to history in case any item price is not added
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        try {
            String query = "SELECT " + COLUMN_PRICE + " FROM " + "[" + tablename + "]";
            cursor = db.rawQuery(query, null);

            Log.d("Total Price", "Query Successful");
            if (cursor.moveToFirst()) {

                do {

                    if (cursor.getDouble(0) == 0.0)
                        return true;

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.d("Null Price", e.getMessage());
        }

        db.close();
        return false;
    }
}
