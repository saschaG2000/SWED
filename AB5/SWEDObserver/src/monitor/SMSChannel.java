package monitor;

/** Beispiel: SMS–Channel */
public class SMSChannel implements ResponseChannel {
    @Override
    public void send(User user, String message) {
        // Hier nur Simulation
        System.out.printf("Sende SMS an %s: %s%n",
            user.getName(), message);
    }
}
