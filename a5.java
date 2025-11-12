import java.util.*;

public class SmartAmbulanceDirected {

    static class Edge {
        int to;
        int time; // travel time in minutes
        Edge(int to, int time) {
            this.to = to;
            this.time = time;
        }
    }

    static class Graph {
        int n; // number of intersections
        List<List<Edge>> adj; // adjacency list

        Graph(int n) {
            this.n = n;
            adj = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                adj.add(new ArrayList<>());
            }
        }

        // Add directed edge (one-way road)
        void addEdge(int from, int to, int time) {
            adj.get(from).add(new Edge(to, time));
        }

        // Update single edge (manual traffic update)
        void updateEdge(int from, int to, int newTime) {
            boolean found = false;
            for (Edge e : adj.get(from)) {
                if (e.to == to) {
                    e.time = newTime;
                    found = true;
                    System.out.println("Updated edge " + from + " -> " + to + " to new travel time: " + newTime + " minutes");
                    break;
                }
            }
            if (!found)
                System.out.println("No edge found from " + from + " -> " + to);
        }

        // Bulk update multiple edge costs
        void updateEdgeCost() {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter number of updates to perform: ");
            int k = sc.nextInt();

            for (int i = 0; i < k; i++) {
                System.out.print("\nUpdate " + (i + 1) + " - From: ");
                int from = sc.nextInt();
                System.out.print("To: ");
                int to = sc.nextInt();
                System.out.print("New travel time (min): ");
                int time = sc.nextInt();
                updateEdge(from, to, time);
            }
            System.out.println("All updates completed successfully.");
        }

        // Dijkstra's algorithm for shortest path
        void dijkstra(int src, Set<Integer> hospitals) {
            int[] dist = new int[n];
            int[] parent = new int[n];
            boolean[] visited = new boolean[n];

            Arrays.fill(dist, Integer.MAX_VALUE);
            Arrays.fill(parent, -1);
            dist[src] = 0;

            PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
            pq.add(new int[]{0, src});

            while (!pq.isEmpty()) {
                int[] cur = pq.poll();
                int d = cur[0];
                int u = cur[1];

                if (visited[u]) continue;
                visited[u] = true;

                for (Edge e : adj.get(u)) {
                    int v = e.to;
                    int newDist = d + e.time;
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        parent[v] = u;
                        pq.add(new int[]{newDist, v});
                    }
                }
            }

            System.out.println("\nShortest travel times from Source (" + src + "):");
            for (int i = 0; i < n; i++) {
                System.out.println("To node " + i + " : " +
                        (dist[i] == Integer.MAX_VALUE ? "INF" : dist[i]) + " minutes");
            }

            // Find nearest hospital
            int nearestHospital = -1;
            int bestTime = Integer.MAX_VALUE;
            for (int h : hospitals) {
                if (dist[h] < bestTime) {
                    bestTime = dist[h];
                    nearestHospital = h;
                }
            }

            if (nearestHospital == -1 || bestTime == Integer.MAX_VALUE) {
                System.out.println("\nNo reachable hospital found.");
            } else {
                System.out.println("\nNearest hospital: Node " + nearestHospital +
                        " (Time: " + bestTime + " minutes)");
                System.out.print("Path: ");
                printPath(nearestHospital, parent);
                System.out.println();
            }
        }

        // Reconstruct and print the shortest path
        void printPath(int dest, int[] parent) {
            if (dest == -1) {
                System.out.println("No path available");
                return;
            }
            List<Integer> path = new ArrayList<>();
            int cur = dest;
            while (cur != -1) {
                path.add(cur);
                cur = parent[cur];
            }
            Collections.reverse(path);
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i));
                if (i != path.size() - 1) System.out.print(" -> ");
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Graph g = new Graph(6);

        // Directed roads (example graph)
        g.addEdge(0, 1, 4);
        g.addEdge(0, 2, 2);
        g.addEdge(1, 2, 1);
        g.addEdge(1, 3, 5);
        g.addEdge(2, 3, 8);
        g.addEdge(2, 4, 10);
        g.addEdge(3, 4, 2);
        g.addEdge(3, 5, 6);
        g.addEdge(4, 5, 2);

        int source = 0;
        Set<Integer> hospitals = new HashSet<>(Arrays.asList(4, 5));

        while (true) {
            System.out.println("\n==============================");
            System.out.println("SMART AMBULANCE TRAFFIC SYSTEM");
            System.out.println("1. Calculate shortest route to hospitals");
            System.out.println("2. Update single road travel time");
            System.out.println("3. Bulk update multiple roads");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    g.dijkstra(source, hospitals);
                    break;
                case 2:
                    System.out.print("Enter from-node: ");
                    int from = sc.nextInt();
                    System.out.print("Enter to-node: ");
                    int to = sc.nextInt();
                    System.out.print("Enter new travel time: ");
                    int time = sc.nextInt();
                    g.updateEdge(from, to, time);
                    break;
                case 3:
                    g.updateEdgeCost();
                    break;
                case 4:
                    System.out.println("Exiting system...");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}
