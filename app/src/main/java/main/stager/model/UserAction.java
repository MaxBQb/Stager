package main.stager.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserAction {
    @Getter private List<Stage> stages;
    @Getter private Status status;

    public enum Status {
        WAITING,
        SUCCEED,
        ABORTED
    }
}

