package com.pparkjuhyun.rxandroid.adapter.viewholder;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pparkjuhyun.rxandroid.databinding.RecyclerViewItemBinding;
import com.pparkjuhyun.rxandroid.vo.RecyclerViewItem;

import io.reactivex.Observable;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    public RecyclerViewItemBinding binding;

    public RecyclerViewHolder(RecyclerViewItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;

    }

    public Observable<RecyclerViewItem> getClickObserver (RecyclerViewItem item) {
        return Observable.create(e -> binding.getRoot().setOnClickListener(
            view -> e.onNext(item)
        ));
    }
}
