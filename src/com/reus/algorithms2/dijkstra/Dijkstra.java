package com.reus.algorithms2.dijkstra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Dijkstra {
    public static void main(String[] args) {
        Map<Integer, List<Integer[]>> graph = new HashMap<>();
        List<Integer[]> rootEdges = new ArrayList<>();
        rootEdges.add(new Integer[]{1, 20});
        rootEdges.add(new Integer[]{2, 10});
        graph.put(0, rootEdges);

        List<Integer[]> twoEdges = new ArrayList<>();
        twoEdges.add(new Integer[]{1, 5});
        graph.put(2, twoEdges);

        System.out.println(dijkstra(3, graph, 0));
    }

    private static Map<Integer, Integer> dijkstra(int n, Map<Integer, List<Integer[]>> graph, int v) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[v] = 0;
        boolean[] visited = new boolean[n];

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparing(pair -> pair[1]));
        pq.add(new int[]{v, 0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            visited[cur[0]] = true;
            List<Integer[]> edges = graph.get(cur[0]);
            if (edges == null) {
                continue;
            }
            for (Integer[] edge : edges) {
                if (visited[edge[0]]) continue;

                if (edge[1] + dist[cur[0]] < dist[edge[0]]) {
                    dist[edge[0]] = edge[1] + dist[cur[0]];
                    pq.add(new int[]{edge[0], dist[edge[0]]});
                }
            }
        }

        Map<Integer, Integer> res = new HashMap<>();
        for (int i = 0; i < n; i++) {
            res.put(i, dist[i]);
        }

        return res;
    }
}
