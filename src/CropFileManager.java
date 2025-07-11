import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CropFileManager {
    public static void updateCropsFile(Fruit fruit, String newKeeperId) {
        try {
            Path path = Paths.get("src/Crops.txt");
            List<String> lines = Files.readAllLines(path);
            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.split(", ");
                    if (parts[1].equalsIgnoreCase("fruit") &&
                            parts[0].equals(fruit.getName()) &&
                            Double.parseDouble(parts[2]) == fruit.getWeight() &&
                            parts[3].equals(fruit.getCultivatedSeason()) &&
                            parts[4].equals(fruit.getTaste()) &&
                            Double.parseDouble(parts[5]) == fruit.getPrice()) {

                        if (newKeeperId != null) {
                            String updatedLine = parts[0] + ", " + parts[1] + ", " + parts[2] + ", " +
                                    parts[3] + ", " + parts[4] + ", " + parts[5] + ", " + newKeeperId;
                            updatedLines.add(updatedLine);
                        }
                    } else {
                        updatedLines.add(line);
                    }
                }


            Files.write(path, updatedLines);

        } catch (IOException e) {
            System.err.println("Error updating Crops.txt: " + e.getMessage());
        }
    }

    public static void addCropToFile(Crop crop, String keeperId){
        try {
            Path path = Paths.get("src/Crops.txt");
            List<String> lines = Files.readAllLines(path);
            List<String> updatedLines = new ArrayList<>(lines);


            if (crop instanceof Fruit fruit) {
                String fruitLine = fruit.getName() + ", fruit, " + fruit.getWeight() + ", " +
                        fruit.getCultivatedSeason() + ", " + fruit.getTaste() + ", " +
                        fruit.getPrice() + ", " + keeperId;
                updatedLines.add(fruitLine);
            } else if (crop instanceof Vegetable vegetable) {
                String vegetableLine = vegetable.getName() + ", vegetable, " + vegetable.getWeight() + ", "
                        + vegetable.getCultivatedRegion() + ", "  + keeperId;
                updatedLines.add(vegetableLine);
            }

            Files.write(path, updatedLines);


        } catch (IOException e) {
            System.err.println("Error adding crop to Crops.txt: " + e.getMessage());
        }
    }
}