package main.stager.ui.activity_stages;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import main.stager.DataProvider;
import main.stager.model.Stage;
import main.stager.model.UserAction;

public class StagesListViewModel extends AndroidViewModel {
    private static DataProvider dataProvider = DataProvider.getInstance();
    private MutableLiveData<List<Stage>> stages;
    private MutableLiveData<String> actionName;

    public StagesListViewModel(@NonNull Application application) {
        super(application);
        stages = new MutableLiveData<>();
        actionName = new MutableLiveData<>();
    }

    public LiveData<List<Stage>> getStages(String key) {
        if (stages.getValue() == null)
            dataProvider.getStages(key).addValueEventListener(
                DataProvider.getListChangesListener(stages, Stage.class)
            );
        return stages;
    }

    public LiveData<String> getActionName(String key, String defaultValue) {
        if (actionName.getValue() == null)
            dataProvider.getAction(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists())
                            return;
                        UserAction ua = snapshot.getValue(UserAction.class);
                        actionName.postValue(ua != null ? ua.getName() : defaultValue);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Do nothing :)
                    }
            });
        return actionName;
    }
}
