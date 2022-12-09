package com.capstone.atyourservice_capstone2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder> {

    Context context;
    ArrayList<userCardviewData> list;
    StorageReference displayStorageRef;
    DatabaseReference reff;

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
//        holder.setIsRecyclable(false);
        userCardviewData user=list.get(position);
        holder.firstname.setText(user.getFirstname());
        holder.lastname.setText(user.getLastname());
        holder.status.setText(user.getStatus());
        if (user.getStatus().equals("offline")){
            holder.status.setTextColor(Color.parseColor("#ff0000"));
        }else if (user.getStatus().equals("online")){
            holder.status.setTextColor(Color.parseColor("#09ff00"));
        }
        checkCertified(user.getUid(),holder.cardView,holder.action);
        holder.longhitude.setText(user.getLonghitude());
        holder.latitude.setText(user.getLatitude());
        holder.uid.setText(user.getUid());
        fetchprofilepicAndDisplay(user.getUid(), "Plumber", holder.profilePix);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        TextView firstname,lastname,status,longhitude,latitude,uid,action;
        CircleImageView profilePix;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            profilePix = itemView.findViewById(R.id.searchprofImg_plumber);
            firstname = itemView.findViewById(R.id.Firstname_Ctv);
            lastname = itemView.findViewById(R.id.Lastname_Ctv);
            status = itemView.findViewById(R.id.status_Ctv);
            longhitude = itemView.findViewById(R.id.longhitude_Ctv);
            latitude = itemView.findViewById(R.id.latitude_Ctv);
            uid = itemView.findViewById(R.id.uid_Ctv);
            action = itemView.findViewById(R.id.action_Ctv);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }
    //==== checking if the user is certified / uncertified

    private void checkCertified(String uid, CardView card,TextView action){
        reff = FirebaseDatabase.getInstance().getReference("admin").child("plumber_list").child(uid);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String act=snapshot.child("actions").getValue().toString();

                if(act.equals("pending")){
                    card.setEnabled(false);
                    action.setText(act);
                    action.setTextColor(Color.parseColor("#ff0000"));
                }else if (act.equals("uncertified")){
                    card.setEnabled(false);
                    action.setText(act);
                    action.setTextColor(Color.parseColor("#ff0000"));
                }else if (act.equals("certified")){
                    action.setText(act);
                    action.setTextColor(Color.parseColor("#09ff00"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //=======================displays profile picture from database...
    private void fetchprofilepicAndDisplay(String uid, String userType, CircleImageView profpix){
        try {
            reff = FirebaseDatabase.getInstance().getReference().child("uploads").child(uid);
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String profile = snapshot.child("ProfilePicture").getValue().toString();

                    displayStorageRef = FirebaseStorage.getInstance().getReference().child("uploads/"+ profile);

                    try {
                        final File localFile = File.createTempFile(profile, "jpg");
                        displayStorageRef.getFile(localFile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                                        if (userType.equals("Plumber")){
                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                            profpix.setImageBitmap(bitmap);
                                        } else if (userType.equals("Client")){
                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                            profpix.setImageBitmap(bitmap);
                                        }
//                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                                        profileImageView.setImageBitmap(bitmap);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
//            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
        }

    }

}
