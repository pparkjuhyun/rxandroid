package com.pparkjuhyun.rxandroid.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.jakewharton.rxbinding3.view.RxView;
import com.pparkjuhyun.rxandroid.databinding.ActivityRxsampleBinding;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.observers.DisposableObserver;

public class RxSampleActivity extends RxAppCompatActivity {
    private ActivityRxsampleBinding binding;
    private static final int SEVEN = 7;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRxsampleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getClickEventObservable()
                .map(s -> "clicked")
                .subscribe(getObserver());

        getClickEventObservableWithLambda()
                .map(s -> "clicked lambda")
                .subscribe(s -> Log.v("rxAndroid", s));

        getClickEventObservableWithRxBinding()
                .subscribe(s -> Log.v("rxAndroid", s));

        getClickEventObservableExtra()
                .map(local -> SEVEN)
                .flatMap(this::compareNumbers)
                .subscribe(s -> Log.v("rxAndroid", s));
    }

    private Observable<View> getClickEventObservable() {
        return Observable.create(new ObservableOnSubscribe<View>() {
            @Override
            public void subscribe(ObservableEmitter<View> emitter) throws Exception {
                binding.btnClick1.setOnClickListener(emitter::onNext);
            }
        });
    }

    private DisposableObserver<? super String> getObserver() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.v("rxAndroid", s);
            }

            @Override
            public void onError(Throwable e) {
                Log.v("rxAndroid", e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.v("rxAndroid", "Complete");
            }
        };
    }

    private Observable<View> getClickEventObservableWithLambda() {
        return Observable.create(s -> binding.btnClick2.setOnClickListener(s::onNext));
    }

    private Observable<String> getClickEventObservableWithRxBinding() {
        return RxView.clicks(binding.btnClick3)
                .map(s -> "Clicked RxBinding");
    }

     private Observable<View> getClickEventObservableExtra() {
        return Observable.create(s -> binding.btnClick4.setOnClickListener(s::onNext));
    }

    private Observable<String> compareNumbers(int input) {
        return Observable.just(input)
                .flatMap( in -> {
                    Random random = new Random();
                    int data = random.nextInt(10);
                    return Observable.just("local : " + in, "remote : " + data, "result = " + (in == data));
                });
    }
}
