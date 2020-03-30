package com.pparkjuhyun.rxandroid.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import com.pparkjuhyun.rxandroid.R;
import com.pparkjuhyun.rxandroid.adapter.RecyclerViewAdapter;
import com.pparkjuhyun.rxandroid.databinding.ActivityRecyclerViewBinding;
import com.pparkjuhyun.rxandroid.vo.RecyclerViewItem;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RecyclerViewActivity extends AppCompatActivity {
    private ActivityRecyclerViewBinding binding;
    private RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecyclerViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAdapter == null) {
            return;
        }

        getItemObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    mAdapter.updateItems(item);
                    mAdapter.notifyDataSetChanged();
                });
    }

    private void init() {
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(llm);
        mAdapter = new RecyclerViewAdapter();
        binding.recyclerView.setAdapter(mAdapter);
        mAdapter.getItemPublishSubject()
                .subscribe(s -> toast(s.getTitle()));
    }

    private Observable<RecyclerViewItem> getItemObservable() {
        final PackageManager pm = getPackageManager();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        return Observable.fromIterable(pm.queryIntentActivities(i, 0))
                .sorted(new ResolveInfo.DisplayNameComparator(pm))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(item -> {
                    Drawable thumbnail = item.activityInfo.loadIcon(pm);
                    String title = item.activityInfo.loadLabel(pm).toString();
                    return RecyclerViewItem.of(thumbnail, title);
                });
    }

    private void toast(String title) {
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
    }
}
