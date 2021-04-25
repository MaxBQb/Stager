package main.stager.linkers;

import androidx.annotation.DrawableRes;
import org.jetbrains.annotations.NotNull;
import main.stager.R;
import main.stager.model.Status;

public class LStatus {
    public static @DrawableRes int toIcon(@NotNull Status status) {
        switch (status) {
            case ABORTED: return R.drawable.ic_stage_status_abort;
            case SUCCEED: return R.drawable.ic_stage_status_succes;
            case WAITING: return R.drawable.ic_stage_status_wait;
            case LOCKED:  return R.drawable.ic_stage_status_locked;
            default: throw new IllegalArgumentException();
        }
    }
}
