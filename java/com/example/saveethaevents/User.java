package com.example.saveethaevents;

public class User {
    public String pass, dept, access, name;
    public User(){
        // do nothing
    }
    public User(String password, String dept, String access, String name){
        this.pass=password;
        this.dept=dept;
        this.access=access;
        this.name=name;
    }
}
