package monitor;

public class User {
    private final String userId;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final ResponseChannel channel;

    public User(String userId,
                String name,
                String email,
                String phoneNumber,
                ResponseChannel channel) {
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
    public ResponseChannel getChannel() { return channel; }
}
