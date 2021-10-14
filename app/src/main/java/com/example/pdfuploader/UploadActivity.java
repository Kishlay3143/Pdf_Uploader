package com.example.pdfuploader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.security.ProtectionDomain;

public class UploadActivity extends AppCompatActivity {

    EditText fileTitle;
    Button upload;
    ImageView file,cancel,browse;

    Uri filePath;
    StorageReference storageReference;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        getSupportActionBar().hide();  // Hide Action Bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   // Hide Status Bar

        fileTitle=findViewById(R.id.fileTitle);
        upload=findViewById(R.id.upload);
        file=findViewById(R.id.file);
        cancel=findViewById(R.id.cancel);
        browse=findViewById(R.id.browse);

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference=FirebaseDatabase.getInstance().getReference("myDocuments");

        file.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                file.setVisibility(View.INVISIBLE);
                cancel.setVisibility(View.INVISIBLE);
                browse.setVisibility(View.VISIBLE);
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
//                startActivity(new Intent(getApplicationContext(),UploadActivity.class));
                Dexter.withContext(getApplicationContext()).
                        withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(final PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Select Pdf Files"),101);
                            }

                            @Override
                            public void onPermissionDenied(final PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(final PermissionRequest permissionRequest, final PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                processUpload(filePath);
            }
        });

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==101 && resultCode==RESULT_OK){
            filePath=data.getData();
            file.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            browse.setVisibility(View.INVISIBLE);
        }
    }

    public void processUpload(Uri filePath){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading.....");
        pd.show();

        final StorageReference reference=storageReference.child("uploads/"+System.currentTimeMillis()+".pdf");
        reference.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                        // We have done this to get the url of the file downloaded inside the Firebase storage.
                        // Now,this url would be passed inside the realtime database.
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {

                                fileInfoModel obj=new fileInfoModel(fileTitle.getText().toString(),uri.toString());
                                databaseReference.child(databaseReference.push().getKey()).setValue(obj);
                                pd.dismiss();
                                Toast.makeText(UploadActivity.this, "File Uploaded !", Toast.LENGTH_SHORT).show();


                                file.setVisibility(View.INVISIBLE);
                                cancel.setVisibility(View.INVISIBLE);
                                browse.setVisibility(View.VISIBLE);
                                fileTitle.setText("");
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull final UploadTask.TaskSnapshot snapshot) {

                        float percent=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        pd.setMessage("Uploaded: "+(int)percent+"%");
                    }
                });
    }
}