package channel;


import model.User;
import pattern.Observer;

public class EmailChannel implements Observer {
    @Override
    public void update(User user, String message) {
        sendEmail(user, message);
    }

    private void sendEmail(User user, String message) {
        // hier echte SMTP-Logik einbinden
        System.out.printf("Sende E-Mail an %s <%s>: %s%n",
            user.getName(), user.getEmail(), message);
    }
}