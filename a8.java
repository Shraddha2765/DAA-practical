import java.util.*;

// SwiftShip: Optimize delivery routes using Least Cost Branch and Bound
public class SwiftShipBranchBound {

    static final int INF = 999999; // Represent infinity (no edge)

    // Node class to represent each partial route in the state-space tree
    static class Node {
        int[][] reducedMatrix; // Cost matrix after reduction
        List<Integer> path;    // Sequence of cities visited
        int costSoFar;         // Total cost so far
        int bound;             // Lower bound (costSoFar + reduction)
        int level;             // How many cities visited so far
        int currentCity;       // The last city visited

        Node(int n) {
            reducedMatrix = new int[n][n];
            path = new ArrayList<>();
        }
    }

    // Function to make a deep copy of matrix
    static int[][] copyMatrix(int[][] mat) {
        int n = mat.length;
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++)
            copy[i] = Arrays.copyOf(mat[i], n);
        return copy;
    }

    // Row and Column reduction function (returns reduction cost)
    static int reduceMatrix(int[][] mat) {
        int n = mat.length;
        int reduction = 0;

        // Row reduction
        for (int i = 0; i < n; i++) {
            int rowMin = INF;
            for (int j = 0; j < n; j++)
                rowMin = Math.min(rowMin, mat[i][j]);

            if (rowMin != INF && rowMin > 0) {
                reduction += rowMin;
                for (int j = 0; j < n; j++)
                    if (mat[i][j] != INF)
                        mat[i][j] -= rowMin;
            }
        }

        // Column reduction
        for (int j = 0; j < n; j++) {
            int colMin = INF;
            for (int i = 0; i < n; i++)
                colMin = Math.min(colMin, mat[i][j]);

            if (colMin != INF && colMin > 0) {
                reduction += colMin;
                for (int i = 0; i < n; i++)
                    if (mat[i][j] != INF)
                        mat[i][j] -= colMin;
            }
        }

        return reduction;
    }

    // Create a new child node from a parent node after visiting nextCity
    static Node createChild(Node parent, int currentCity, int nextCity, int[][] costMatrix) {
        int n = costMatrix.length;

        Node child = new Node(n);
        child.reducedMatrix = copyMatrix(parent.reducedMatrix);

        // Set row of currentCity and column of nextCity to infinity
        for (int j = 0; j < n; j++)
            child.reducedMatrix[currentCity][j] = INF;
        for (int i = 0; i < n; i++)
            child.reducedMatrix[i][nextCity] = INF;

        // Also, prevent coming back to start before visiting all
        child.reducedMatrix[nextCity][0] = INF;

        // Calculate cost so far
        child.costSoFar = parent.costSoFar + costMatrix[currentCity][nextCity];

        // Reduce the new matrix to get reduction cost
        int reductionCost = reduceMatrix(child.reducedMatrix);

        // Calculate total lower bound for this child
        child.bound = child.costSoFar + reductionCost;

        // Copy and update path
        child.path = new ArrayList<>(parent.path);
        child.path.add(nextCity);

        child.level = parent.level + 1;
        child.currentCity = nextCity;

        return child;
    }

    // Solve the TSP using Least-Cost Branch and Bound
    static void solveTSP(int[][] costMatrix) {
        int n = costMatrix.length;

        // Create the root node
        Node root = new Node(n);
        root.reducedMatrix = copyMatrix(costMatrix);
        root.costSoFar = 0;
        root.path.add(0); // start from city 0
        root.level = 0;
        root.currentCity = 0;

        // Initial reduction to get the first lower bound
        int reductionCost = reduceMatrix(root.reducedMatrix);
        root.bound = reductionCost;

        // Priority Queue (min-heap) based on node bound
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.bound));
        pq.add(root);

        int bestCost = INF;
        List<Integer> bestPath = null;

        // Branch and Bound loop
        while (!pq.isEmpty()) {
            Node current = pq.poll();

            // Skip if bound already worse than best found
            if (current.bound >= bestCost)
                continue;

            // If all cities are visited, complete the tour
            if (current.level == n - 1) {
                int last = current.currentCity;
                int finalCost = current.costSoFar + costMatrix[last][0]; // return to start
                if (finalCost < bestCost) {
                    bestCost = finalCost;
                    bestPath = new ArrayList<>(current.path);
                    bestPath.add(0);
                }
                continue;
            }

            // Branch to all possible next cities
            for (int nextCity = 0; nextCity < n; nextCity++) {
                if (!current.path.contains(nextCity) && current.reducedMatrix[current.currentCity][nextCity] != INF) {
                    Node child = createChild(current, current.currentCity, nextCity, costMatrix);
                    if (child.bound < bestCost) {
                        pq.add(child);
                    }
                }
            }
        }

        // Print the best result
        System.out.println("\nOptimal Route Found:");
        for (int i = 0; i < bestPath.size(); i++) {
            System.out.print(bestPath.get(i));
            if (i < bestPath.size() - 1)
                System.out.print(" → ");
        }
        System.out.println("\nMinimum Cost = " + bestCost);
    }

    // ------------------ MAIN ---------------------
    public static void main(String[] args) {
        // Example cost matrix (distance × fuel cost)
        int[][] costMatrix = {
                {INF, 10, 15, 20},
                {10, INF, 35, 25},
                {15, 35, INF, 30},
                {20, 25, 30, INF}
        };

        System.out.println("SwiftShip — Delivery Route Optimization using LC Branch and Bound");
        solveTSP(costMatrix);
    }
}


