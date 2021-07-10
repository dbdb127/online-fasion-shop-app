package com.seungwoodev.project2;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<String> titles;
    private List<Integer> prices;
    private List<Integer> qty;
    private List<Integer> mImages;
    private SecondAdapter adapter;

    public SecondFragment() {
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
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recyclerview);

        titles = new ArrayList<>();
        prices = new ArrayList<Integer>();
        qty = new ArrayList<Integer>();
        mImages = new ArrayList<>();

        adapter = new SecondAdapter(getActivity().getApplicationContext(), titles, prices, qty, mImages);

        //product list
        mImages.add(R.drawable.ic_baseline_checkroom_24);
        titles.add("temp");
        qty.add(1);
        prices.add(12345);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);

        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

    }
}