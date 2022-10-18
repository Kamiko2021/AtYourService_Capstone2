package com.capstone.atyourservice_capstone2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ModelReclerView extends RecyclerView.Adapter<ModelReclerView.ViewHolder> {

    Context context;
    ArrayList<Model> arrayList = new ArrayList<>();

    public ModelReclerView(Context context, ArrayList<Model> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.card_view,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(arrayList.get(position).getImage());
        holder.textView1.setText(arrayList.get(position).getFirstname());
        holder.textView2.setText(arrayList.get(position).getLastname());
        holder.textView3.setText(arrayList.get(position).getStatus());
        holder.textView4.setText(arrayList.get(position).getLatitude());
        holder.textView5.setText(arrayList.get(position).getLonghitude());
        holder.textView6.setText(arrayList.get(position).getDistance());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "test" + position,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView1,textView2,textView3,textView4,textView5,textView6;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profileImg);
            textView1 = itemView.findViewById(R.id.Firstname);
            textView2 = itemView.findViewById(R.id.Lastname);
            textView3 = itemView.findViewById(R.id.status);
            textView4 = itemView.findViewById(R.id.latitude);
            textView5 = itemView.findViewById(R.id.longhitude);
            textView6 = itemView.findViewById(R.id.distance);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
