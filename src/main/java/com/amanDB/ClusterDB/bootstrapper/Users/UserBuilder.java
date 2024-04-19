package com.amanDB.ClusterDB.bootstrapper.Users;

public class UserBuilder {
   private int userID;
   private String username;
   private String password;
   private String role;


   public UserBuilder userID(int userID){
       this.userID =userID;
       return this;
   }
    public UserBuilder username(String username){
        this.username =username;
        return this;
    }
    public UserBuilder password(String password){
        this.password =password;
        return this;
    }
    public UserBuilder role(String role){
        this.role =role;
        return this;
    }

   public User build(){
       return new User(userID,username,password,role);
   }
}
