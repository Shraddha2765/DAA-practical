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

        // Greedy coloring algorithm
        void greedyColoring() {
            int[] result = new int[numCourses]; // color assigned to each course
            Arrays.fill(result, -1);

            // Assign first color to first course
            result[0] = 0;

            // Temporary array to mark unavailable colors
            boolean[] available = new boolean[numCourses];

            // Assign colors to remaining courses
            for (int u = 1; u < numCourses; u++) {

                // Mark colors of adjacent courses as unavailable
                Arrays.fill(available, false);
                for (int neighbor : adjList.get(u)) {
                    if (result[neighbor] != -1)
                        available[result[neighbor]] = true;
                }

                // Find the first available color
                int color;
                for (color = 0; color < numCourses; color++) {
                    if (!available[color])
                        break;
                }
                result[u] = color;
            }


	void welshPowellColoring() {
            // Step 1: compute degree of each course
            int[] degree = new int[numCourses];
            for (int i = 0; i < numCourses; i++) {
                degree[i] = adjList.get(i).size();
            }

            // Step 2: sort courses by decreasing degree
            Integer[] order = new Integer[numCourses];
            for (int i = 0; i < numCourses; i++) order[i] = i;
            Arrays.sort(order, (a, b) -> degree[b] - degree[a]);

            // Step 3: prepare result array (colors for each course)
            int[] result = new int[numCourses];
            Arrays.fill(result, -1); // uncolored

            int color = 0; // first slot (color 0)

            // Step 4: color vertices
            for (int i = 0; i < numCourses; i++) {
                int course = order[i];
                if (result[course] == -1) { // if not yet colored
                    result[course] = color;

                    // Assign same color to all non-adjacent uncolored vertices
                    for (int j = i + 1; j < numCourses; j++) {
                        int nextCourse = order[j];
                        if (result[nextCourse] == -1 && !isAdjacent(course, nextCourse)) {
                            result[nextCourse] = color;
                        }
                    }
                    color++; // next color for next group
                }
            }



            // Print the results
            System.out.println("\nExam Slot Allocation (Greedy Coloring):");
            for (int i = 0; i < numCourses; i++) {
                System.out.println("Course " + i + " → Slot " + result[i]);
            }

            int numSlots = Arrays.stream(result).max().getAsInt() + 1;
            System.out.println("\n✅ Minimum exam slots required (approx): " + numSlots);
        }
    }

    public static void main(String[] args) {
        // Example: 5 courses (0..4)
        Graph g = new Graph(5);

        // Edges represent students enrolled in both courses
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 3);
        g.addEdge(3, 4);

        g.greedyColoring();
    }
}


