public class Vegetable extends Crop implements CropKeeper{
    private final String cultivatedRegion;
    private CropKeeper keeper;

    public Vegetable(String name, double weight, String cultivatedSeason, String cultivatedRegion) {
        super(name, weight, cultivatedSeason);
        this.cultivatedRegion = cultivatedRegion;
    }

    @Override
    public String toString(){
        return "Vegetable " +
                "Name: " + getName() + '\'' +
                ", Weight: " + getWeight() + '\'' +
                ", Season: '" + getCultivatedSeason() + '\'' +
                ", Region: '" + cultivatedRegion + '\'';
    }

    @Override
    public void consumeIt(){
        System.out.println("Vegetables are cooked.");
    }

    @Override
    public void storeIt() throws CanNotBeStoredException {
        throw new CanNotBeStoredException("Vegetables cannot be stored.");
    }

    public int compareTo(Vegetable other){
        if(this.getName().equals(other.getName()))
            return 0;
        else
            return (int) (this.getWeight() - other.getWeight());
    }

    @Override
    public void howToStore(){
        System.out.println("Vegetables are kept in sheds, not listed.");
    }

    public String getCultivatedRegion() {
        return cultivatedRegion;
    }

    public void setKeeper(CropKeeper keeper){
        this.keeper = keeper;
    }
}