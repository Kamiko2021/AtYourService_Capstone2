package com.capstone.atyourservice_capstone2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class client_searchPlumber extends Fragment {

    private RecyclerView recyclerView;
    ArrayList<Model> arrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_search_plumber, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        arrayList.add(new Model(R.drawable.defaultprofile, "Karl Michael", "Lopez", "online", "10.000", "12.000", "10km"));
        ModelReclerView modelReclerView = new ModelReclerView(getActivity(), arrayList);
        recyclerView.setAdapter(modelReclerView);


        return view;
    }
}