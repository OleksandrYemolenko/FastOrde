package com.sashaermolenko.fastorder.Items;

public class CartItem extends Item {
    private String comment;
    private int amount = 1;
    private String price;
    private String description;
    private boolean showFullName = false;

    public CartItem(String title, String imgURL, int id, String price, String description, String comment, int amount) {
        super(title, imgURL, id);
        this.price = price;
        this.description = description;
        this.comment = comment;
        this.amount = amount;
    }

    public CartItem(String title, String imgURL, int id, String price, String description) {
        super(title, imgURL, id);
        this.price = price;
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public String getComment() {
        return comment;
    }

    public void setAmount(int eps) {
        amount += eps;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String getName() {
        return showFullName == true ? super.getName() : super.getName().substring(0, Math.min(9, super.getName().length())) + (super.getName().length() > 9 ? "..." : "");
    }

    public void setVisOfFullName(boolean showFullName) {
        this.showFullName = showFullName;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public boolean getVisOfFullName() {
        return showFullName;
    }
}
