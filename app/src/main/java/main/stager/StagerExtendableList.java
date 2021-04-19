package main.stager;

import android.view.View;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.reflect.InvocationTargetException;

public abstract class StagerExtendableList<TVM extends ViewModel,
                                           TA extends StagerListAdapter<T,
                                           ? extends RecyclerView.ViewHolder>,
                                           T> extends StagerList<TVM, TA, T> {

    // Listeners
    protected abstract void onButtonAddClicked(View v);

    @Override
    protected TA createAdapter() {
        try {
            return getAdapterType().getConstructor(NavController.class).newInstance(navigator);
        } catch (NoSuchMethodException e) {
            return super.createAdapter();
        } catch (InvocationTargetException e) {
            return super.createAdapter();
        } catch (IllegalAccessException e) {
            return null;
        } catch (java.lang.InstantiationException e) {
            return null;
        }
    }

    protected void setEventListeners() {
        super.setEventListeners();
        view.findViewById(R.id.button_list_add).setOnClickListener(this::onButtonAddClicked);
    }
}