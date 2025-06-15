package service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import model.Subscription;

public class WebsiteMonitor {
    private final List<Subscription> subscriptions = new ArrayList<>();
    private final ScheduledExecutorService scheduler =
        Executors.newSingleThreadScheduledExecutor();

    public void addSubscription(Subscription sub) {
        sub.registerObserver(sub.getOwner().getChannel());
        subscriptions.add(sub);
    }

    public void removeSubscription(String subId) {
        subscriptions.removeIf(s -> s.getSubId().equals(subId));
    }

    public List<Subscription> getSubscriptions() {
        return List.copyOf(subscriptions);
    }

    /** Startet das periodische Prüfen aller Subscriptions */
    public void startMonitoring() {
        for (Subscription sub : subscriptions) {
            scheduler.scheduleAtFixedRate(
                sub::checkAndNotify,
                0,
                sub.getFrequency(),
                TimeUnit.SECONDS
            );
        }
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}