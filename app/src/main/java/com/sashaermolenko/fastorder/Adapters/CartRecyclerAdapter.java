package com.sashaermolenko.fastorder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sashaermolenko.fastorder.Fragments.CartFragment;
import com.sashaermolenko.fastorder.Items.CartItem;
import com.sashaermolenko.fastorder.Items.DishItem;
import com.sashaermolenko.fastorder.MainActivity;
import com.sashaermolenko.fastorder.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.RecyclerViewHolder>{

    private static int totalPrice = 0;
    private Context context;
    private ArrayList<CartItem> items = new ArrayList<>();

    public CartRecyclerAdapter(Context context) {
        this.context = context;
        items = MainActivity.cartItems;
        totalPrice = 0;
        for(int i = 0; i < items.size(); ++i){
            totalPrice += items.get(i).getAmount() * Double.valueOf(items.get(i).getPrice());
        }
    }

    public static String getTotalPrice() {
        return Integer.toString(totalPrice);
    }

    public void addAll(List<CartItem> items) {
        int pos = getItemCount();
        this.items.addAll(items);
        notifyItemRangeInserted(pos, this.items.size());
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cartitem, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        final CartItem cartItem = items.get(position);

        holder.bind(cartItem);

        holder.itemView.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean expanded = cartItem.getExpandable();
                cartItem.setComment(holder.comment.getText().toString());
                cartItem.setExpanded(!expanded);
                notifyItemChanged(position);
                cartItem.setVisOfFullName(!expanded);
            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartItem.setAmount(1);
                Integer amount = Integer.valueOf(holder.amount.getText().toString()) + 1;
                Double pr = Double.valueOf(cartItem.getPrice()) * amount;
                holder.total_price.setText(Double.toString(pr));
                holder.amount.setText(Integer.toString(amount));
                totalPrice += Double.valueOf(cartItem.getPrice());
                //notifyItemChanged(position);
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartItem.getAmount() == 1) {
                    items.remove(position);
                    notifyDataSetChanged();
                } else {
                    cartItem.setAmount(-1);
                    Integer amount = Math.max(1, Integer.valueOf(holder.amount.getText().toString()) - 1);
                    Double pr = Double.valueOf(cartItem.getPrice()) * amount;
                    holder.total_price.setText(Double.toString(pr));
                    holder.amount.setText(Integer.toString(amount));
                }
                totalPrice -= Double.valueOf(cartItem.getPrice());
                //notifyItemChanged(position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalPrice -= Double.valueOf(cartItem.getPrice())*cartItem.getAmount();
                items.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView title, price, description, total_price, total, amount;
        private Button plus, minus, delete;
        private ImageView image;
        private View subItem;
        private EditText comment;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            subItem = itemView.findViewById(R.id.sub_item_cart);

            amount = (TextView) itemView.findViewById(R.id.dish_amount_cart);
            delete = (Button) itemView.findViewById(R.id.delete_cart);
            comment = (EditText) itemView.findViewById(R.id.comment_cart);
            plus = (Button) itemView.findViewById(R.id.btn_plus_cart);
            minus = (Button) itemView.findViewById(R.id.btn_minus_cart);
            title = (TextView) itemView.findViewById(R.id.title_cart);
            total = (TextView) itemView.findViewById(R.id.total_cart);
            total_price = (TextView) itemView.findViewById(R.id.total_price_cart);
            price = (TextView) itemView.findViewById(R.id.price_cart);
            description = (TextView) itemView.findViewById(R.id.description_cart);
            title.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf"));
            total.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf"));
            total_price.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf"));
            price.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf"));
            description.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf"));

            image = (ImageView) itemView.findViewById(R.id.imgD_cart);

            //image.setImageResource(R.drawable.no_img); //
        }

        public void bind(final CartItem recyclerItem) {
            boolean expanded = recyclerItem.getExpandable();

            comment.setText(recyclerItem.getComment());
            amount.setText(Integer.toString(recyclerItem.getAmount()));
            title.setText(recyclerItem.getName());
            description.setText(recyclerItem.getDescription());
            // TODO добавить текст button.setText();
            Picasso.with(context).load(recyclerItem.getURL()).into(image);

            subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);
            price.setVisibility(expanded ? View.INVISIBLE : View.VISIBLE);

            Integer am = Integer.valueOf(amount.getText().toString());
            Double pr = Double.valueOf(recyclerItem.getPrice()) * am;
            total_price.setText(Double.toString(pr));
            price.setText(Double.toString(pr) + '₴');
        }
    }
}
