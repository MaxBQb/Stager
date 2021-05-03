package main.stager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Contact extends FBModel {
    @Getter private String name;
    @Getter private String description;
    @Getter private String email;
}

