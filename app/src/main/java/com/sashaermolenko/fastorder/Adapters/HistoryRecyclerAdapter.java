package com.sashaermolenko.fastorder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sashaermolenko.fastorder.DishActivity;
import com.sashaermolenko.fastorder.Items.HistoryItem;
import com.sashaermolenko.fastorder.Items.MenuItem;
import com.sashaermolenko.fastorder.MainActivity;
import com.sashaermolenko.fastorder.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.RecyclerViewHolder> {

    private Context context;
    private ArrayList<HistoryItem> items = new ArrayList<>();

    public HistoryRecyclerAdapter(Context context) {
        this.context = context;
        try {
            if(!MainActivity.historyItems.get(0).getDate().equals("asd"))
                this.items = MainActivity.historyItems;
        }catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

        for(HistoryItem x : items){
            if(x.getItemsCount() == 0){
                MainActivity.historyItems.remove(x);
                items.remove(x);
                notifyDataSetChanged();
                break;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.historyitem, parent, false);
        return new HistoryRecyclerAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {
        final HistoryItem historyItem = items.get(position);

        holder.bind(historyItem, position);

        holder.itemView.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.cartItems != null && MainActivity.historyItems != null && MainActivity.historyItems.get(position).getItems() != null)
                    MainActivity.cartItems.addAll(MainActivity.historyItems.get(position).getItems());
                else
                    Toast.makeText(context, "Это тестовый образец", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        private TextView price, date;
        private RecyclerView recView;
        private LinearLayoutManager manager;
        private HistoryListRecyclerAdapter adapter;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            price = (TextView) itemView.findViewById(R.id.price_hist);
            date = (TextView) itemView.findViewById(R.id.date);
            recView = (RecyclerView) itemView.findViewById(R.id.historuListRecView);

            price.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf"));
            date.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf"));
        }

        public void bind(final HistoryItem recyclerItem, int position) {

            date.setText(recyclerItem.getDate());
            price.setText(recyclerItem.getPrice());

            manager = new LinearLayoutManager(context);
            adapter = new HistoryListRecyclerAdapter(context, position);

            recView.setLayoutManager(manager);
            recView.setAdapter(adapter);
        }


    }
}
