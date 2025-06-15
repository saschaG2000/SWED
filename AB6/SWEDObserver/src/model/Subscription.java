package model;


import java.util.ArrayList;
import java.util.List;

import pattern.Observer;
import pattern.Subject;
import service.ComparisonStrategy;

public class Subscription implements Subject {
    private final String subId;
    private final String url;
    private final int frequency;               // in Sekunden
    private final User owner;
    private final ComparisonStrategy comparator;
    private final List<Observer> observers = new ArrayList<>();

    public Subscription(String subId,
                        String url,
                        int frequency,
                        User owner,
                        ComparisonStrategy comparator) {
        this.subId      = subId;
        this.url        = url;
        this.frequency  = frequency;
        this.owner      = owner;
        this.comparator = comparator;
    }

    @Override
    public void registerObserver(Observer o) {
        if (!observers.contains(o)) observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer o : observers) {
            o.update(owner, message);
        }
    }

    /**
     * Prüft Strategie und benachrichtigt bei Änderung.
     * Wird vom WebsiteMonitor periodisch aufgerufen.
     */
    public void checkAndNotify() {
        if (comparator.hasChanged(url)) {
            String message = "Neuer Inhalt gefunden unter " + url;
            notifyObservers(message);
        }
    }

    public String getSubId()        { return subId; }
    public String getUrl()          { return url; }
    public int    getFrequency()    { return frequency; }
    public User   getOwner()        { return owner; }
}