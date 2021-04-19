package main.stager;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public abstract class StagerListAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    protected List<T> mValues = new ArrayList<>();

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
}
