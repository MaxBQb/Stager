package main.stager.list;

import android.view.View;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import main.stager.R;

public abstract class StagerExtendableList<TVM extends ViewModel,
                                           TA extends StagerListAdapter<T,
                                                                                      ? extends RecyclerView.ViewHolder>,
                                           T> extends StagerList<TVM, TA, T> {

    // Listeners
    protected abstract void onButtonAddClicked(View v);

    protected void setEventListeners() {
        super.setEventListeners();
        view.findViewById(R.id.button_list_add).setOnClickListener(this::onButtonAddClicked);
    }
}