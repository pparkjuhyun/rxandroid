package com.pparkjuhyun.rxandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pparkjuhyun.rxandroid.databinding.ActivityRetrofitBinding;

public class RetrofitActivity extends AppCompatActivity {
    private ActivityRetrofitBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRetrofitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {

    }
}
