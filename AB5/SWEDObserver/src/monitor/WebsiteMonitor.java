package monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Zentrale Klasse, die Subscriptions verwaltet und  prüft.
 */
public class WebsiteMonitor {
    private final List<Subscription> subscriptions = new ArrayList<>();
    private final UpdateManager updater = new UpdateManager();
    private final ScheduledExecutorService scheduler =
        Executors.newSingleThreadScheduledExecutor();

    //Funktion zum hinzufügen einer Subscription
    public void addSubscription(Subscription sub) {
        subscriptions.add(sub);
    }

    //Funktion zum entfernen vorhandener Subscriptions
    public void removeSubscription(String subId) {
        subscriptions.removeIf(s -> s.getSubId().equals(subId));
    }

    
   //Funktion zum updaten der vorhandenen Subscription
    public void updateSubscription(String subId, String newUrl, int newFreq) {
        Subscription old = subscriptions.stream()
            .filter(s -> s.getSubId().equals(subId))
            .findFirst()
            .orElse(null);

        if (old == null) {
            System.out.println("Keine Subscription mit ID " + subId + " gefunden.");
            return;
        }

        // remove old
        subscriptions.remove(old);

        // create updated, reuse same subId and owner
        Subscription updated = new Subscription(
            subId,
            newUrl,
            newFreq,
            old.getOwner()
        );

        subscriptions.add(updated);
        System.out.println("Subscription " + subId + " aktualisiert.");
    }

    public int getSubscriptionCount() {
        return subscriptions.size();
    }

    public void run() {
        for (Subscription sub : subscriptions) {
            final Subscription subscription = sub;
            long interval = subscription.getFrequency();
            scheduler.scheduleWithFixedDelay(
                () -> checkAndNotify(subscription),
                interval,      
                interval,
                TimeUnit.SECONDS
            );
        }
    }

    private void checkAndNotify(Subscription sub) {
        String msg = updater.checkUpdates(sub);
        if (msg != null) {
            sub.getOwner().getChannel().send(sub.getOwner(), msg);
        }
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

