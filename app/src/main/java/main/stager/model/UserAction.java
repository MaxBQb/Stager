package main.stager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserAction extends FBModel {
    @Getter private Status status;
    @Getter private String name;
}

