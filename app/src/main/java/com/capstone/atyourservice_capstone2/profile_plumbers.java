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

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class profile_plumbers extends Fragment {

    public EditText email_txt, firstname_txt,lastname_txt,birthdate_txt,gender_txt;
    public TextView usertype_txt, uid_data;
    public DatabaseReference reff;
    public FirebaseDatabase firebaseDatabase;

    private CircleImageView profileImageView;
    private Button backButton,saveButton,updateBtn,subscibeBtn;
    private TextView profileChangeBtn;

    private Uri mImageUri;
    public StorageReference mStorageRef;
    public StorageReference certStore;
    private StorageReference displayStorageRef;
    private StorageTask mUploadTask;
    private StorageTask certUploadTask;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference certdb;

    //============= Pop-up initialization=========
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    Button openGallery,uploadBtn;
    TextView SubsStat;
    WebView paypalPayment;
    ImageView certCap;
    String emailAddress;
    Button updatebtn_popUp,closebtn_popUp;
    EditText firstname_edtxt,lastname_edtxt,gender_edtxt,birthdate_edtxt;


    public String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_plumbers, container, false);

        //----------declaration of editexts---------------

        firstname_txt = (EditText) view.findViewById(R.id.firstname_plumberData);
        lastname_txt = (EditText) view.findViewById(R.id.lastname_plumberData);
        birthdate_txt = (EditText) view.findViewById(R.id.birthdate_plumberData);
        gender_txt = (EditText) view.findViewById(R.id.gender_plumberData);
        //-----------declaration of textview------------
        uid_data = (TextView)view.findViewById(R.id.uid_lbl);
        usertype_txt=(TextView) view.findViewById(R.id.usertype_Data);
        profileImageView=(CircleImageView) view.findViewById(R.id.profile_image);
        profileChangeBtn = (TextView) view.findViewById(R.id.change_profile_btn);
        saveButton = (Button) view.findViewById(R.id.btnSave);
        updateBtn = (Button) view.findViewById(R.id.update_plumberbtn);
        subscibeBtn= (Button) view.findViewById(R.id.upload_cv);
        //------declaration of firebase storage----
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        certStore = FirebaseStorage.getInstance().getReference("certificate");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        certdb = FirebaseDatabase.getInstance().getReference("certificate");

        profileChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(getActivity(), "upload in progress", Toast.LENGTH_SHORT).show();
                }else {
                    uploadFile();
                }
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        subscibeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                certificateAndSubscription();
            }
        });



        fetchData();
        fetchprofilepicAndDisplay();


        return view;
    }
    //for update data
    public void updateData(){
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View editprofilePopupView= getLayoutInflater().inflate(R.layout.updateprofile_plumber_popop, null);

        //========== Initialize EditText======
        firstname_edtxt = (EditText) editprofilePopupView.findViewById(R.id.firstname_editxt);
        lastname_edtxt = (EditText) editprofilePopupView.findViewById(R.id.lastname_editxt);
        birthdate_edtxt = (EditText) editprofilePopupView.findViewById(R.id.birthdate_editxt);
        gender_edtxt = (EditText) editprofilePopupView.findViewById(R.id.gender_editxt);

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
                String gender_txt=gender_edtxt.getText().toString().trim();
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
                if (gender_txt.isEmpty()){
                    gender_edtxt.setError("Gender is required");
                    gender_edtxt.requestFocus();
                    return;
                }
                if (birthdate_txt.isEmpty()){
                    birthdate_edtxt.setError("Birthdate is required");
                    birthdate_edtxt.requestFocus();
                    return;
                }

                updateClient clientUpdate= new updateClient(fname,lname,gender_txt,birthdate_txt,userType,emailAddress);
                FirebaseDatabase.getInstance().getReference("USERS").child(uid)
                        .setValue(clientUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
//                                    Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_LONG).show();
                                    updateAdminPlumberList(fname, lname, uid);
                                }else {
                                    Toast.makeText(getActivity(), "Update Failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }

    // for upload certificate
    public void certificateAndSubscription(){
        dialogBuilder = new AlertDialog.Builder(getActivity());
        View openSubscription= getLayoutInflater().inflate(R.layout.certificate_and_subscriptions, null);

        //initialization
        SubsStat = (TextView) openSubscription.findViewById(R.id.subscriptionStat);
        paypalPayment = (WebView) openSubscription.findViewById(R.id.paymentSubsciption);
        openGallery = (Button) openSubscription.findViewById(R.id.open_galleryBtn);
        uploadBtn = (Button) openSubscription.findViewById(R.id.upload_btn);
        certCap = (ImageView) openSubscription.findViewById(R.id.certificateCapture);

        //==WebView initialization==
        paypalPayment.getSettings().setJavaScriptEnabled(true);
        paypalPayment.addJavascriptInterface(this, "android");
        paypalPayment.loadUrl("file:///android_asset/paypal.html");

        //uploading

        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseCert();
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (certUploadTask != null && certUploadTask.isInProgress()){
                    Toast.makeText(getActivity(), "upload in progress", Toast.LENGTH_SHORT).show();
                }else {
                    uploadCert();
                }
            }
        });


        //===== setting up view=====
        dialogBuilder.setView(openSubscription);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void updateAdminPlumberList(String firstname, String lastname, String uid){

        reff= FirebaseDatabase.getInstance().getReference("admin").child("plumber_list").child(uid);
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String act=snapshot.child("actions").getValue().toString();

                        adminAddPlumber update=new adminAddPlumber(firstname,lastname,act,uid);
                        FirebaseDatabase.getInstance().getReference("admin").child("plumber_list").child(uid)
                                .setValue(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }

    //----------methods in retrieving data into database---------

    private void choosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
    private void chooseCert(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == -1 && data!=null && data.getData() != null){
            mImageUri = data.getData();
//            Picasso.with(getActivity()).load(mImageUri).into(profileImageView);
            profileImageView.setImageURI(mImageUri);
        }
        if (requestCode == 2 && resultCode == -1 && data!=null && data.getData() != null){
            mImageUri = data.getData();
//            Picasso.with(getActivity()).load(mImageUri).into(profileImageView);
            certCap.setImageURI(mImageUri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver Cr=getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(Cr.getType(uri));
    }


    private void uploadFile(){

        ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Uploading Image.....");
        pd.show();

        if (mImageUri != null){

            Random random = new Random();
            int randomNumber = random.nextInt(1000-100) + 100;

            StorageReference fileReference= mStorageRef.child("profilepix"+ randomNumber + "" + "." + getFileExtension(mImageUri));
            mDatabaseRef.child(uid).child("ProfilePicture").setValue("profilepix"+ randomNumber +"."+ getFileExtension(mImageUri));

           mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                            //---upload item----

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

    // upload certificate from database
    private void uploadCert(){
        ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Uploading certificate.....");
        pd.show();

        if (mImageUri != null){

            Random random = new Random();
            int randomNumber = random.nextInt(1000-100) + 100;



            StorageReference fileReference= certStore.child("certificateCap"+ randomNumber + "" + "." + getFileExtension(mImageUri));
            certdb.child(uid).child(uid).child("filename").setValue("certificateCap"+ randomNumber +"."+ getFileExtension(mImageUri));

            certUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Certificate Uploaded", Toast.LENGTH_SHORT).show();
                            //---upload item----

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Certificate Upload failed", Toast.LENGTH_SHORT).show();
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
//displays profile picture from database...
    private void fetchprofilepicAndDisplay(){
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
//                                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    profileImageView.setImageBitmap(bitmap);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
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
    }

    public void fetchData(){

        //=========Accessing User Credentials=============
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            emailAddress=email;
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            uid = user.getUid();

            //========saving data into textviews======

            uid_data.setText(uid);

            //   =================== fetching data from firebase database==========
            reff = FirebaseDatabase.getInstance().getReference().child("USERS").child(uid);
            reff.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //-----------fetching data from firebasedatabase----------
                    String firstname = snapshot.child("firstname").getValue().toString();
                    String lastname = snapshot.child("lastname").getValue().toString();
                    String gender = snapshot.child("gender").getValue().toString();
                    String birthdate = snapshot.child("birthdate").getValue().toString();
                    String usertype = snapshot.child("userType").getValue().toString();

                    //------------displaying data into edittxt fields----------

                    firstname_txt.setText(firstname);
                    lastname_txt.setText(lastname);
                    gender_txt.setText(gender);
                    birthdate_txt.setText(birthdate);
                    //-------------displaying data into textview------------
                    usertype_txt.setText(usertype);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }


}
