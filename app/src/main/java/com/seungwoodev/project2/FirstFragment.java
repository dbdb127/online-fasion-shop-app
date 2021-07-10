package com.seungwoodev.project2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<String> titles;
    private List<Integer> prices;
    private List<Integer> mImages;
    private FirstAdapter adapter;

    public FirstFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recyclerview);

        titles = new ArrayList<>();
        prices = new ArrayList<Integer>();
        mImages = new ArrayList<Integer>();

        adapter = new FirstAdapter(getActivity().getApplicationContext(), titles, prices, mImages);

        //product list
        mImages.add(R.drawable.ic_baseline_checkroom_24);
        titles.add("temp");
        prices.add(12345);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);

        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

    }
}