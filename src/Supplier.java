import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.*;

public class Supplier implements CropKeeper {
    private final String name;
    private final String id;
    private double budget;
    private final List<Crop> cropList;

    public Supplier(String name, String id, double budget) {
        if (!id.startsWith("1"))
            throw new IllegalArgumentException("Supplier ID must start with 1.");
        this.name = name;
        this.id = id;
        this.budget = budget;
        this.cropList = new ArrayList<>();
    }

    @Override
    public void howToStore() {
        System.out.println("Vegetables are stored in sheds, not listed.");
        System.out.println("Fruits are stored in large refrigerated cooler rooms.");
    }

    private void updateCropsFile(Fruit fruit, String newKeeperId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/Crops.txt"));
            List<String> updatedLines = new ArrayList<>(lines);
            for (int i = 0; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(", ");

                if (parts[1].equalsIgnoreCase("fruit") &&
                    parts[0].equals(fruit.getName()) &&
                    Double.parseDouble(parts[2]) == fruit.getWeight() && 
                    parts[3].equals(fruit.getCultivatedSeason()) && 
                    parts[4].equals(fruit.getTaste()) && 
                    Double.parseDouble(parts[5]) == fruit.getPrice()) {

                    String updatedLine = parts[0] + ", " + parts[1] + ", " + parts[2] + ", " +
                                         parts[3] + ", " + parts[4] + ", " + parts[5] + ", " + newKeeperId;
                    
                    updatedLines.set(i, updatedLine);
                    break;
                }
            }

            Files.write(Paths.get("src/Crops.txt"), updatedLines);

        } catch (IOException e) {
            System.err.println("Error updating Crops.txt: " + e.getMessage());
        }
    }

    public void buyCrop(Crop c) throws SupplierHasNotEnoughMoneyException {
        if (c instanceof Fruit fruit) {
            if (fruit.getPrice() > budget) {
                throw new SupplierHasNotEnoughMoneyException("Supplier has not enough money.");
            }

            budget -= fruit.getPrice();
            cropList.add(fruit);
            fruit.setKeeper(this);
            updateCropsFile(fruit, this.id);
        }
    }

    public void sellCrop(Crop c) throws FruitNotFoundException, CanNotBeStoredException {
        if (!cropList.contains(c)) {
            throw new FruitNotFoundException("This crop is not available in supplier's list.");
        }

        if (c instanceof Fruit fruit) {
            cropList.remove(fruit);
            budget += fruit.getPrice();
            fruit.storeIt();

            CropKeeper newKeeper = fruit.getKeeper();
            if (!(newKeeper instanceof Store)) {
                throw new CanNotBeStoredException("Fruit must be assigned to a store.");
            } else {
                String storeId = ((Store) newKeeper).getId();
                updateCropsFile(fruit, storeId);
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public double getBudget() {
        return budget;
    }

    public List<Crop> getCropList() {
        return cropList;
    }
}