package com.capstone.atyourservice_capstone2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class plumberNotification extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference database;
    notifadapter notifAdapter;
    ArrayList<notifdata> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_plumber_notification, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.notifList);
        FirebaseUser user1= FirebaseAuth.getInstance().getCurrentUser();
        String uid=user1.getUid();
        database = FirebaseDatabase.getInstance().getReference("notification_plumber").child(uid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        notifAdapter = new notifadapter(getActivity(),list);
        recyclerView.setAdapter(notifAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    notifdata notif=dataSnapshot.getValue(notifdata.class);
                    list.add(notif);
                }
            notifAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}