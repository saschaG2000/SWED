package monitor;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

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
                    if (monitor.getSubscriptionCount() == 0) {
                        System.out.println("Fehler: Keine Subscriptions vorhanden!");
                        break;
                    }
                    startMonitoring(monitor);
                    break mainLoop;

                default:
                    System.out.println("Ungültige Auswahl, bitte 1 oder 2.");
            }
        }
    }

    private static void settingsLoop(Scanner sc, WebsiteMonitor monitor) {
        settingsLoop:
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
                    System.out.print("Neue URL: ");
                    String urlUp = sc.nextLine().trim();
                    System.out.print("Neues Intervall (s): ");
                    int freqUp = Integer.parseInt(sc.nextLine().trim());
                    monitor.updateSubscription(idUp, urlUp, freqUp);
                    break;

                case "c":
                    System.out.print("Sub-ID zum Löschen: ");
                    String idDel = sc.nextLine().trim();
                    monitor.removeSubscription(idDel);
                    System.out.println("Subscription " + idDel + " gelöscht.");
                    break;

                case "d":
                    break settingsLoop;

                default:
                    System.out.println("Ungültig, bitte a–d eingeben.");
            }
        }
    }

    private static void createSubscription(Scanner sc, WebsiteMonitor monitor) {
        System.out.print("User-ID: ");
        String userId = sc.nextLine().trim();

        System.out.print("Name: ");
        String name = sc.nextLine().trim();

        System.out.print("E-Mail: ");
        String email = sc.nextLine().trim();

        System.out.print("Telefonnummer: ");
        String phone = sc.nextLine().trim();

        System.out.print("Kanal (email/sms): ");
        String chan = sc.nextLine().trim().toLowerCase();
        ResponseChannel channel = chan.equals("sms")
            ? new SMSChannel()
            : new EmailChannel();

        User user = new User(userId, name, email, phone, channel);

        System.out.print("Zu überwachende URL: ");
        String url = sc.nextLine().trim();

        System.out.print("Intervall in Sekunden: ");
        int freq = Integer.parseInt(sc.nextLine().trim());

        String subId = userId + "-" + (monitor.getSubscriptionCount() + 1);
        Subscription sub = new Subscription(subId, url, freq, user);
        monitor.addSubscription(sub);

        System.out.println("Neue Subscription angelegt: " + subId + "\n");
    }

    private static void startMonitoring(WebsiteMonitor monitor) throws InterruptedException {
        // Shutdown-Hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nBeende Monitoring…");
            monitor.shutdown();
            System.out.println("Fertig.");
        }));

        monitor.run();
        System.out.println("\n=== Monitoring läuft für "
            + monitor.getSubscriptionCount()
            + " Subscription(s). Mit STRG+C beenden. ===");

        new CountDownLatch(1).await();
    }
}

