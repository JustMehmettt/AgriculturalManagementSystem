public abstract class Crop {
    private final String name;
    private final double weight;
    private final String cultivatedSeason;

    Crop(String name, double weight, String cultivatedSeason) {
        this.name = name;
        this.weight = weight;
        this.cultivatedSeason = cultivatedSeason;
    }

    public String getName() {
        return name;
    }
    public double getWeight() {
        return weight;
    }
    public String getCultivatedSeason() {
        return cultivatedSeason;
    }

    public abstract String toString();
    public abstract void consumeIt();
    public abstract void storeIt() throws CanNotBeStoredException;
}
