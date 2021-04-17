package main.stager;

import android.view.View;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import java.lang.reflect.InvocationTargetException;

public abstract class StagerExtendableListFragment<TVM extends ViewModel, TA extends Adapter, T>
        extends StagerListFragment<TVM, TA, T> {

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