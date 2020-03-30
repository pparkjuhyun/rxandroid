package com.pparkjuhyun.rxandroid.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pparkjuhyun.rxandroid.adapter.viewholder.RecyclerViewHolder;
import com.pparkjuhyun.rxandroid.databinding.RecyclerViewItemBinding;
import com.pparkjuhyun.rxandroid.vo.RecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private List<RecyclerViewItem> mItems = new ArrayList<>();
    private PublishSubject<RecyclerViewItem> mPublishSubject;

    public RecyclerViewAdapter() {
        this.mPublishSubject =PublishSubject.create();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerViewItemBinding binding = RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        final RecyclerViewItem item = mItems.get(position);
        holder.binding.ivThumbnail.setImageDrawable(item.getImage());
        holder.binding.tvTitle.setText(item.getTitle());
        holder.getClickObserver(item).subscribe(mPublishSubject);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateItems(List<RecyclerViewItem> items) {
        mItems.addAll(items);
    }

    public void updateItems(RecyclerViewItem item) {
        mItems.add(item);
    }

    public PublishSubject<RecyclerViewItem> getItemPublishSubject() {
        return mPublishSubject;
    }
}
