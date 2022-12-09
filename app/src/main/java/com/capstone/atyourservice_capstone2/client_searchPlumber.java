package com.capstone.atyourservice_capstone2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class client_searchPlumber extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference database;
    myAdapter adapter;
    ArrayList<userCardviewData> list;
    DatabaseReference reff;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_search_plumber, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.plumberlist_recyclerview);
        database = FirebaseDatabase.getInstance().getReference("waiting");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        list = new ArrayList<>();
        adapter = new myAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        userCardviewData user = dataSnapshot.getValue(userCardviewData.class);
                        String uid = dataSnapshot.child("uid").getValue().toString();
//                        checkCertified(uid,list,user);
                        list.add(user);

                    }
                    adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    //==== checking if the user is certified / uncertified

    private void checkCertified(String uid, List list2,Object object){
        reff = FirebaseDatabase.getInstance().getReference("admin").child("plumber_list").child(uid);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String act=snapshot.child("actions").getValue().toString();

                if(act.equals("pending")){
                   list2.clear();
                   list2.add(object);
                } else if (act.equals("uncertified")){
                    list2.clear();
                    list2.add(object);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}