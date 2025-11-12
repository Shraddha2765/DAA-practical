import java.util.*;

public class DisasterReliefKnapsack {

    static class Item {
        String name;
        int weight;
        int utility;   // importance or usefulness in disaster relief
        boolean perishable; // true = higher priority (like medicine/food)
        
        Item(String name, int weight, int utility, boolean perishable) {
            this.name = name;
            this.weight = weight;
            this.utility = utility;
            this.perishable = perishable;
        }
    }

    static class Result {
        int maxUtility;
        List<Item> chosenItems;
        Result(int maxUtility, List<Item> chosenItems) {
            this.maxUtility = maxUtility;
            this.chosenItems = chosenItems;
        }
    }

    // Dynamic Programming 0/1 Knapsack Solver
    static Result solveKnapsack(Item[] items, int W) {
        int n = items.length;
        int[][] dp = new int[n + 1][W + 1];

        // DP Table Filling
        for (int i = 1; i <= n; i++) {
            int wt = items[i - 1].weight;
            int val = items[i - 1].utility;
            for (int w = 0; w <= W; w++) {
                if (wt <= w)
                    dp[i][w] = Math.max(val + dp[i - 1][w - wt], dp[i - 1][w]);
                else
                    dp[i][w] = dp[i - 1][w];
            }
        }

        // Reconstruct chosen items
        List<Item> chosen = new ArrayList<>();
        int w = W;
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                chosen.add(items[i - 1]);
                w -= items[i - 1].weight;
            }
        }
        Collections.reverse(chosen);

        return new Result(dp[n][W], chosen);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter truck capacity (kg): ");
        int W = sc.nextInt();

        // Sample data (You can modify or input manually)
        Item[] items = {
            new Item("Medicines", 2, 20, true),
            new Item("Food Packets", 5, 70, true),
            new Item("Blankets", 4, 60, false),
            new Item("Water Bottles", 3, 40, true),
            new Item("Tents", 6, 90, false)
        };

        // 🔹 Optional Priority Adjustment: Give perishable items slight bonus
        // (This helps prioritize medicines and food in real-world scenario)
        for (Item item : items) {
            if (item.perishable)
                item.utility += 10; // small boost to priority
        }

        // Solve using Dynamic Programming
        Result res = solveKnapsack(items, W);

        System.out.println("\n✅ Optimal Utility Value = " + res.maxUtility);
        System.out.println("🚚 Items loaded into truck:");
        for (Item item : res.chosenItems)
            System.out.println("  - " + item.name + " (Weight: " + item.weight + " kg, Utility: " + item.utility + ")");

        sc.close();
    }
}


