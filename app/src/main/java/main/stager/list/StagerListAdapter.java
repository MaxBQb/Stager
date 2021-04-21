package main.stager.list;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.stager.model.FBModel;

public abstract class StagerListAdapter<T, VH extends RecyclerView.ViewHolder>
        extends ListAdapter<T, VH> {
    protected OnItemClickListener<T> onItemClickListener;

    protected StagerListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    public T get(int index) {
        return getItem(index);
    }

    @FunctionalInterface
    public interface OnItemClickListener<T> {
        void onItemClick(T item, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void moveItem(int from, int to) {
        ArrayList<T> list = new ArrayList<T>(getCurrentList());
        list.add(to, list.remove(from));
        submitList(list);
    }

    protected void bindOnItemClickListener(View view, T item, int pos){
        if (onItemClickListener != null)
            view.setOnClickListener(v -> onItemClickListener
                    .onItemClick(item, pos));
    }

    protected abstract static class FBItemCallback<U extends FBModel> extends DiffUtil.ItemCallback<U> {
        @Override
        public boolean areItemsTheSame(@NonNull U oldItem, @NonNull U newItem) {
            return oldItem.getKey().equals(newItem.getKey());
        }
    }
}
