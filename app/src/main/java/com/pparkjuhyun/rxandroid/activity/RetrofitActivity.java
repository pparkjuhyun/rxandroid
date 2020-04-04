package com.pparkjuhyun.rxandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pparkjuhyun.rxandroid.adapter.LogAdapter;
import com.pparkjuhyun.rxandroid.databinding.ActivityRetrofitBinding;
import com.pparkjuhyun.rxandroid.network.retrofit.Contributor;
import com.pparkjuhyun.rxandroid.network.retrofit.GitHubServiceApi;
import com.pparkjuhyun.rxandroid.network.retrofit.RestfulAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitActivity extends AppCompatActivity {
    private ActivityRetrofitBinding binding;

    private static final String sName = "jungjoonpark-pandora";
    private static final String sRepo = "rxAndroid";

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRetrofitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        setupLogger();

        binding.btnOnlyRetrofit.setOnClickListener(view -> {
            startRetrofit();
        });
        binding.btnRetrofitOkhttp.setOnClickListener(view -> {
            startOkHttp();
        });
        binding.btnRetrofitRxjava.setOnClickListener(view -> {
            startRx();
        });
    }

    /**
     * Only Retrofit (internal OkHttp)
     */
    private void startRetrofit() {
        GitHubServiceApi service = RestfulAdapter.getInstance().getSimpleApi();
        Call<List<Contributor>> call = service.getCallContributors(sName, sRepo);
        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                if(response.isSuccessful()) {
                    List<Contributor> contributors = response.body();
                    for(Contributor c : contributors) {
                        log(c.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable t) {
                log(t.getMessage());
            }
        });
    }

    /**
     * retrofit + OkHttp
     */
    private void startOkHttp() {
        GitHubServiceApi service = RestfulAdapter.getInstance().getServiceApi();
        Call<List<Contributor>> call = service.getCallContributors(sName, sRepo);

        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                if(response.isSuccessful()) {
                    List<Contributor> contributors = response.body();
                    for(Contributor c : contributors) {
                        log(c.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable t) {
                log(t.getMessage());
            }
        });
    }

    /**
     * Retrofit + OkHttp + Rxjava
     */
    private void startRx() {
        GitHubServiceApi service = RestfulAdapter.getInstance().getServiceApi();
        Observable<List<Contributor>> observable = service.getObContributors(sName, sRepo);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Contributor>>() {
                    @Override
                    public void onNext(List<Contributor> contributors) {
                        for (Contributor c : contributors) {
                            log(c.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        log(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        log("complete");
                    }
                })


        );
    }

    // Log
    private LogAdapter mLogAdapter;
    private List<String> mLogs;

    private void log(String log) {
        mLogs.add(log);
        mLogAdapter.clear();
        mLogAdapter.addAll(mLogs);
    }

    private void setupLogger() {
        mLogs = new ArrayList<>();
        mLogAdapter = new LogAdapter(this, new ArrayList<>());
        binding.lvRfLog.setAdapter(mLogAdapter);
    }
}
