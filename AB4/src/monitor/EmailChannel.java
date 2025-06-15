package monitor;

/** Beispiel: E-Mail–Channel */
public class EmailChannel implements ResponseChannel {
    @Override
    public void send(User user, String message) {
        System.out.printf("Sende E-Mail an %s (%s): %s%n",
            user.getName(), user.getEmail(), message);
    }
}
