package main.stager;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import main.stager.model.Stage;
import main.stager.model.UserAction;

public class StagesListViewModel extends AndroidViewModel {
    private static DataProvider dataProvider = DataProvider.getInstance();
    private MutableLiveData<List<Stage>> stages;

    public StagesListViewModel(@NonNull Application application) {
        super(application);
        stages = new MutableLiveData<>();
    }

    public LiveData<List<Stage>> getStages(String actionName) {
        if (stages.getValue() == null) {
            dataProvider.getAction(actionName).addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                UserAction ua = snapshot.getValue(UserAction.class);
                                if (ua != null)
                                    stages.postValue(ua.getStages());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    }
            );
        }
        return stages;
    }

}
