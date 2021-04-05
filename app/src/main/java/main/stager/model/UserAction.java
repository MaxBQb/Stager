package main.stager.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class UserAction {
    @Getter private Status status;
    @Getter private String name;
    @Getter @Setter
    private String key;

    public UserAction(Status status, String name) {
        this.status = status;
        this.name = name;
        key = null;
    }
}

