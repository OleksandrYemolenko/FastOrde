package com.sashaermolenko.fastorder.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sashaermolenko.fastorder.Adapters.CartRecyclerAdapter;
import com.sashaermolenko.fastorder.OrderPayActivity;
import com.sashaermolenko.fastorder.R;


public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private CartRecyclerAdapter adapter;
    private Context context;
    private View view;
    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_cart, container, false);

        context = container.getContext();

        recyclerView = (RecyclerView) view.findViewById(R.id.cartRecView);

        manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);

        adapter = new CartRecyclerAdapter(getContext());
        recyclerView.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, OrderPayActivity.class);
                context.startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
