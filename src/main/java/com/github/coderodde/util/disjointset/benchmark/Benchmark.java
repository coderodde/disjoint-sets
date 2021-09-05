package com.github.coderodde.util.disjointset.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.github.coderodde.graph.UndirectedGraphEdge;
import com.github.coderodde.graph.UndirectedGraphNode;
import com.github.coderodde.graph.WeightFunction;
import com.github.coderodde.util.disjointset.AbstractDisjointSetRootFinder;
import com.github.coderodde.util.disjointset.AbstractDisjointSetUnionComputer;
import com.github.coderodde.util.disjointset.DisjointSetIterativePathCompressionNodeFinder;
import com.github.coderodde.util.disjointset.DisjointSetPathHalvingNodeFinder;
import com.github.coderodde.util.disjointset.DisjointSetPathSplittingNodeFinder;
import com.github.coderodde.util.disjointset.DisjointSetRecursivePathCompressionNodeFinder;
import com.github.coderodde.util.disjointset.DisjointSetUnionByRankComputer;
import com.github.coderodde.util.disjointset.DisjointSetUnionBySizeComputer;
import java.io.OutputStream;
import java.io.PrintStream;

public class Benchmark {

    private static final int SIZE = 100000;
    private static final float EDGE_LOAD_FACTOR = 15.0f;

    private final List<UndirectedGraphNode> graph;
    private final WeightFunction weightFunction;
    
    public Benchmark(long seed) {
        Random random = new Random(seed);
        Pair<List<UndirectedGraphNode>, WeightFunction> data
                = createRandomGraph(SIZE, EDGE_LOAD_FACTOR, random);
        
        this.graph = data.first;
        this.weightFunction = data.second;
    }

    private void run(RunType runType) {
        PrintStream originalPrintStream = null;
        
        if (runType.equals(RunType.WARMING_UP)) {
            originalPrintStream = directOutToNowhere();
        }
        
        for (AbstractDisjointSetRootFinder<UndirectedGraphNode> rootFinder :
                getRootFinders()) {
            for (AbstractDisjointSetUnionComputer<UndirectedGraphNode> 
                    unionComputers : getUnionComputers()) {
                
                System.out.println(
                        " Root finder: " + 
                                rootFinder.getClass().getSimpleName() + 
                                ", union computer: " + 
                                unionComputers.getClass().getSimpleName());
                
                
            }
        }

        if (originalPrintStream != null) {
            System.setOut(originalPrintStream);
            
        }
    }
    
    private enum RunType {
        WARMING_UP,
        BENCHMARKING
    }
    
    public static void main(final String... args) {
        long seed = System.currentTimeMillis();
        System.out.println("Seed: " + seed);
        Benchmark benchmark = new Benchmark(seed);
        benchmark.run(RunType.WARMING_UP);
        benchmark.run(RunType.BENCHMARKING);
    }
    
    private static PrintStream directOutToNowhere() {
        PrintStream originalPrintStream = System.out;

        System.setOut(new PrintStream(new OutputStream() {
            public void write(int b) {
                // Ignore console output.
            }
        }));
        
        return originalPrintStream;
    }
    
    public static final class Pair<F, S> {

        public final F first;
        public final S second;

        public Pair(final F first, final S second) {
            this.first = first;
            this.second = second;
        }
    }

    public static <E> E choose(final List<E> list, final Random rnd) {
        if (list.isEmpty()) {
            return null;
        }

        return list.get(rnd.nextInt(list.size()));
    }

    private static Pair<List<UndirectedGraphNode>, WeightFunction>
            createRandomGraph(final int size, 
                              final float edgeLoadFactor,
                              final Random rnd) {
        final List<UndirectedGraphNode> graph = new ArrayList<>(size);
        final WeightFunction wf = new WeightFunction();

        for (int i = 0; i < size; ++i) {
            graph.add(new UndirectedGraphNode("" + i));
        }

        int edges = (int)(Math.min(1.0f, edgeLoadFactor) * size);

        while (edges > 0) {
            final UndirectedGraphNode u = choose(graph, rnd);
            final UndirectedGraphNode v = choose(graph, rnd);
            u.connectTo(v);
            wf.put(u, v, 10.0 * rnd.nextDouble() - 5.0);
            --edges;
        }

        return new Pair(graph, wf);
    }
            
    private static List<AbstractDisjointSetRootFinder<UndirectedGraphNode>> 
        getRootFinders() {
        List<AbstractDisjointSetRootFinder<UndirectedGraphNode>> rootFinders = 
                new ArrayList<>();
        
        rootFinders.add(new DisjointSetRecursivePathCompressionNodeFinder<>());
        rootFinders.add(new DisjointSetIterativePathCompressionNodeFinder<>());
        rootFinders.add(new DisjointSetPathHalvingNodeFinder<>());
        rootFinders.add(new DisjointSetPathSplittingNodeFinder<>());
       
        return rootFinders;
    }
        
    private static List<AbstractDisjointSetUnionComputer<UndirectedGraphNode>>
         getUnionComputers() {
        
        List<AbstractDisjointSetUnionComputer<UndirectedGraphNode>>
                unionComputers = new ArrayList<>();
        
        unionComputers.add(new DisjointSetUnionByRankComputer<>());
        unionComputers.add(new DisjointSetUnionBySizeComputer<>());
        
        return unionComputers;
    }
}