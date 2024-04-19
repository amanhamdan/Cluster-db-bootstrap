package com.amanDB.ClusterDB.bootstrapper.Users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private String username;
    private String password;
    private String role;
    private int userID;

     public User(int userID, String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.userID = userID;
    }
}
