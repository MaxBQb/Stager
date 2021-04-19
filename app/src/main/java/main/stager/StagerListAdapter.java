package main.stager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public abstract class StagerListAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    protected List<T> mValues = new ArrayList<>();
    protected OnItemClickListener<T> onItemClickListener;

    public void setValues(List<T> values) {
        mValues = values;
        notifyDataSetChanged();
    }

    public T get(int index) {
        return mValues.get(index);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @FunctionalInterface
    public interface OnItemClickListener<T> {
        void onItemClick(T item, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
