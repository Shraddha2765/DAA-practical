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

        // Dijkstra's algorithm
        void dijkstra(int src, Set<Integer> hospitals) {
            int[] dist = new int[n];
            int[] parent = new int[n];
            boolean[] visited = new boolean[n];

            // initialize distances
            Arrays.fill(dist, Integer.MAX_VALUE);
            Arrays.fill(parent, -1);
            dist[src] = 0;

            // priority queue (min-heap)
            PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
            pq.add(new int[]{0, src}); // {distance, node}

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

            // print all distances
            System.out.println("\nShortest travel times from source (" + src + "):");
            for (int i = 0; i < n; i++) {
                System.out.println("To node " + i + " : " + (dist[i] == Integer.MAX_VALUE ? "INF" : dist[i]) + " minutes");
            }

            // find nearest hospital
            int nearestHospital = -1;
            int bestTime = Integer.MAX_VALUE;
            for (int h : hospitals) {
                if (dist[h] < bestTime) {
                    bestTime = dist[h];
                    nearestHospital = h;
                }
            }

            System.out.println("\nNearest hospital: " + nearestHospital + " (Time: " + bestTime + " minutes)");
            System.out.print("Path: ");
            printPath(nearestHospital, parent);
            System.out.println();
        }

        // reconstruct and print path
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
        Graph g = new Graph(6);

        // Directed roads (as per your example)
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

        g.dijkstra(source, hospitals);
    }
}


