package com.github.coderodde.util.disjointset;

/**
 *
 * @author rodde
 */
public abstract class AbstractDisjointSetRootFinder<E> {
    
    DisjointSet<E> ownerDisjointSet;
    
    /**
     * If {@code item} belongs to a tree, returns the root of that three. 
     * Otherwise, a trivial, empty tree {@code T} is created, and {@code item} 
     * is added to {@code T}.
     * 
     * @param item the query item.
     * @return the root of the tree to which {@code item} belongs to.
     */
    public abstract E find(E item);
}
