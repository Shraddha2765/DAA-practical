import java.util.*;

public class SimpleSwiftCargo {
    public static void main(String[] args) {
        Graph g = new Graph();

        // Add nodes (stage 0 to 3)
        g.addNode(1, 0); // source
        g.addNode(2, 1);
        g.addNode(3, 1);
        g.addNode(4, 2);
        g.addNode(5, 2);
        g.addNode(6, 3); // destination

        // Add edges with only cost
        g.addEdge(1, 2, 5);
        g.addEdge(1, 3, 6);
        g.addEdge(2, 4, 4);
        g.addEdge(2, 5, 7);
        g.addEdge(3, 4, 2);
        g.addEdge(3, 5, 5);
        g.addEdge(4, 6, 6);
        g.addEdge(5, 6, 4);

        // Find minimum cost route
        g.findMinCostPath(1, 6);
    }
}

class Edge {
    int from, to;
    double cost;
    Edge(int from, int to, double cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }
}

class Node {
    int id, stage;
    Node(int id, int stage) {
        this.id = id;
        this.stage = stage;
    }
}

class Graph {
    Map<Integer, Node> nodes =  new HashMap<>();
    Map<Integer, List<Edge>> adj = new HashMap<>();
    Map<Integer, List<Integer>> stageNodes = new HashMap<>();

    // Add node
    void addNode(int id, int stage) {
        Node n = new Node(id, stage);
        nodes.put(id, n);
        adj.put(id, new ArrayList<>());
        stageNodes.computeIfAbsent(stage, k -> new ArrayList<>()).add(id);
    }

    // Add edge
    void addEdge(int from, int to, double cost) {
        if (!nodes.containsKey(from) || !nodes.containsKey(to))
            throw new RuntimeException("Invalid node id");
        adj.get(from).add(new Edge(from, to, cost));
    }

    // Dynamic Programming algorithm for multistage graph
    void findMinCostPath(int sourceId, int destId) {
        Node src = nodes.get(sourceId);
        Node dst = nodes.get(destId);
        if (src == null || dst == null || src.stage > dst.stage) {
            System.out.println("Invalid source or destination");
            return;
        }

        Map<Integer, Double> dp = new HashMap<>(); // min cost to reach each node
        Map<Integer, Integer> parent = new HashMap<>(); // for path reconstruction

        // Step 1: Initialize all nodes' cost = ∞
        for (int id : nodes.keySet())
            dp.put(id, Double.POSITIVE_INFINITY);
        dp.put(sourceId, 0.0);

        // Step 2: Stage-by-stage DP
        for (int st = src.stage; st <= dst.stage; st++) {
            List<Integer> currStage = stageNodes.getOrDefault(st, Collections.emptyList());
            for (int u : currStage) {
                double costU = dp.get(u);
                if (costU == Double.POSITIVE_INFINITY) continue;

                for (Edge e : adj.getOrDefault(u, Collections.emptyList())) {
                    Node toNode = nodes.get(e.to);
                    if (toNode.stage < st) continue; // no backward movement

                    double newCost = costU + e.cost;
                    if (newCost < dp.get(e.to)) {
                        dp.put(e.to, newCost);
                        parent.put(e.to, u);
                    }
                }
            }
        }

        // Step 3: Print result
        double finalCost = dp.get(destId);
        if (finalCost == Double.POSITIVE_INFINITY) {
            System.out.println("No path found.");
            return;
        }

        // reconstruct path
        LinkedList<Integer> path = new LinkedList<>();
        int cur = destId;
        while (cur != sourceId) {
            path.addFirst(cur);
            cur = parent.get(cur);
        }
        path.addFirst(sourceId);

        System.out.println("Minimum cost path: " + path);
        System.out.println("Total cost: " + finalCost);
    }
}


