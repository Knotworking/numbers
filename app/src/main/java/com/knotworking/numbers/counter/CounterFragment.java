package com.knotworking.numbers.counter;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.knotworking.numbers.R;
import com.knotworking.numbers.database.CounterContract;

import java.util.List;

public class CounterFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<CounterItem>> {
    private static final int COUNTER_LOADER = 1;

    private RecyclerView recyclerView;
    private CounterAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_counter, container, false);
        recyclerView = (RecyclerView)root.findViewById(R.id.fragment_counter_recyclerView);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CounterAdapter();
//        adapter.setData(getDummyData());
        recyclerView.setAdapter(adapter);

        getLoaderManager().initLoader(COUNTER_LOADER, null, CounterFragment.this);
    }

//    private List<CounterItem> getDummyData() {
//        List<CounterItem> data = new ArrayList<>();
//
//        data.add(new CounterItem("Snakes", 500));
//        data.add(new CounterItem("Bottles of beer on the wall", 99));
//        data.add(new CounterItem("High fives", 0));
//        return data;
//    }

    @Override
    public Loader<List<CounterItem>> onCreateLoader(int id, Bundle args) {
        Uri uri = CounterContract.Counters.CONTENT_URI;
        String[] projection = new String[]{
                CounterContract.Counters.NAME,
                CounterContract.Counters.COUNT};
        return new CounterListLoader(getActivity(), uri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<List<CounterItem>> loader, List<CounterItem> data) {
        if (data == null) {
            return;
        }

        adapter.setData(data);

    }

    @Override
    public void onLoaderReset(Loader<List<CounterItem>> loader) {

    }
}
