package com.github.coderodde.util.disjointset;

/**
 *
 * @author rodde
 */
public final class DisjointSetPathSplittingNodeFinder<E>
extends AbstractDisjointSetRootFinder<E> {

    @Override
    public E find(E item) {
        DisjointSetNode<E> node =
                ownerDisjointSet.find(ownerDisjointSet.getNode(item));
        
        while (node.getParent() != node) {
            DisjointSetNode<E> parent = node.getParent();
            node.setParent(parent.getParent());
            node = parent;
        }
        
        return node.getItem();
    }
}
