package com.capstone.atyourservice_capstone2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder> {

    Context context;
    ArrayList<userCardviewData> list;

    public myAdapter(Context context, ArrayList<userCardviewData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.card_view,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        userCardviewData user=list.get(position);
        holder.firstname.setText(user.getFirstname());
        holder.lastname.setText(user.getLastname());
        holder.status.setText(user.getStatus());
        holder.longhitude.setText(user.getLonghitude());
        holder.latitude.setText(user.getLatitude());
        holder.distance.setText(user.getDistance());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView firstname,lastname,status,longhitude,latitude,distance;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            firstname = itemView.findViewById(R.id.Firstname_Ctv);
            lastname = itemView.findViewById(R.id.Lastname_Ctv);
            status = itemView.findViewById(R.id.status_Ctv);
            longhitude = itemView.findViewById(R.id.longhitude_Ctv);
            latitude = itemView.findViewById(R.id.latitude_Ctv);
            distance = itemView.findViewById(R.id.distance_Ctv);


        }
    }

}
