package main.stager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Stage {
    @Getter
    private UserAction.Status currentStatus;
    @Getter private String name;
}