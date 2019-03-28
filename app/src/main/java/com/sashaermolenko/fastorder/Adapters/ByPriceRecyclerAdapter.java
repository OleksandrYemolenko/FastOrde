package com.sashaermolenko.fastorder.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sashaermolenko.fastorder.Items.HistoryItem;
import com.sashaermolenko.fastorder.MainActivity;
import com.sashaermolenko.fastorder.R;

import java.util.ArrayList;

public class ByPriceRecyclerAdapter extends RecyclerView.Adapter<ByPriceRecyclerAdapter.RecyclerViewHolder> {

    private Context context;
    private ArrayList<HistoryItem> items = new ArrayList<>();

    public ByPriceRecyclerAdapter(Context context) {
        this.context = context;
        try {
            if(MainActivity.historyItems.get(0).getDate() != "asd")
                this.items = MainActivity.historyItems;
        }catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public ByPriceRecyclerAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.historyitem, parent, false);
        return new ByPriceRecyclerAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ByPriceRecyclerAdapter.RecyclerViewHolder holder, final int position) {
        final HistoryItem historyItem = items.get(position);

        holder.bind(historyItem);

        holder.itemView.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(MainActivity.cartItems == null)
//                    Toast.makeText(context, "Cart", Toast.LENGTH_SHORT).show();
//
//                if(MainActivity.allHistory == null)

                MainActivity.cartItems = MainActivity.historyItems.get(position).getItems();

//                Toast.makeText(context, Integer.toString(MainActivity.cartItems.size()) + " " + Integer.toString(MainActivity.allHistory.get(position).size()), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        private TextView price, date;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            price = (TextView) itemView.findViewById(R.id.price_hist);
            date = (TextView) itemView.findViewById(R.id.date);

            price.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf"));
            date.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf"));
        }

        public void bind(final HistoryItem recyclerItem) {

            date.setText(recyclerItem.getDate());
            price.setText(recyclerItem.getPrice());
        }


    }

}
