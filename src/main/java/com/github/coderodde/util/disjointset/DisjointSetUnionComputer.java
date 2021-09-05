package com.github.coderodde.util.disjointset;

/**
 *
 * @author rodde
 */
public abstract class DisjointSetUnionComputer<E> {
    
    DisjointSet<E> ownerDisjointSet;
    
    public abstract void union(E item1, E item2);
}
