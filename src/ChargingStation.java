import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * ChargingStation - manages the chargers and the charging workflow.
 *
 * Notice: the list holds Chargeable (the interface), not EVCharger.
 * The station only uses what the interface promises.
 */
public class ChargingStation {

    private final String stationName;
    private final List<Chargeable> chargers;

    public ChargingStation(String stationName) {
        this.stationName = stationName;
        this.chargers = new ArrayList<>();
        loadSampleChargers();
    }

    // Sample chargers. Rates are ILLUSTRATIVE values, not real tariffs.
    private void loadSampleChargers() {
        chargers.add(new EVCharger(1, "AC Slow", 8.00, true));
        chargers.add(new EVCharger(2, "AC Fast", 12.50, true));
        chargers.add(new EVCharger(3, "DC Fast", 18.00, false)); // occupied by another vehicle
        chargers.add(new EVCharger(4, "DC Ultra-Fast", 24.00, true));
    }

    public String getStationName() {
        return stationName;
    }

    // Returns the charger with this ID, or null if it does not exist
    public Chargeable findChargerById(int chargerId) {
        for (Chargeable charger : chargers) {
            if (charger.getChargerId() == chargerId) {
                return charger;
            }
        }
        return null;
    }

    // Prints all chargers in a table
    public void displayChargers() {
        System.out.println();
        System.out.println("=== " + stationName + " - Chargers (rates are illustrative) ===");
        System.out.printf(Locale.US, "%-4s%-18s%-16s%s%n", "ID", "Type", "Rate (Rs./kWh)", "Status");
        System.out.println("--------------------------------------------------");
        for (Chargeable charger : chargers) {
            String status = charger.isAvailable() ? "Available" : "Unavailable";
            System.out.printf(Locale.US, "%-4d%-18s%-16.2f%s%n",
                    charger.getChargerId(),
                    charger.getChargerType(),
                    charger.getRatePerKWh(),
                    status);
        }
    }

    // Starts a session on the given charger. Returns true if it started.
    public boolean startSession(Chargeable charger, double energyKWh) {
        // Calculate the cost first, so nothing changes if the energy value is invalid
        double totalCost = charger.calculateCost(energyKWh);

        if (!charger.startCharging()) {
            System.out.println("Charger " + charger.getChargerId() + " is unavailable. Session not started.");
            return false;
        }

        printSessionSummary(charger, energyKWh, totalCost);
        return true;
    }

    // Ends a session and frees the charger. Returns true if a charger was freed.
    public boolean endSession(int chargerId) {
        Chargeable charger = findChargerById(chargerId);

        if (charger == null) {
            System.out.println("Charger " + chargerId + " does not exist.");
            return false;
        }
        if (charger.isAvailable()) {
            System.out.println("Charger " + chargerId + " is already available. There is no session to end.");
            return false;
        }

        charger.finishCharging();
        System.out.println("Charging session on charger " + chargerId + " has finished.");
        System.out.println("Charger " + chargerId + " is now AVAILABLE again.");
        return true;
    }

    private void printSessionSummary(Chargeable charger, double energyKWh, double totalCost) {
        System.out.println();
        System.out.println("========== CHARGING SESSION SUMMARY ==========");
        System.out.println("Charger ID       : " + charger.getChargerId());
        System.out.println("Charger Type     : " + charger.getChargerType());
        System.out.printf(Locale.US, "Energy Requested : %.2f kWh%n", energyKWh);
        System.out.printf(Locale.US, "Rate per kWh     : Rs. %.2f%n", charger.getRatePerKWh());
        System.out.printf(Locale.US, "Total Cost       : Rs. %.2f%n", totalCost);
        System.out.println("Session Status   : IN PROGRESS (charger is now occupied)");
        System.out.println("==============================================");
        System.out.println("The charger becomes available again when you choose");
        System.out.println("option 3 (End a charging session).");
    }
}