package com.sashaermolenko.fastorder.Items;

import java.util.ArrayList;

public class HistoryItem extends Item {

    private String price;
    private String date;
    private ArrayList<CartItem> items = new ArrayList<>();

    public HistoryItem(String date, String price, ArrayList<CartItem> items) {
        super();

        this.date = date;
        this.price = price;
        this.items = items;
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<CartItem> items) {
        this.items = items;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    @Override
    public  String toString(){
        return date + " " + price;
    }
}
