package com.sashaermolenko.fastorder.Items;

public class HistoryItem extends Item {

    private String price;
    private String date;

    public HistoryItem(String date, String price) {
        super();

        this.date = date;
        this.price = price;
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
