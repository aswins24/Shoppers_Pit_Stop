package com.example.ashwin.shopperspitstop;

/**
 * Created by ashwin on 5/26/2019.
 */
public class HistoryItems {

    private int id;
    private String name;
    private int link_id;
    private String date;
    private String time;
    private String header;
    private int type =1;
    private double price;

    public HistoryItems() {

    }

    public void set_table_name(String tableName) {

        this.name = tableName;

    }

    public void set_date(String date) {

        this.date = date;

    }

    public void set_time(String time) {

        this.time = time;

    }

    public void set_header(String header){
        this.header = header;
        this.type = 0;

    }

    public void set_header_no_type(String header){
        this.header = header;
    }

    public void set_price(double price){
        this.price = price;
    }

    public double get_price(){
        return this.price;
    }
    public String get_header(){
        return this.header;
    }

    public int get_type(){
        return this.type;
    }

    public int get_id() {
        return this.id;
    }

    public void set_id(int id) {

        this.id = id;

    }

    public String get_Name() {
        return this.name;
    }

    public int get_link_id() {
        return this.link_id;
    }

    public void set_link_id(int link_id) {

        this.link_id = link_id;

    }

    public String getTime() {
        return this.time;
    }

    public String getDate() {
        return this.date;
    }
}
