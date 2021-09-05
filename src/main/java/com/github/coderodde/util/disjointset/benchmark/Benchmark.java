package com.github.coderodde.util.disjointset.benchmark;

import com.github.coderodde.graph.KruskalMST;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.github.coderodde.graph.UndirectedGraphNode;
import com.github.coderodde.graph.WeightFunction;
import com.github.coderodde.util.disjointset.AbstractDisjointSetRootFinder;
import com.github.coderodde.util.disjointset.AbstractDisjointSetUnionComputer;
import com.github.coderodde.util.disjointset.DisjointSet;
import com.github.coderodde.util.disjointset.DisjointSetIterativePathCompressionNodeFinder;
import com.github.coderodde.util.disjointset.DisjointSetPathHalvingNodeFinder;
import com.github.coderodde.util.disjointset.DisjointSetPathSplittingNodeFinder;
import com.github.coderodde.util.disjointset.DisjointSetRecursivePathCompressionNodeFinder;
import com.github.coderodde.util.disjointset.DisjointSetUnionByRankComputer;
import com.github.coderodde.util.disjointset.DisjointSetUnionBySizeComputer;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collections;

public class Benchmark {

    private static final int SIZE = 100_000;
    private static final float EDGE_LOAD_FACTOR = 15.0f;
    private static final int BAR_LENGTH = 80;
    
    private static final String BAR = buildBar();
    
    private final List<UndirectedGraphNode> graph;
    private final WeightFunction weightFunction;
    
    private final List<Result> results = new ArrayList<>(8);
    
    public Benchmark(long seed) {
        Random random = new Random(seed);
        
        long startTime = System.currentTimeMillis();
        
        Pair<List<UndirectedGraphNode>, WeightFunction> data
                = createRandomGraph(SIZE, EDGE_LOAD_FACTOR, random);
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        System.out.println(
                "The benchmark graph is built in " + duration +
                        " milliseconds.\n");
        
        this.graph = data.first;
        this.weightFunction = data.second;
    }

    private void run(RunType runType) {
        PrintStream originalPrintStream = null;
        
        if (runType.equals(RunType.WARMING_UP)) {
            originalPrintStream = directOutToNowhere();
        }
        
        String configMessage;
        
        for (AbstractDisjointSetRootFinder<UndirectedGraphNode> rootFinder :
                getRootFinders()) {
            for (AbstractDisjointSetUnionComputer<UndirectedGraphNode> 
                    unionComputer : getUnionComputers()) {
                
                bar();
                
                configMessage =
                        "Root finder: " + 
                                rootFinder.getClass().getSimpleName() + 
                                ", union computer: " + 
                                unionComputer.getClass().getSimpleName();
                
                System.out.println(configMessage);
                
                long duration = runKruskalsAlgorithm(rootFinder, unionComputer);

                if (runType.equals(RunType.BENCHMARKING)) {
                    this.results.add(new Result(configMessage, duration));
                }
            }
        }

        if (originalPrintStream != null) {
            System.setOut(originalPrintStream);
        }
    }
    
    private long runKruskalsAlgorithm(
            AbstractDisjointSetRootFinder<UndirectedGraphNode> rootFinder,
            AbstractDisjointSetUnionComputer
                    <UndirectedGraphNode> unionComputer) {
        
        DisjointSet<UndirectedGraphNode> disjointSet = 
                new DisjointSet<>(rootFinder, unionComputer);
        
        KruskalMST mst = new KruskalMST(disjointSet);
        
        long startTime = System.currentTimeMillis();
            
        KruskalMST.Data data = 
                mst.findMinimumSpanningTree(graph, weightFunction);
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        System.out.println(
                "Duration: " + duration + " ms. Total edges: " + 
                        data.edges.size() + ". Total weight: " + 
                        data.totalWeight + ".");
        
        return duration;
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
        bar();
        Collections.sort(benchmark.results);
        benchmark.printResults();
    }
    
    private void printResults() {
        int i = 1;
        
        for (Result result : results) {
            System.out.println(
                    i + ". " + result.configMessage + ", " + result.duration + 
                            " milliseconds.");
            i++;
        }
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
         
    private static String buildBar() {
        StringBuilder sb = new StringBuilder(BAR_LENGTH);
        
        for (int i = 0; i < BAR_LENGTH; i++) {
            sb.append('.');
        }
        
        return sb.toString();
    }
         
    private static void bar() {
        System.out.println(BAR);
    }
    
    private static final class Result implements Comparable<Result> {
        
        final String configMessage;
        final long duration;
        
        Result(String configMessage, long duration) {
            this.configMessage = configMessage;
            this.duration = duration;
        }
        
        @Override
        public int compareTo(Result other) {
            return Long.compare(duration, other.duration);
        }
        
        @Override
        public String toString() {
            return configMessage + ", duration = " + 
                    duration + " milliseconds.";
        }
    }
}