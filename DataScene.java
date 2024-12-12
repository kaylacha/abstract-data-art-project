import org.code.theater.*;
import org.code.media.*;
import java.util.*;

public class DataScene extends Scene {

    private String[] names;     // The 1D array of names
    private int[] rankings;     // The 1D array of rankings
    private int[] tuitions;     // The 1D array of tuitions
    private int[] acceptances;  // The 1D array of acceptances

    // Arrays for sorting universities
    private String[] expensive;
    private String[] affordable;
    private String[] top20;

    // Counts each category
    private int expensiveIndex = 0;
    private int affordableIndex = 0;
    private int top20Index = 0;

    // Colors for stroke and fill
    private String[] colors = {"yellow", "orange", "blue", "white"};

    /*
     * Constructor initializes the data arrays and prepares for categorization.
     */
    public DataScene() {
        clear("black");

        names = FileReader.toStringArray("names.txt");
        rankings = FileReader.toIntArray("rankings.txt");
        tuitions = FileReader.toIntArray("tuitions.txt");
        acceptances = FileReader.toIntArray("acceptances.txt");

        expensive = new String[names.length];
        affordable = new String[names.length];
        top20 = new String[names.length];
    }

   /**
     * Categorizes universities based on tuition fees and returns a fill color.
     */
    private String sortByCost(int index) {
        int tuition = tuitions[index];
        if (tuition >= 20000) {
            expensive[expensiveIndex] = names[index];
            expensiveIndex++;
            return "red";
        } else if (tuition < 20000) {
            affordable[affordableIndex] = names[index];
            affordableIndex++;
            return "green";
        } else {
            return "blue";
        }
    }

    /*
     * Categorizes universities as top 20 or not and returns a stroke color
     */
    private String sortTop20(int index) {
        int ranking = rankings[index];
        if (ranking >= 20) {
            top20[top20Index] = names[index];
            top20Index++; // Increment counter for top 20
            return "purple"; // Purple for non-top 20
        } else {
            return "pink"; // Pink for top 20 universities
        }
    }

    /*
     * Main visualization method. Iterates through universities,
     * categorizes them, calculates positions, and draws shapes
     */
    public void drawScene() {
        setStrokeWidth(5.0); // Set stroke width for shapes

        for (int i = 0; i < names.length; i++) {
            String fillColor = sortByCost(i);
            setFillColor(fillColor);

            String strokeColor = sortTop20(i);
            setStrokeColor(strokeColor);

            int x = acceptanceBasedXCoord(i);
            int y = (int) Math.round(Math.pow(rankings[i], 2) * 0.2);
            int size = feesBasedSize(i);

            drawRegularPolygon(x, y, 3, size);

            if (fillColor.equals("green")) {
                playNoteAndPause(80, 1);
            } else {
                playNoteAndPause(50, 1);
            }
        }
        setFillColor(colors[(int) (Math.random() * 4)]);
        setStrokeColor(colors[(int) (Math.random() * 4)]);
        drawRegularPolygon(acceptanceBasedXCoord((int) average()), 300, 3, 10);
    }


    /*
     * Calculates x-coordinate based on acceptance rate
     */
    private int acceptanceBasedXCoord(int index) {
        int rate = acceptances[index];
        if (rate > 50) {
            return 10; // High acceptance rate
        } else if (rate <= 50 && rate > 10) {
            return 80; // Medium acceptance rate
        } else if (rate <= 10 && rate > 0) {
            return 160; // Low acceptance rate
        } else {
            return 0; // Default for invalid rates
        }
    }

    /*
     * Calculates the size of the triangle based on tuition fees
     */
    private int feesBasedSize(int index) {
        int tuition = tuitions[index];
        if (tuition > 55000) {
            return 40; // Large size for high fees
        } else if (tuition <= 55000 && tuition > 20000) {
            return 30; // Medium size for medium fees
        } else if (tuition <= 20000 && tuition > 0) {
            return 20; // Small size for low fees
        }
        return 0; // Default size for invalid fees
    }

    /*
     * Calculates the average acceptance rate across all universities
     */
    private double average() {
        double sum = 0;
        for (int i = 0; i < names.length; i++) {
            sum += acceptances[i];
        }
        return sum / 20.0;
    }
}