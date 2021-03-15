package main.stager;

public class User {
    public String id, nickname, email, hash;

    public User() {}

    public User(String id, String nickname, String email, String hash) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.hash = hash;
    }

}

