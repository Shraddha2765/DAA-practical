import java.util.*;

/**
 * Fractional knapsack (simple) supporting divisible and indivisible items.
 *
 * Note: If some items are indivisible, this greedy method is a heuristic.
 * For strict optimality in mixed (0/1 + fractional) cases you would need a
 * more advanced method (DP or Branch & Bound using fractional relaxation).
 */
public class FractionalKnapsackSimple {

    static class Item {
        String name;
        double weight;
        double value;
        boolean divisible; // true => fractional allowed
        double ratio;      // value / weight

        Item(String name, double weight, double value, boolean divisible) {
            this.name = name;
            this.weight = weight;
            this.value = value;
            this.divisible = divisible;
            this.ratio = value / weight;
        }
    }

    static class Taken {
        String name;
        double takenWeight;
        double valueGained;
        double fraction; // 1.0 => whole, <1 => fraction taken

        Taken(String name, double takenWeight, double valueGained, double fraction) {
            this.name = name;
            this.takenWeight = takenWeight;
            this.valueGained = valueGained;
            this.fraction = fraction;
        }
    }

    // Greedy fractional solver (simple)
    public static List<Taken> solveFractional(List<Item> items, double capacity) {
        // sort by ratio descending
        items.sort((a, b) -> Double.compare(b.ratio, a.ratio));

        double remaining = capacity;
        List<Taken> takenList = new ArrayList<>();

        for (Item it : items) {
            if (remaining <= 1e-9) break; // boat full

            if (it.divisible) {
                if (it.weight <= remaining) {
                    // take full
                    takenList.add(new Taken(it.name, it.weight, it.value, 1.0));
                    remaining -= it.weight;
                } else {
                    // take fraction
                    double frac = remaining / it.weight;
                    double val = it.value * frac;
                    takenList.add(new Taken(it.name, remaining, val, frac));
                    remaining = 0;
                    break;
                }
            } else {
                // indivisible item: only take if it fits fully
                if (it.weight <= remaining) {
                    takenList.add(new Taken(it.name, it.weight, it.value, 1.0));
                    remaining -= it.weight;
                } else {
                    // skip it
                }
            }
        }
        return takenList;
    }

    // Utility to print results
    public static void printResult(List<Taken> taken, double capacity) {
        double totalWeight = 0;
        double totalValue = 0;
        System.out.println("Items taken:");
        for (Taken t : taken) {
            System.out.printf(" - %s : weight=%.3f, value=%.3f, fraction=%.3f\n",
                    t.name, t.takenWeight, t.valueGained, t.fraction);
            totalWeight += t.takenWeight;
            totalValue += t.valueGained;
        }
        System.out.printf("Total weight used: %.3f / %.3f\n", totalWeight, capacity);
        System.out.printf("Total utility value: %.3f\n", totalValue);
    }

    // Demo example
    public static void main(String[] args) {
        double W = 50.0; // boat capacity in kg

        List<Item> items = new ArrayList<>();
        // name, weight, value (utility), divisible?
        items.add(new Item("MedicineKit", 15, 90, false)); // indivisible, high value
        items.add(new Item("Water", 20, 60, true));        // divisible
        items.add(new Item("FoodPackets", 30, 90, true));  // divisible
        items.add(new Item("Blankets", 10, 20, false));    // indivisible
        items.add(new Item("Fuel", 5, 8, true));           // divisible

        List<Taken> result = solveFractional(items, W);
        printResult(result, W);
    }
}





