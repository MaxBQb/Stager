package main.stager.Base;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import java.util.HashMap;
import main.stager.utils.ChangeListeners.firebase.ValueEventListener;
import main.stager.utils.DataProvider;
import main.stager.utils.Utilits;

public abstract class StagerViewModel extends AndroidViewModel {
    protected static DataProvider dataProvider = DataProvider.getInstance();
    protected HashMap<Object, DatabaseReference> backPath;

    public StagerViewModel(@NonNull Application application) {
        super(application);
        backPath = new HashMap<>();
    }

    @FunctionalInterface
    protected interface EventListenerSetter {
        void add();
    }

    protected <T> LiveData<T> getData(MutableLiveData<T> data, EventListenerSetter els) {
        if (data.getValue() == null)
            els.add();
        return data;
    }

    protected LiveData<String> getText(MutableLiveData<String> data, DatabaseReference ref) {
        return getText(data, ref, "");
    }

    /** Возвращает ссылку, по которой были получены данные
     * @param data - LiveData
     * @return
     */
    public DatabaseReference getBackPathTo(Object data) {
        return backPath.get(data);
    }

    protected LiveData<String> getText(MutableLiveData<String> data, DatabaseReference ref, String defaultValue) {
        backPath.put(data, ref);
        return getData(data, () -> ref.addValueEventListener(
                new ValueEventListener<String>(data, String.class) {
                    @Override
                    protected String modify(String item, DataSnapshot snapshot) {
                        if (Utilits.isNullOrBlank(item))
                            return defaultValue;
                        return item.trim();
                    }
                }
        ));
    }
}