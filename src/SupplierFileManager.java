import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SupplierFileManager {
    public static void updateSupplierFile(Supplier supplier, double newBudget) {
        try {
            Path path = Paths.get("src/Suppliers.txt");
            List<String> lines = Files.readAllLines(path);
            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.split(", ");
                if (parts[0].equals(supplier.getName()) && parts[1].equals(supplier.getId())) {

                    String updatedLine = supplier.getName() + ", " + supplier.getId() + ", " + newBudget;
                    updatedLines.add(updatedLine);
                } else {
                    updatedLines.add(line);
                }
            }
            Files.write(path, updatedLines);
        } catch (IOException e) {
            System.err.println("Error updating Suppliers.txt: " + e.getMessage());
        }
    }

}
