/**
 * EVCharger - a real charger that IMPLEMENTS the Chargeable interface.
 *
 * ENCAPSULATION: all fields are private. Other classes can only use
 * the public methods, which check that values are valid.
 */
public class EVCharger implements Chargeable {

    // Private fields: cannot be changed directly from outside the class
    private final int chargerId;
    private final String chargerType;
    private double ratePerKWh;
    private boolean available;

    // Constructor: sets up a charger and validates the values
    public EVCharger(int chargerId, String chargerType, double ratePerKWh, boolean available) {
        if (chargerType == null || chargerType.trim().isEmpty()) {
            throw new IllegalArgumentException("Charger type cannot be empty.");
        }
        validateRate(ratePerKWh);

        this.chargerId = chargerId;
        this.chargerType = chargerType;
        this.ratePerKWh = ratePerKWh;
        this.available = available;
    }

    // Getters: controlled read access to the private fields
    @Override
    public int getChargerId() {
        return chargerId;
    }

    @Override
    public String getChargerType() {
        return chargerType;
    }

    @Override
    public double getRatePerKWh() {
        return ratePerKWh;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    // Controlled update: the rate can never become negative.
    // (Not called by the menu; it shows validated updating of a private field.)
    public void setRatePerKWh(double newRate) {
        validateRate(newRate);
        this.ratePerKWh = newRate;
    }

    // An unavailable charger cannot start a new session.
    @Override
    public boolean startCharging() {
        if (!available) {
            return false;
        }
        available = false; // charger is now occupied
        return true;
    }

    // Session finished: the charger becomes available again.
    @Override
    public void finishCharging() {
        available = true;
    }

    // Total cost = energy required in kWh x rate per kWh
    @Override
    public double calculateCost(double energyKWh) {
        if (energyKWh <= 0 || Double.isNaN(energyKWh) || Double.isInfinite(energyKWh)) {
            throw new IllegalArgumentException("Energy must be a positive number.");
        }
        return energyKWh * ratePerKWh;
    }

    // Private helper: only used inside this class
    private static void validateRate(double rate) {
        if (rate < 0 || Double.isNaN(rate) || Double.isInfinite(rate)) {
            throw new IllegalArgumentException("Charging rate cannot be negative.");
        }
    }
}