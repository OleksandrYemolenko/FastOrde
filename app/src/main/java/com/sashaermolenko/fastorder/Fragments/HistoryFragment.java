package com.sashaermolenko.fastorder.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sashaermolenko.fastorder.Adapters.HistoryRecyclerAdapter;
import com.sashaermolenko.fastorder.Adapters.MenuRecyclerAdapter;
import com.sashaermolenko.fastorder.R;

public class HistoryFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private HistoryRecyclerAdapter adapter;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.historyRecView);

        manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);

        adapter = new HistoryRecyclerAdapter(getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
}
