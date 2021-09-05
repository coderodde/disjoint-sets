package com.github.coderodde.util.disjointset;

/**
 *
 * @author rodde
 */
public abstract class AbstractDisjointSetUnionComputer<E> {
    
    DisjointSet<E> ownerDisjointSet;
    
    public abstract void union(E item1, E item2);
}
