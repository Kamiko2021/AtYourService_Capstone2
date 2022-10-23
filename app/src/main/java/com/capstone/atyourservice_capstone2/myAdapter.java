package com.capstone.atyourservice_capstone2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
        holder.uid.setText(user.getUid());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "position"+ position, Toast.LENGTH_LONG).show();
                Intent intent= new Intent(context, hiring_details.class);
                intent.putExtra("firstname", list.get(position).getFirstname());
                intent.putExtra("lastname", list.get(position).getLastname());
                intent.putExtra("status", list.get(position).getStatus());
                intent.putExtra("latitude", list.get(position).getLatitude());
                intent.putExtra("longhitude", list.get(position).getLonghitude());
                intent.putExtra("uid", list.get(position).getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView firstname,lastname,status,longhitude,latitude,uid;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            firstname = itemView.findViewById(R.id.Firstname_Ctv);
            lastname = itemView.findViewById(R.id.Lastname_Ctv);
            status = itemView.findViewById(R.id.status_Ctv);
            longhitude = itemView.findViewById(R.id.longhitude_Ctv);
            latitude = itemView.findViewById(R.id.latitude_Ctv);
            uid = itemView.findViewById(R.id.uid_Ctv);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }

}
