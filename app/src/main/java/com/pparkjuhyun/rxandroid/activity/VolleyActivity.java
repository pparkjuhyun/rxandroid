package com.pparkjuhyun.rxandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.pparkjuhyun.rxandroid.adapter.LogAdapter;
import com.pparkjuhyun.rxandroid.databinding.ActivityVolleyBinding;
import com.pparkjuhyun.rxandroid.network.LocalVolley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class VolleyActivity extends AppCompatActivity {
    private ActivityVolleyBinding binding;
    private static final String URL = "http://time.jsontest.com/";

    private CompositeDisposable mCompositeDisposable;

    private LogAdapter mAdapter;
    private List<String> mLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVolleyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        mCompositeDisposable = new CompositeDisposable();
        setupLogger();

        binding.vfBtnGet.setOnClickListener(view -> {
            post(getObservable());
        });

        binding.vfBtnGet2.setOnClickListener(view -> {
            post(getObservableFromCallable());
        });

        binding.vfBtnGet3.setOnClickListener(view -> {
            post(getObservableFromFuture());
        });
    }


    private void post(Observable<JSONObject> observable) {
        DisposableObserver<JSONObject> observer = getObserver();

        mCompositeDisposable.add(
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(observer)
        );
    }

    private RequestFuture<JSONObject> getFuture() {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest req = new JsonObjectRequest(URL, null, future, future);
        LocalVolley.getRequestQueue().add(req);
        return future;
    }

    private JSONObject getData() throws ExecutionException, InterruptedException {
        return getFuture().get();
    }

    private Observable<JSONObject> getObservable() {
        return Observable.defer(() -> {
            try {
                return Observable.just(getData());
            } catch (InterruptedException e) {
                Log.e("rxAndroid", "error: " + e.getMessage());
                return Observable.error(e);
            } catch (ExecutionException e) {
                Log.e("rxAndroid", "error: " + e.getCause());
                return Observable.error(e.getCause());
            }
        });
    }

    private Observable<JSONObject> getObservableFromCallable() {
        return Observable.fromCallable(this::getData);
    }

    private Observable<JSONObject> getObservableFromFuture() {
        return Observable.fromFuture(getFuture());
    }

    private DisposableObserver<JSONObject> getObserver() {
        return new DisposableObserver<JSONObject>() {
            @Override
            public void onNext(JSONObject jsonObject) {
                log(" >> " + jsonObject.toString());
            }

            @Override
            public void onError(Throwable t) {
                log(t.toString());
            }

            @Override
            public void onComplete() {
                log("complete");
            }
        };
    }

    private void log(String log) {
        mLogs.add(log);
        mAdapter.clear();
        mAdapter.addAll(mLogs);
    }

    private void setupLogger() {
        mLogs = new ArrayList<>();
        mAdapter = new LogAdapter(this, new ArrayList<>());
        binding.vfLvLog.setAdapter(mAdapter);
    }
}
