package com.example.chaton.view.activities.display;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.chaton.R;
import com.example.chaton.common.Common;
import com.example.chaton.databinding.ActivityViewImageBinding;

public class ViewImageActivity extends AppCompatActivity {

    private ActivityViewImageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_image);

        binding.imageView.setImageBitmap(Common.IMAGE_BITMAP);
    }
}
