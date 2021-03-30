package main.stager.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import main.stager.User;

@AllArgsConstructor
@NoArgsConstructor
public class UserAction {
    @Getter private String owner;
    @Getter private List<String> sharedTo;
    @Getter private List<Stage> stages;
    @Getter private Status status;

    public enum Status {
        WAITING,
        SUCCEED,
        ABORTED
    }
}

