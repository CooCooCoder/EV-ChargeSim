import java.util.Scanner;

/**
 * Main - the console menu. It reads input, validates it,
 * and asks ChargingStation to do the work.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ChargingStation station = new ChargingStation("EV ChargeSim Station");

        System.out.println("Welcome to " + station.getStationName());
        System.out.println("(Simulation only. Rates are illustrative, not real tariffs.)");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine("Enter your choice (1-4): ");

            if (choice == null) { // input stream closed
                break;
            }

            switch (choice) {
                case "1":
                    station.displayChargers();
                    break;
                case "2":
                    handleStartSession(station);
                    break;
                case "3":
                    handleEndSession(station);
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, 3 or 4.");
            }
        }

        System.out.println();
        System.out.println("Thank you for using EV ChargeSim. Goodbye!");
        scanner.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("========== EV ChargeSim MENU ==========");
        System.out.println("1. View chargers");
        System.out.println("2. Start a charging session");
        System.out.println("3. End a charging session");
        System.out.println("4. Exit");
        System.out.println("=======================================");
    }

    // Shows a prompt and returns the trimmed line, or null if there is no more input
    private static String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine().trim();
    }

    private static void handleStartSession(ChargingStation station) {
        station.displayChargers();
        System.out.println();

        // Step 1: read and check the charger ID
        String idText = readLine("Enter charger ID: ");
        if (idText == null) {
            return;
        }

        int chargerId;
        try {
            chargerId = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            System.out.println("Invalid charger ID. Please enter a whole number.");
            return;
        }

        Chargeable charger = station.findChargerById(chargerId);
        if (charger == null) {
            System.out.println("Charger " + chargerId + " does not exist.");
            return;
        }
        if (!charger.isAvailable()) {
            System.out.println("Charger " + chargerId + " is currently unavailable. Please choose another charger.");
            return;
        }

        // Step 2: read and check the energy value
        String energyText = readLine("Enter energy required in kWh: ");
        if (energyText == null) {
            return;
        }

        double energyKWh;
        try {
            energyKWh = Double.parseDouble(energyText);
        } catch (NumberFormatException e) {
            System.out.println("Invalid energy value. Please enter a number such as 20 or 15.5.");
            return;
        }

        if (Double.isNaN(energyKWh) || Double.isInfinite(energyKWh)) {
            System.out.println("Invalid energy value. Please enter a normal number.");
            return;
        }
        if (energyKWh == 0) {
            System.out.println("Energy must be greater than zero.");
            return;
        }
        if (energyKWh < 0) {
            System.out.println("Energy cannot be negative.");
            return;
        }

        // Step 3: start the session
        station.startSession(charger, energyKWh);
    }

    private static void handleEndSession(ChargingStation station) {
        String idText = readLine("Enter the ID of the charger whose session has finished: ");
        if (idText == null) {
            return;
        }

        int chargerId;
        try {
            chargerId = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            System.out.println("Invalid charger ID. Please enter a whole number.");
            return;
        }

        station.endSession(chargerId);
    }
}