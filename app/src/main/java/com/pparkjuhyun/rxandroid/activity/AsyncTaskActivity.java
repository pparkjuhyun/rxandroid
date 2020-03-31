package com.pparkjuhyun.rxandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.jakewharton.rxbinding3.widget.RxTextView;
import com.pparkjuhyun.rxandroid.R;
import com.pparkjuhyun.rxandroid.databinding.ActivityAsyncTaskBinding;

import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class AsyncTaskActivity extends AppCompatActivity {
    private ActivityAsyncTaskBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAsyncTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //traditional asynctask
        RxAsyncTask task = new RxAsyncTask();
        task.execute("Hello", "async", "world");

        //reactive style
        initRxAsync();
    }

    public class RxAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder word = new StringBuilder();
            for(String s : strings) {
                word.append(s).append(" ");
            }
            return word.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            binding.tvTask.setText(s);
        }
    }

    private void initRxAsync() {
        Observable.just("Hello", "rx", "world")
                .reduce((x, y) -> x + " " + y)
                .observeOn(AndroidSchedulers.mainThread())
                //.subscribe(getObserver())
                .subscribe(
                        binding.tvRxTask::setText,
                        e -> Log.e("rxAndroid", e.getMessage()),
                        () -> Log.v("rxAndroid", "done")
                );
    }

    private MaybeObserver<String> getObserver() {
        return new MaybeObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(String s) {
                binding.tvRxTask.setText(s);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("rxAndroid", e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.v("rxAndroid", "done");
            }
        };
    }
}
