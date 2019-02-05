package com.sashaermolenko.fastorder.Items;

public class DishItem extends Item {
    private String price;
    private String comment;
    private int amount = 1;
    private String description;
    private boolean showFullName = false;

    public DishItem(String title, String imgURL, int id, String price, String description) {
        super(title, imgURL, id);
        this.price = price;
        this.description = description;
    }

    @Override
    public String getName() {
        return showFullName == true ? super.getName() : super.getName().substring(0, Math.min(9, super.getName().length())) + (super.getName().length() > 9 ? "..." : "");
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
