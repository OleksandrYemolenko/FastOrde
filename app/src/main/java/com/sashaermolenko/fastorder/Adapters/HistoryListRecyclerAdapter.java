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

import com.sashaermolenko.fastorder.Items.CartItem;
import com.sashaermolenko.fastorder.MainActivity;
import com.sashaermolenko.fastorder.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryListRecyclerAdapter extends RecyclerView.Adapter<HistoryListRecyclerAdapter.RecyclerViewHolder>{

    private Context context;
    private ArrayList<CartItem> items = new ArrayList<>();

    public HistoryListRecyclerAdapter(Context context, int position) {
        this.context = context;

        items = MainActivity.historyItems.get(position).getItems();
    }

    public void addAll(List<CartItem> items) {
        int pos = getItemCount();
        this.items.addAll(items);
        notifyItemRangeInserted(pos, this.items.size());
    }

    @NonNull
    @Override
    public HistoryListRecyclerAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new HistoryListRecyclerAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryListRecyclerAdapter.RecyclerViewHolder holder, final int position) {
        final CartItem item = items.get(position);

        holder.bind(item);

        holder.itemView.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "50", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(items != null)
            return items.size();
        else
            return 0;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView title, amount;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            amount = (TextView) itemView.findViewById(R.id.amount);

            title.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf"));
            amount.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf"));
        }

        public void bind(final CartItem recyclerItem) {

            title.setText(recyclerItem.getName());

            amount.setText(Integer.toString(recyclerItem.getAmount()));
        }
    }
}
