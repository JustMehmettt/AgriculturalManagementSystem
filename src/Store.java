import java.util.ArrayList;
import java.util.List;

public class Store implements CropKeeper{
    private final String id;
    private final String name;
    private final double maxCapacityArea;
    private double usedCapacityArea;
    private final double KGperSquareMeter;
    private final List<Fruit> fruitList;

    public Store(String id, String name, double maxCapacityArea) {
        if (!id.startsWith("5"))
            throw new IllegalArgumentException("Store ID must start with 5.");
        this.id = id;
        this.name = name;
        this.maxCapacityArea = maxCapacityArea;
        this.usedCapacityArea = 0;
        this.KGperSquareMeter = 10;
        this.fruitList = new ArrayList<>();
    }

    public void howToStore(){
        System.out.println("Fruits are stored in large refrigerated cooler rooms.");
        System.out.println("Vegetables are kept in sheds, not listed.");
    }

    public double availableCapacity(){
        return getMaxCapacityArea() - getUsedCapacityArea();
    }

    public boolean canBeStored(Fruit f){
        double requiredArea = f.getWeight() / getKGperSquareMeter();
        return availableCapacity() >= requiredArea;
    }

    public void importCrop (Fruit fruit) throws CapacityNotEnoughException{
        if(!canBeStored(fruit))
            throw new CapacityNotEnoughException("Capacity is not enough.");

        Fruit existingFruit = null;
        for (Fruit f : getFruitList()){
            if (f.getName().equals(fruit.getName())) {
                existingFruit = f;
                break;
            }
        }

        if (existingFruit != null){
            double totalWeight = existingFruit.getWeight() + fruit.getWeight();
            Fruit updatedFruit = new Fruit(existingFruit.getName(), totalWeight, existingFruit.getCultivatedSeason(), existingFruit.getTaste(), existingFruit.getPrice());
            getFruitList().remove(existingFruit);
            getFruitList().add(updatedFruit);
        }

        else
            getFruitList().add(fruit);

        setUsedCapacityArea(getUsedCapacityArea() + fruit.getWeight() / getKGperSquareMeter());
    }

    public void exportCrop(String fruitName) throws FruitNotFoundException {
        Fruit existingFruit = null;
        for (Fruit f : getFruitList()) {
            if(f.getName().equalsIgnoreCase(fruitName)) {
                existingFruit = f;
                break;
            }
        }

        if (existingFruit == null)
            throw new FruitNotFoundException("Fruit not found.");

        getFruitList().remove(existingFruit);
        setUsedCapacityArea(getUsedCapacityArea() - existingFruit.getWeight() / getKGperSquareMeter());
        CropFileManager.updateCropsFile(existingFruit, null);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxCapacityArea() {
        return maxCapacityArea;
    }

    public double getUsedCapacityArea() {
        return usedCapacityArea;
    }

    public void setUsedCapacityArea(double usedCapacityArea) {
        this.usedCapacityArea = usedCapacityArea;
    }

    public double getKGperSquareMeter() {
        return KGperSquareMeter;
    }

    public List<Fruit> getFruitList() {
        return fruitList;
    }
}