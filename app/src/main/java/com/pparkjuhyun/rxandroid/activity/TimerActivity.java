package com.pparkjuhyun.rxandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.pparkjuhyun.rxandroid.adapter.LogAdapter;
import com.pparkjuhyun.rxandroid.databinding.ActivityTimerBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TimerActivity extends AppCompatActivity {
    private ActivityTimerBinding binding;
    private LogAdapter mAdapter;
    private List<String> mLogs;

    private static final long INITIAL_DELAY = 0L;
    private static final long PERIOD = 3L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        setupLogger();
        binding.btnPolling.setOnClickListener(view -> {
            startPollingV1();
        });

        binding.btnPollingOp.setOnClickListener(view -> {
            startPollingV2();
        });
    }

    private void startPollingV1() {
        Observable<String> ob = Observable.interval(INITIAL_DELAY, PERIOD, TimeUnit.SECONDS)
                .flatMap(o -> Observable.just("polling #1 " + o.toString()));

        ob.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::log);
    }

    private void startPollingV2() {
        Observable<String> ob2 = Observable.just("polling #2")
                .repeatWhen(o -> o.delay(PERIOD, TimeUnit.SECONDS));

        ob2.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::log);
    }

    private void log(String log) {
        mLogs.add(log);
        mAdapter.clear();
        mAdapter.addAll(mLogs);
    }

    private void setupLogger() {
        mLogs = new ArrayList<>();
        mAdapter = new LogAdapter(this, new ArrayList<>());
        binding.lvLog.setAdapter(mAdapter);
    }
}
