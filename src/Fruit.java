public class Fruit extends Crop implements CropKeeper{
    private final String taste;
    private final double price;
    private CropKeeper keeper;

    public Fruit(String name, double weight, String cultivatedSeason, String taste, double price) {
        super(name, weight, cultivatedSeason);
        this.taste = taste;
        this.price = price;
    }

    @Override
    public String toString(){
        return "Fruit " +
                "Name: '" + getName() + '\'' +
                ", Weight: " + getWeight() +
                ", Season: '" + getCultivatedSeason() + '\'' +
                ", Taste: '" + taste + '\'' +
                ", Price: " + price;
    }

    @Override
    public void consumeIt(){
        System.out.println("Fruit are consumed raw.");
    }

    @Override
    public void storeIt() throws CanNotBeStoredException {
        if (keeper != null) {
            keeper.howToStore();
        } else {
            throw new CanNotBeStoredException("Fruit cannot be stored without a keeper.");
        }
    }

    public int compareTo(Fruit other){
        if(this.getName().equals(other.getName()))
            return 0;
        else
            return (int) (this.getWeight() - other.getWeight());
    }

    @Override
    public void howToStore(){
        System.out.println("Fruits are kept in stores, not listed.");
    }

    public String getTaste() {
        return taste;
    }

    public double getPrice() {
        return price;
    }

    public void setKeeper(CropKeeper keeper){
        this.keeper = keeper;
    }

    public CropKeeper getKeeper() {
        return keeper;
    }

}