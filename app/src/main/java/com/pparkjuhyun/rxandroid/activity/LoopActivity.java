package com.pparkjuhyun.rxandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import com.pparkjuhyun.rxandroid.databinding.ActivityLoopBinding;
import java.util.Arrays;
import io.reactivex.Observable;

public class LoopActivity extends AppCompatActivity {
    public static final String TAG = LoopActivity.class.getSimpleName();

    private ActivityLoopBinding binding;

    Iterable<String> samples = Arrays.asList(
            "banana", "orange", "apple", "apple mango", "melon", "watermelon"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        binding.btnLoop1.setOnClickListener( view -> {
            Log.v("rxAndroid", ">>>> get an apple :: Java");
            for(String s : samples) {
                if (s.contains("apple")) {
                    Log.v("rxAndroid", s);
                    return;
                }
            }
        });

        binding.btnLoop2.setOnClickListener(view -> {
            Log.v("rxAndroid", ">>>> get an apple :: rx 1.x");

        });

        binding.btnLoop3.setOnClickListener(view -> {
            Log.v("rxAndroid", ">>>> get an apple :: rx2.x");
            Observable.fromIterable(samples)
                    .filter(s -> s.contains("apple"))
                    .first("Not found")
                    .subscribe(s -> Log.v("rxAndroid",s));
        });
    }
}
