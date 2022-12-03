package com.capstone.atyourservice_capstone2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.MyViewHolder>{

    Context context;
    ArrayList<chatData> list;
    StorageReference displayStorageRef;
    DatabaseReference reff;

    public chatAdapter(Context context, ArrayList<chatData> list) {
        this.context=context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.chat_box,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        chatData chat=list.get(position);
        holder.message_data.setText(chat.getMsgs());
        fetchprofilepicAndDisplay(list.get(position).getSender_uid(), holder.senderProfile);
        holder.sender_uid.setText(chat.getSender_uid());
        holder.date.setText(chat.getDateNow());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView message_data,date,sender_uid;
        CircleImageView senderProfile;
        CardView chatbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message_data = itemView.findViewById(R.id.message);
            sender_uid = itemView.findViewById(R.id.uid_sender);
            date=itemView.findViewById(R.id.date_now);
            senderProfile=itemView.findViewById(R.id.chatprofilepix);

        }
    }

    //=======================displays profile picture from database...
    private void fetchprofilepicAndDisplay(String uid, CircleImageView profpix){
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


                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                            profpix.setImageBitmap(bitmap);

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
