package app;


import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import channel.EmailChannel;
import channel.SMSChannel;
import model.Subscription;
import model.User;
import service.ComparisonStrategy;
import service.HtmlComparisonStrategy;
import service.SizeComparisonStrategy;
import service.TextComparisonStrategy;
import service.WebsiteMonitor;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        WebsiteMonitor monitor = new WebsiteMonitor();

        mainLoop:
        while (true) {
            System.out.println("=== Hauptmenü ===");
            System.out.println("1) Settings");
            System.out.println("2) Start Monitoring");
            System.out.print("Auswahl (1/2): ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    settingsLoop(sc, monitor);
                    break;
                case "2":
                    if (monitor.getSubscriptions().isEmpty()) {
                        System.out.println("Fehler: Keine Subscriptions vorhanden!");
                        break;
                    }
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        System.out.println("\nBeende Monitoring…");
                        monitor.shutdown();
                        System.out.println("Fertig.");
                    }));
                    monitor.startMonitoring();
                    new CountDownLatch(1).await();
                    break mainLoop;
                default:
                    System.out.println("Ungültige Auswahl, bitte 1 oder 2.");
            }
        }
    }

    private static void settingsLoop(Scanner sc, WebsiteMonitor monitor) {
        while (true) {
            System.out.println("\n--- Settings ---");
            System.out.println("a) Neue Subscription");
            System.out.println("b) Subscription ändern");
            System.out.println("c) Subscription löschen");
            System.out.println("d) Zurück");
            System.out.print("Auswahl (a–d): ");
            String cmd = sc.nextLine().trim().toLowerCase();

            switch (cmd) {
                case "a":
                    createSubscription(sc, monitor);
                    break;
                case "b":
                    System.out.print("Sub-ID zum Ändern: ");
                    String idUp = sc.nextLine().trim();
                    monitor.removeSubscription(idUp);
                    // vereinfachtes Update: neu anlegen
                    createSubscription(sc, monitor, idUp);
                    System.out.println("Subscription " + idUp + " aktualisiert.");
                    break;
                case "c":
                    System.out.print("Sub-ID zum Löschen: ");
                    String idDel = sc.nextLine().trim();
                    monitor.removeSubscription(idDel);
                    System.out.println("Subscription " + idDel + " gelöscht.");
                    break;
                case "d":
                    return;
                default:
                    System.out.println("Ungültig, bitte a–d eingeben.");
            }
        }
    }

    private static void createSubscription(Scanner sc, WebsiteMonitor monitor) {
        createSubscription(sc, monitor, null);
    }

    private static void createSubscription(Scanner sc,
                                           WebsiteMonitor monitor,
                                           String forcedId) {
        System.out.print("User-ID: ");
        String userId = sc.nextLine().trim();
        System.out.print("Name: ");
        String name   = sc.nextLine().trim();
        System.out.print("E-Mail: ");
        String email  = sc.nextLine().trim();
        System.out.print("Telefonnummer: ");
        String phone  = sc.nextLine().trim();
        System.out.print("Kanal (email/sms): ");
        String chan   = sc.nextLine().trim().toLowerCase();

        User user = new User(
            userId,
            name,
            email,
            phone,
            chan.equals("sms") ? new SMSChannel() : new EmailChannel()
        );

        System.out.print("Zu überwachende URL: ");
        String url = sc.nextLine().trim();
        System.out.print("Intervall in Sekunden: ");
        int freq = Integer.parseInt(sc.nextLine().trim());

        System.out.print("Strategie (1=Größe,2=HTML,3=Text): ");
        int strat = Integer.parseInt(sc.nextLine().trim());
        ComparisonStrategy comparator;
        switch (strat) {
            case 2: comparator = new HtmlComparisonStrategy(); break;
            case 3: comparator = new TextComparisonStrategy(); break;
            default: comparator = new SizeComparisonStrategy(); break;
        }

        String subId = forcedId != null
            ? forcedId
            : userId + "-" + (monitor.getSubscriptions().size() + 1);

        Subscription sub = new Subscription(subId, url, freq, user, comparator);
        monitor.addSubscription(sub);
        System.out.println("Neue Subscription angelegt: " + subId + "\n");
    }
}