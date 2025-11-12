import java.util.*;

public class UniversityExamScheduler {

    // Represent the graph
    static class Graph {
        int numCourses;
        List<List<Integer>> adjList;

        Graph(int numCourses) {
            this.numCourses = numCourses;
            adjList = new ArrayList<>();
            for (int i = 0; i < numCourses; i++)
                adjList.add(new ArrayList<>());
        }

        // Add edge between two courses (common student)
        void addEdge(int c1, int c2) {
            adjList.get(c1).add(c2);
            adjList.get(c2).add(c1);
        }

        // Check if two nodes are adjacent
        boolean isAdjacent(int a, int b) {
            return adjList.get(a).contains(b);
        }

        // --------------------------------------------------
        // 1. GREEDY COLORING METHOD
        // --------------------------------------------------
        void greedyColoring() {
            int[] result = new int[numCourses];
            Arrays.fill(result, -1);

            // Assign first color to first vertex
            result[0] = 0;
            boolean[] available = new boolean[numCourses];

            // Assign colors to remaining vertices
            for (int u = 1; u < numCourses; u++) {
                Arrays.fill(available, false);

                for (int neighbor : adjList.get(u)) {
                    if (result[neighbor] != -1)
                        available[result[neighbor]] = true;
                }

                int color;
                for (color = 0; color < numCourses; color++)
                    if (!available[color])
                        break;
                result[u] = color;
            }

            System.out.println("\n--- GREEDY COLORING ---");
            printResult(result);
        }

        // --------------------------------------------------
        // 2. WELSH-POWELL METHOD
        // --------------------------------------------------
        void welshPowellColoring() {
            int[] degree = new int[numCourses];
            for (int i = 0; i < numCourses; i++)
                degree[i] = adjList.get(i).size();

            Integer[] order = new Integer[numCourses];
            for (int i = 0; i < numCourses; i++) order[i] = i;

            Arrays.sort(order, (a, b) -> degree[b] - degree[a]);

            int[] result = new int[numCourses];
            Arrays.fill(result, -1);
            int color = 0;

            for (int i = 0; i < numCourses; i++) {
                int course = order[i];
                if (result[course] == -1) {
                    result[course] = color;
                    for (int j = i + 1; j < numCourses; j++) {
                        int next = order[j];
                        if (result[next] == -1 && !isAdjacent(course, next)) {
                            boolean canColor = true;
                            for (int neighbor : adjList.get(next)) {
                                if (result[neighbor] == color) {
                                    canColor = false;
                                    break;
                                }
                            }
                            if (canColor)
                                result[next] = color;
                        }
                    }
                    color++;
                }
            }

            System.out.println("\n--- WELSH-POWELL COLORING ---");
            printResult(result);
        }

        // --------------------------------------------------
        // 3. DSATUR ALGORITHM
        // --------------------------------------------------
        void dsaturColoring() {
            int[] result = new int[numCourses];
            Arrays.fill(result, -1);

            int[] degree = new int[numCourses];
            for (int i = 0; i < numCourses; i++)
                degree[i] = adjList.get(i).size();

            int[] saturation = new int[numCourses];
            Arrays.fill(saturation, 0);

            int coloredCount = 0;
            int maxDegreeVertex = 0;
            for (int i = 1; i < numCourses; i++)
                if (degree[i] > degree[maxDegreeVertex])
                    maxDegreeVertex = i;

            result[maxDegreeVertex] = 0;
            coloredCount++;

            while (coloredCount < numCourses) {
                int nextVertex = -1, maxSat = -1;
                for (int i = 0; i < numCourses; i++) {
                    if (result[i] == -1) {
                        int satCount = getSaturation(i, result);
                        if (satCount > maxSat ||
                                (satCount == maxSat && degree[i] > degree[nextVertex])) {
                            maxSat = satCount;
                            nextVertex = i;
                        }
                    }
                }

                boolean[] usedColors = new boolean[numCourses];
                for (int neighbor : adjList.get(nextVertex))
                    if (result[neighbor] != -1)
                        usedColors[result[neighbor]] = true;

                int color;
                for (color = 0; color < numCourses; color++)
                    if (!usedColors[color])
                        break;

                result[nextVertex] = color;
                coloredCount++;
            }

            System.out.println("\n--- DSATUR COLORING ---");
            printResult(result);
        }

        int getSaturation(int vertex, int[] result) {
            Set<Integer> distinctColors = new HashSet<>();
            for (int neighbor : adjList.get(vertex))
                if (result[neighbor] != -1)
                    distinctColors.add(result[neighbor]);
            return distinctColors.size();
        }

        // --------------------------------------------------
        // PRINT RESULT METHOD
        // --------------------------------------------------
        void printResult(int[] result) {
            int numSlots = Arrays.stream(result).max().getAsInt() + 1;
            for (int i = 0; i < numCourses; i++)
                System.out.println("Course " + i + " → Exam Slot " + result[i]);
            System.out.println("Minimum exam slots required: " + numSlots);
        }
    }

    // --------------------------------------------------
    // MAIN METHOD
    // --------------------------------------------------
    public static void main(String[] args) {
        Graph g = new Graph(5);

        // Edges represent conflicts (common students)
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 3);
        g.addEdge(3, 4);

        g.greedyColoring();
        g.welshPowellColoring();
        g.dsaturColoring();

        // Comparison Summary
        System.out.println("\n--- COMPARISON SUMMARY ---");
        System.out.println("1. Greedy Coloring: Simple, fast, may not give optimal slots.");
        System.out.println("2. Welsh-Powell: Uses degree sorting; reduces conflicts better.");
        System.out.println("3. DSATUR: Most accurate; selects vertex with highest saturation and degree, giving near-optimal coloring.");
    }
}
