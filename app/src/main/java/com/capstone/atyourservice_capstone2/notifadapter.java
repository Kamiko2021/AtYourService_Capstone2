package com.capstone.atyourservice_capstone2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class notifadapter extends RecyclerView.Adapter<notifadapter.MyViewHolder> {

    Context context;
    ArrayList<notifdata> list;

    public notifadapter(Context context, ArrayList<notifdata> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.notifcardview,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        notifdata notif= list.get(position);
        holder.firstname.setText(notif.getFirstname());
        holder.lastname.setText(notif.getLastname());
        holder.serviceRequest.setText(notif.getServiceRequest());
        holder.uid_client.setText(notif.getClient_uid());
        holder.dateNow.setText(notif.getDateNow());
        holder.distance.setText(notif.getDistance());
        holder.address.setText(notif.getAddress());
        holder.requesStatus.setText(notif.getRequestStatus());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, transaction_plumber.class);
                intent.putExtra("firstname", list.get(position).getFirstname());
                intent.putExtra("lastname", list.get(position).getLastname());
                intent.putExtra("client_uid", list.get(position).getClient_uid());
                intent.putExtra("dateNow", list.get(position).getDateNow());
                intent.putExtra("distance", list.get(position).getDistance());
                intent.putExtra("address", list.get(position).getAddress());
                intent.putExtra("serviceRequest", list.get(position).getServiceRequest());
                intent.putExtra("requestStatus", list.get(position).getRequestStatus());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView firstname,lastname,uid_client,dateNow,distance,address,serviceRequest,requesStatus;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            firstname = itemView.findViewById(R.id.firstname);
            lastname = itemView.findViewById(R.id.lastname);
            uid_client = itemView.findViewById(R.id.client_uniqueID);
            dateNow=itemView.findViewById(R.id.dateNow);
            distance = itemView.findViewById(R.id.distance);
            address = itemView.findViewById(R.id.location_address);
            serviceRequest = itemView.findViewById(R.id.serviceRequest);
            requesStatus = itemView.findViewById(R.id.requestStatusTxt);
            cardView=itemView.findViewById(R.id.notifcardview);
        }
    }
}
