/**
 * Chargeable - the ABSTRACTION for anything that can charge a vehicle.
 *
 * An interface only says WHAT a charger can do. It does not say HOW.
 * The class that implements it (EVCharger) provides the actual code.
 */
public interface Chargeable {

    // Information a charger can report
    int getChargerId();

    String getChargerType();

    double getRatePerKWh();

    boolean isAvailable();

    // Starts a session. Returns true if started, false if the charger was unavailable.
    boolean startCharging();

    // Ends the session and makes the charger available again.
    void finishCharging();

    // Total cost = energy (kWh) x rate per kWh
    double calculateCost(double energyKWh);
}