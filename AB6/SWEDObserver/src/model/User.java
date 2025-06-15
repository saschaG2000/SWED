package model;

import pattern.Observer;

public class User {
    private final String userId;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final Observer channel;

    public User(String userId, String name, String email, String phoneNumber, Observer channel) {
        this.userId      = userId;
        this.name        = name;
        this.email       = email;
        this.phoneNumber = phoneNumber;
        this.channel     = channel;
    }

    public String getUserId()      { return userId; }
    public String getName()        { return name; }
    public String getEmail()       { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public Observer getChannel()   { return channel; }
}