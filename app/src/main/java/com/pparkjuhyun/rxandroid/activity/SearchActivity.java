package com.pparkjuhyun.rxandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

import com.jakewharton.rxbinding3.widget.RxTextView;
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent;
import com.pparkjuhyun.rxandroid.R;
import com.pparkjuhyun.rxandroid.databinding.ActivitySearchBinding;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;
    private Disposable mDispoasable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        // version1
//        mDispoasable = getObservable()
//                .debounce(500, TimeUnit.MILLISECONDS)
//                .filter(s -> !TextUtils.isEmpty(s))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(getObserver());

        //version2
        //RxTextView를 이용해
        mDispoasable = RxTextView.textChangeEvents(binding.etSearch)
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(s -> !TextUtils.isEmpty(s.getText().toString()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserverLib());
    }

    private Observable<CharSequence> getObservable() {
        return Observable.create(emitter -> binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emitter.onNext(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }));
    }

    private DisposableObserver<CharSequence> getObserver() {
        return new DisposableObserver<CharSequence>() {
            @Override
            public void onNext(CharSequence charSequence) {
                Log.v("rxAndroid", "Search: " + charSequence.toString());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private DisposableObserver<TextViewTextChangeEvent> getObserverLib() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent view) {
                Log.v("rxAndroid", view.getText().toString());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }
}
