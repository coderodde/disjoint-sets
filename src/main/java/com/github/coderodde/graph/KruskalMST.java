package com.github.coderodde.graph;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.github.coderodde.util.disjointset.DisjointSet;

public final class KruskalMST {

    private final DisjointSet<UndirectedGraphNode> disjointSet;
    
    public KruskalMST(DisjointSet<UndirectedGraphNode> disjointSet) {
        this.disjointSet = disjointSet;
    }

    public List<UndirectedGraphEdge> 
        findMinimumSpanningTree(final List<UndirectedGraphNode> graph,
                                final WeightFunction weightFunction) {
        List<UndirectedGraphEdge> edges = 
                prepareEdgeList(graph, weightFunction);

        List<UndirectedGraphEdge> minimumSpanningTree = 
                new ArrayList<>();

        for (final UndirectedGraphEdge edge : edges) {
            UndirectedGraphNode u = edge.firstNode();
            UndirectedGraphNode v = edge.secondNode();

            UndirectedGraphNode root1 = this.disjointSet.find(u);
            UndirectedGraphNode root2 = this.disjointSet.find(v);

            if (root1 != root2) {
                this.disjointSet.union(root1, root2);
                minimumSpanningTree.add(edge);
            }
        }

        return minimumSpanningTree;
    }

    private List<UndirectedGraphEdge>
         prepareEdgeList(List<UndirectedGraphNode> graph,
                         WeightFunction weightFunction) {
        List<UndirectedGraphEdge> edges = new ArrayList<>();
        
        for (UndirectedGraphNode node : graph) {
            for (UndirectedGraphNode neighbor : node) {
                edges.add(new UndirectedGraphEdge(node, 
                                                neighbor, 
                                                weightFunction
                                                        .get(node, neighbor)));
            }
        }

        Collections.<UndirectedGraphEdge>sort(edges);
        return edges;
    }
}