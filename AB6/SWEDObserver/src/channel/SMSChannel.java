package channel;



import model.User;
import pattern.Observer;

public class SMSChannel implements Observer {
    @Override
    public void update(User user, String message) {
        sendSms(user, message);
    }

    private void sendSms(User user, String message) {
        // hier Anbindung an SMS-Gateway
        System.out.printf("Sende SMS an %s (%s): %s%n",
            user.getName(), user.getPhoneNumber(), message);
    }
}