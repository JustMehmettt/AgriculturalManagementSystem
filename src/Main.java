import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final List<Crop> crops = new ArrayList<>();
    private static final List<Store> stores = new ArrayList<>();
    private static final List<Supplier> suppliers = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args){
        loadDataFromFiles();
        int choice;
        do{
            displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> displaySuppliers();
                case 2 -> displayStores();
                case 3 -> buyFruitForSupplier();
                case 4 -> sellFruitFromSupplier();
                case 5 -> removeFruitFromStore();
                case 6 -> removeCropFromSupplier();
                case 7 -> addNewCrop();
                case 8 -> showRemainingBudget();
                case 9 -> showRemainingCapacity();
                case 0 -> System.out.println("Exiting program...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private static void displayMenu(){
        System.out.println("\nPlease make a choice(0 to 9):");
        System.out.println("1) Display all suppliers and their crop list");
        System.out.println("2) Display all stores and their fruit list");
        System.out.println("3) Buy a fruit crop for a Supplier");
        System.out.println("4) Sell a fruit crop of a Supplier");
        System.out.println("5) Remove a fruit from a store");
        System.out.println("6) Remove a crop from a supplier");
        System.out.println("7) Add crop");
        System.out.println("8) Show remaining budget");
        System.out.println("9) Show remaining capacity");
        System.out.println("0) Quit");
        System.out.print("Enter your choice: ");
    }

    private static void loadDataFromFiles(){
        try {
            loadStores();
            loadSuppliers();
            loadCrops();
        } catch (IOException e) {
            System.err.println("Error while loading data from files. " + e.getMessage());
            System.exit(1);
        }
    }

    private static void loadStores() throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Stores.txt"))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] parts = line.split(", ");
                Store store = new Store (parts[1], parts[0], Double.parseDouble(parts[2]));
                stores.add(store);
            }
        }
    }

    private static void loadSuppliers() throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Suppliers.txt"))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] parts = line.split(", ");
                Supplier supplier = new Supplier (parts[0], parts[1], Double.parseDouble(parts[2]));
                suppliers.add(supplier);
            }
        }
    }

    private static void loadCrops() throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Crops.txt"))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] parts = line.split(", ");
                if (parts[1].equalsIgnoreCase("fruit")){
                    Fruit fruit = new Fruit (parts[0], Double.parseDouble(parts[2]), parts[3], parts[4], Double.parseDouble(parts[5]));
                    crops.add(fruit);
                    String keeperId = parts[6];
                    assignCropToKeeper(fruit, keeperId);
                } else if (parts[1].equalsIgnoreCase("vegetable")) {
                    Vegetable vegetable = new Vegetable(parts[0], Double.parseDouble(parts[2]), parts[3], parts[4]);
                    crops.add(vegetable);
                }
            }
        }
    }

    private static void assignCropToKeeper(Fruit fruit, String keeperId){
        if (keeperId.startsWith("1")){
            for(Supplier supplier : suppliers){
                if(supplier.getId().equals(keeperId)){
                    try {
                        supplier.buyCrop(fruit);
                    } catch (SupplierHasNotEnoughMoneyException e){
                        System.err.println("Error assigning fruit to supplier: " + e.getMessage());
                    } break;
                }
            }
        }
        else if (keeperId.startsWith("5")){
            for (Store store : stores){
                if(store.getId().equals(keeperId)){
                    try {
                        store.importCrop(fruit);
                    } catch (CapacityNotEnoughException e){
                        System.err.println("Error assigning fruit to store: " + e.getMessage());
                    } break;
                }
            }
        }
    }

    private static void displaySuppliers(){
        System.out.println("\nSuppliers:");
        for (Supplier supplier : suppliers){
            System.out.println("\nSupplier " + supplier.getName() + " ID: " + supplier.getId());
            System.out.println("Budget: " + supplier.getBudget());
            System.out.println("Crops:");
            for (Crop crop : supplier.getCropList()){
                System.out.println(crop);
            }
            supplier.howToStore();
            System.out.println();
        }
    }

    private static void displayStores(){
        System.out.println("\nStores:");
        for (Store store : stores){
            System.out.println("\nStore " + store.getName() + " ID: " + store.getId());
            System.out.println("Max capacity area: " + store.getMaxCapacityArea());
            System.out.println("Used capacity area: " + store.getUsedCapacityArea());
            System.out.println("KG per square meter: " + store.getKGperSquareMeter());
            System.out.println("Fruits:");
            for (Fruit fruit : store.getFruitList()){
                System.out.println(fruit);
            }
        }
    }

    private static void buyFruitForSupplier(){
        System.out.println("\nAvailable stores:");
        for(Store store : stores){
            System.out.println(store.getName() + " ID: " + store.getId());
        }
        System.out.print("\nEnter store ID: ");
        String storeId = scanner.nextLine();

        Store selectedStore = null;
        for (Store store : stores){
            if(store.getId().equals(storeId)){
                selectedStore = store;
                break;
            }
        }

        if (selectedStore == null){
            System.out.println("Store not found.");
            return;
        }

        System.out.println("\nAvailable fruits in " + selectedStore.getName());
        for (Fruit fruit : selectedStore.getFruitList()){
            System.out.println(fruit.toString());
        }

        System.out.print("\nEnter fruit name to buy: ");
        String fruitName = scanner.nextLine();

        Fruit selectedFruit = null;
        for (Fruit fruit : selectedStore.getFruitList()){
            if(fruit.getName().equals(fruitName)){
                selectedFruit = fruit;
                break;
            }
        }

        if (selectedFruit == null){
            System.out.println("Fruit not found.");
            return;
        }

        System.out.println("\nAvailable suppliers:");

        for (Supplier supplier : suppliers)
            System.out.println(supplier.getName() + " ID: " + supplier.getId());

        System.out.print("\nEnter supplier ID who will buy this fruit: ");
        String supplierId = scanner.nextLine();

        Supplier selectedSupplier = null;
        for (Supplier supplier : suppliers){
            if(supplier.getId().equals(supplierId)){
                selectedSupplier = supplier;
                break;
            }
        }

        if (selectedSupplier == null){
            System.out.println("Supplier not found.");
            return;
        }

        try {
            selectedSupplier.buyCrop(selectedFruit);
            selectedStore.getFruitList().remove(selectedFruit);
            double areaToFree = selectedFruit.getWeight() / selectedStore.getKGperSquareMeter();
            selectedStore.setUsedCapacityArea(selectedStore.getUsedCapacityArea() - areaToFree);

            System.out.println("Fruit successfully bought.");
        } catch (SupplierHasNotEnoughMoneyException e){
            System.err.println("Error buying fruit: " + e.getMessage());
        }
    }

    private static void sellFruitFromSupplier() {
        System.out.println("\nAvailable suppliers:");
        for (Supplier supplier : suppliers) {
            System.out.println("\n" + supplier.getName() + " ID: " + supplier.getId());
            System.out.println("Crops:");
            for (Crop crop : supplier.getCropList()) {
                if (crop instanceof Fruit) {
                    System.out.println(crop.toString());
                }
            }
        }

        System.out.print("\nEnter supplier ID who will sell this fruit: ");
        String supplierId = scanner.nextLine();

        Supplier selectedSupplier = null;
        for (Supplier supplier : suppliers) {
            if (supplier.getId().equals(supplierId)) {
                selectedSupplier = supplier;
                break;
            }
        }

        if (selectedSupplier == null) {
            System.out.println("Supplier not found.");
            return;
        }

        System.out.print("\nEnter fruit name to sell: ");
        String fruitName = scanner.nextLine();

        Fruit selectedFruit = null;
        for (Crop crop : selectedSupplier.getCropList()) {
            if (crop instanceof Fruit && crop.getName().equals(fruitName)) {
                selectedFruit = (Fruit) crop;
                break;
            }
        }

        if (selectedFruit == null) {
            System.out.println("Fruit not found in supplier's list.");
            return;
        }

        System.out.print("\nAvailable stores: \n");
        for (Store store : stores)
            System.out.println(store.getName() + " ID: " + store.getId());

        System.out.print("\nEnter store ID who will sell the fruit: ");
        String storeId = scanner.nextLine();

        Store selectedStore = null;
        for (Store store : stores) {
            if (store.getId().equals(storeId)) {
                selectedStore = store;
                break;
            }
        }

        if (selectedStore == null) {
            System.out.println("Store not found.");
            return;
        }

        try {
            selectedFruit.setKeeper(selectedStore);
            selectedSupplier.sellCrop(selectedFruit);
            selectedStore.importCrop(selectedFruit);
            System.out.println("Fruit successfully sold.");
        } catch (FruitNotFoundException | CanNotBeStoredException | CapacityNotEnoughException e) {
            System.err.println("Error selling fruit: " + e.getMessage());
        }
    }

    public static void removeFruitFromStore(){
        System.out.println("\nAvailable stores:");
        for (Store store : stores){
            System.out.println("\n" + store.getName() + " ID: " + store.getId());
            System.out.println("Fruits:");
            for (Fruit fruit : store.getFruitList()){
                System.out.println(fruit.toString());
            }
        }

        System.out.print("\nEnter store ID: ");
        String storeId = scanner.nextLine();

        Store selectedStore = null;
        for (Store store : stores){
            if(store.getId().equals(storeId)){
                selectedStore = store;
                break;
            }
        }

        if (selectedStore == null){
            System.out.println("Store not found.");
            return;
        }

        System.out.print("\nEnter fruit name to remove: ");
        String fruitName = scanner.nextLine();

        try {
            selectedStore.exportCrop(fruitName);
            System.out.println("Fruit successfully removed.");
        } catch (FruitNotFoundException e){
            System.err.println("Error removing fruit: " + e.getMessage());
        }
    }

    public static void removeCropFromSupplier(){
        System.out.println("\nAvailable suppliers:");
        for (Supplier supplier : suppliers){
            System.out.println("\n" + supplier.getName() + " ID: " + supplier.getId());
            System.out.println("Crops:");
            for (Crop crop : supplier.getCropList()){
                System.out.println(crop.toString());
            }
        }

        System.out.print("\nEnter supplier ID who will remove this crop: ");
        String supplierId = scanner.nextLine();

        Supplier selectedSupplier = null;
        for (Supplier supplier : suppliers){
            if(supplier.getId().equals(supplierId)){
                selectedSupplier = supplier;
                break;
            }
        }

        if (selectedSupplier == null){
            System.out.println("Supplier not found.");
            return;
        }

        System.out.print("\nEnter crop name to remove: ");
        String cropName = scanner.nextLine();

        Crop cropToRemove = null;
        for (Crop crop : selectedSupplier.getCropList()){
            if (crop.getName().equals(cropName)){
                cropToRemove = crop;
                break;
            }
        }

        if (cropToRemove == null){
            System.out.println("Crop not found.");
            return;
        }

        selectedSupplier.getCropList().remove(cropToRemove);
        CropFileManager.updateCropsFile((Fruit) cropToRemove, null);
        System.out.println("Crop successfully removed.");
    }

    private static void addNewCrop() {
        System.out.println("\nAdd new crop:");
        System.out.println("1) Add Fruit");
        System.out.println("2) Add Vegetable");
        System.out.print("Enter choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter weight (kg): ");
        double weight = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter cultivated season: ");
        String season = scanner.nextLine();

        System.out.println("\nWhere to add the crop?");
        System.out.println("1) Add to Supplier");
        System.out.println("2) Add to Store");
        System.out.print("Enter choice (1 or 2): ");
        int keeperChoice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) { // Adding Fruit
            System.out.print("Enter taste: ");
            String taste = scanner.nextLine();

            System.out.print("Enter price: ");
            double price = scanner.nextDouble();
            scanner.nextLine();

            Fruit newFruit = new Fruit(name, weight, season, taste, price);

            if (keeperChoice == 1) { // Adding to supplier
                System.out.println("\nAvailable suppliers:");
                for (Supplier supplier : suppliers)
                    System.out.println(supplier.getName() + " ID: " + supplier.getId());

                System.out.print("\nEnter supplier ID: ");
                String supplierId = scanner.nextLine();

                for (Supplier supplier : suppliers) {
                    if (supplier.getId().equals(supplierId)) {
                        try {
                            supplier.buyCrop(newFruit);
                            CropFileManager.addCropToFile(newFruit, supplier.getId());
                            System.out.println("Fruit successfully added to supplier.");
                        } catch (SupplierHasNotEnoughMoneyException e) {
                            System.err.println("Error adding fruit: " + e.getMessage());
                        }
                        return;
                    }
                }
                System.out.println("Supplier not found.");
            } else if (keeperChoice == 2) { // Adding to store
                System.out.println("\nAvailable stores:");
                for (Store store : stores)
                    System.out.println(store.getName() + " ID: " + store.getId());


                System.out.print("\nEnter store ID: ");
                String storeId = scanner.nextLine();

                for (Store store : stores) {
                    if (store.getId().equals(storeId)) {
                        try {
                            store.importCrop(newFruit);
                            CropFileManager.addCropToFile(newFruit, store.getId());
                            System.out.println("Fruit successfully added to store.");
                        } catch (CapacityNotEnoughException e) {
                            System.err.println("Error adding fruit: " + e.getMessage());
                        }
                        return;
                    }
                }
                System.out.println("Store not found.");
            }
        } else if (choice == 2) { // Adding Vegetable
            System.out.print("Enter cultivated region: ");
            String region = scanner.nextLine();

            Vegetable newVegetable = new Vegetable(name, weight, season, region);

            if (keeperChoice == 1) {
                System.out.println("\nAvailable suppliers:");
                for (Supplier supplier : suppliers) {
                    System.out.println(supplier.getName() + " ID: " + supplier.getId());
                }

                System.out.print("\nEnter supplier ID: ");
                String supplierId = scanner.nextLine();

                for (Supplier supplier : suppliers) {
                    if (supplier.getId().equals(supplierId)) {
                        supplier.getCropList().add(newVegetable);
                        System.out.println("Vegetable successfully added to supplier.");
                        return;
                    }
                }
                System.out.println("Supplier not found.");
            } else {
                System.out.println("Vegetables can only be added to suppliers.");
            }
        }

    }

    private static void showRemainingBudget() {
        System.out.println("\nRemaining suppliers budget:");
        for (Supplier supplier : suppliers){
            System.out.println(supplier.getName() + " ID: " + supplier.getId() + "\nBudget: " + supplier.getBudget() + "\n");
        }
    }

    private static void showRemainingCapacity(){
        System.out.println("\nRemaining stores capacity:");
        for (Store store : stores){
            System.out.println(store.getName() + " ID: " + store.getId() + "\nMax capacity area: " + store.getMaxCapacityArea() + "\nUsed capacity area: " + store.getUsedCapacityArea() + "\n");
        }
    }

}