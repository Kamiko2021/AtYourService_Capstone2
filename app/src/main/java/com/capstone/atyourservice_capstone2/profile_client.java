package com.capstone.atyourservice_capstone2;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class profile_client extends Fragment {

    TextView firstname_txt,lastname_txt,gender_txt,birthdate_txt;
    Button updatebtn_popUp,closebtn_popUp;
    EditText firstname_edtxt,lastname_edtxt,birthdate_edtxt;
    Spinner gender_edtxt;
    TextView usertype_txt,uid_txt;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    CircleImageView profileImageView;
    Button update_btn,uploadbtn,choose_btn;
    Uri mImageUri;
    String emailAddress,uid;
    DatabaseReference reff,mreff;
    StorageReference mStorageRef,displaystorage;
    StorageTask mUploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_client, container, false);

        //======initialize loading dialog======
        final LoadingDialog dialog=new LoadingDialog(getActivity());
        Handler handler=new Handler(Looper.getMainLooper());

        //===========EditTexts===============
        firstname_txt=(TextView) view.findViewById(R.id.firstname_clientData);
        lastname_txt=(TextView) view.findViewById(R.id.lastname_clientData);
        gender_txt=(TextView) view.findViewById(R.id.gender_clientData);
        birthdate_txt=(TextView) view.findViewById(R.id.birthdate_clientData);

        //=============TextViews===========
        usertype_txt=(TextView) view.findViewById(R.id.usertype_Data_client);
        uid_txt=(TextView) view.findViewById(R.id.uid_lbl_client);


        //===========Buttons===========
        update_btn=(Button) view.findViewById(R.id.update_clientbtn);
        uploadbtn=(Button)view.findViewById(R.id.btnSave);
        choose_btn = (Button) view.findViewById(R.id.btnChoose);

        //=======CircleImageView==========
        profileImageView = (CircleImageView) view.findViewById(R.id.profile_image_client);

        //=======Firebase Declarations=====
        mStorageRef= FirebaseStorage.getInstance().getReference("uploads");
        mreff = FirebaseDatabase.getInstance().getReference("uploads");

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateData();

                    }
                }, 1000);

            }
        });

        choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(getActivity(), "upload in progress", Toast.LENGTH_SHORT).show();
                }else {
                    uploadFile();
                }
            }
        });
        fetchData();
        dialog.startLoadingDialog();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchprofilepicAndDisplay();
                dialog.dismissDialog();
            }
        },1000);

        return view;
    }

    //=====methods in choosing picture to upload=======
    private void choosePicture(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == -1 && data!=null && data.getData() != null){
            mImageUri = data.getData();
            profileImageView.setImageURI(mImageUri);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr=getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile(){

        ProgressDialog pd=new ProgressDialog(getActivity());
        pd.setTitle("Uploading Image.......");
        pd.show();

        if (mImageUri != null){
            Random random=new Random();
            int randomNumber=random.nextInt(1000-100)+ 100;

            StorageReference fileReference=mStorageRef.child("profilepix"+ randomNumber + "" + "." + getFileExtension(mImageUri));
            mreff.child(uid_txt.getText().toString().trim()).child("ProfilePicture").setValue("profilepix"+ randomNumber +"."+ getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Image Upload failed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot tasksnapshot) {
                            double progressPercent = (100.00 * tasksnapshot.getBytesTransferred() / tasksnapshot.getTotalByteCount());
                            pd.setMessage("Percentage: "+ (int) progressPercent + "%");
                        }
                    });
        }else {
            Toast.makeText(getActivity(), "No File Selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void fetchprofilepicAndDisplay(){
        reff = FirebaseDatabase.getInstance().getReference().child("uploads").child(uid);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profile = snapshot.child("ProfilePicture").getValue().toString();
                displaystorage=FirebaseStorage.getInstance().getReference().child("uploads/"+ profile);

                try {
                    final File localfile=File.createTempFile(profile, "jpg");
                    displaystorage.getFile(localfile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap=BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                    profileImageView.setImageBitmap(bitmap);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void fetchData(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            String uid_data=user.getUid();
            String email=user.getEmail();
            emailAddress=email;
            uid=uid_data;
            reff= FirebaseDatabase.getInstance().getReference().child("USERS").child(uid);
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //===========fetching data from database======
                    String firstname = snapshot.child("firstname").getValue().toString();
                    String lastname = snapshot.child("lastname").getValue().toString();
                    String gender = snapshot.child("gender").getValue().toString();
                    String usertype = snapshot.child("userType").getValue().toString();
                    String birthdate = snapshot.child("birthdate").getValue().toString();
                    //=========displaying data into editexts===========
                    firstname_txt.setText(firstname);
                    lastname_txt.setText(lastname);
                    gender_txt.setText(gender);
                    birthdate_txt.setText(birthdate);
                    usertype_txt.setText(usertype);
                    uid_txt.setText(uid_data);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    public void updateData(){
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View editprofilePopupView= getLayoutInflater().inflate(R.layout.updateprofile_plumber_popop, null);

        //========== Initialize EditText======
        firstname_edtxt = (EditText) editprofilePopupView.findViewById(R.id.firstname_editxt);
        lastname_edtxt = (EditText) editprofilePopupView.findViewById(R.id.lastname_editxt);
        birthdate_edtxt = (EditText) editprofilePopupView.findViewById(R.id.birthdate_editxt);
        gender_edtxt = (Spinner) editprofilePopupView.findViewById(R.id.gender_spinner);
        ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Gender));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_edtxt.setAdapter(myAdapter);

        //========= Initialize Button=====
        updatebtn_popUp = (Button) editprofilePopupView.findViewById(R.id.update_clientbtn);
        closebtn_popUp = (Button) editprofilePopupView.findViewById(R.id.close_btn);

        //===== setting up view=====
        dialogBuilder.setView(editprofilePopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        closebtn_popUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        updatebtn_popUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //===================
                String fname=firstname_edtxt.getText().toString().trim();
                String lname=lastname_edtxt.getText().toString().trim();
                String gender_txt=gender_edtxt.getSelectedItem().toString().trim();
                String birthdate_txt=birthdate_edtxt.getText().toString().trim();
                String userType=usertype_txt.getText().toString().trim();

                if (fname.isEmpty()){
                    firstname_edtxt.setError("Firstname is Required");
                    firstname_edtxt.requestFocus();
                    return;
                }
                if (lname.isEmpty()){
                    lastname_edtxt.setError("Lastname is Required");
                    lastname_edtxt.requestFocus();
                    return;
                }
//                if (gender_txt.isEmpty()){
//                    gender_edtxt.setError("Gender is required");
//                    gender_edtxt.requestFocus();
//                    return;
//                }
                if (birthdate_txt.isEmpty()){
                    birthdate_edtxt.setError("Birthdate is required");
                    birthdate_edtxt.requestFocus();
                    return;
                }

                updateClient clientUpdate= new updateClient(fname,lname,gender_txt,birthdate_txt,userType,emailAddress);
                FirebaseDatabase.getInstance().getReference("USERS").child(uid_txt.getText().toString())
                        .setValue(clientUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
//                                    Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getActivity(), "Update Failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }
}