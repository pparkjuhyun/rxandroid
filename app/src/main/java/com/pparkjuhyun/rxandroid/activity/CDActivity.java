package com.pparkjuhyun.rxandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pparkjuhyun.rxandroid.databinding.ActivityCDBinding;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class CDActivity extends AppCompatActivity {
    private ActivityCDBinding binding;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCDBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Disposable disposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("hello world!");
                e.onComplete();
            }
        }).subscribe(binding.tvHello::setText);

        mCompositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }
}
