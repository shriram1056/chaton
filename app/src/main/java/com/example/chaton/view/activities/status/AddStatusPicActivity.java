package com.example.chaton.view.activities.status;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.chaton.R;
import com.example.chaton.databinding.ActivityAddStatusPicBinding;
import com.example.chaton.managers.ChatService;
import com.example.chaton.model.StatusModel;
import com.example.chaton.service.FirebaseService;
import com.example.chaton.view.MainActivity;

import java.util.Random;
import java.util.UUID;

import io.opencensus.internal.Utils;

public class AddStatusPicActivity extends AppCompatActivity {

    private Uri imageUri;
    private ActivityAddStatusPicBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_status_pic);

        imageUri = MainActivity.imageUri;

        setInfo();
        initClick();

    }

    private void initClick() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseService(AddStatusPicActivity.this).uploadImageToFireBaseStorage(imageUri, new FirebaseService.OnCallBack() {
                    @Override
                    public void onUploadSuccess(String imageUrl) {
                        StatusModel status = new StatusModel();
                        status.setId(UUID.randomUUID().toString());
                        status.setCreatedDate(new ChatService(AddStatusPicActivity.this).getCurrentDate());
                        status.setImageStatus(imageUrl);
                        status.setUserID(FirebaseAuth.getInstance().getUid());
                        status.setViewCount("0");
                        status.setTextStatus(binding.edDescription.getText().toString());

                        new FirebaseService(AddStatusPicActivity.this).addNewStatus(status, new FirebaseService.OnAddNewStatusCallBack() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailed() {
                                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onUploadFailed(Exception e) {
                        Log.e("TAG", "onUploadFailed: ",e );
                    }
                });
            }
        });
    }

    private void setInfo() {
        Glide.with(this).load(imageUri).into(binding.imageView);
    }
}