package com.github.coderodde.util.disjointset;

/**
 *
 * @author rodde
 */
public final class DisjointSetPathHalvingNodeFinder<E>
extends AbstractDisjointSetRootFinder<E> {

    @Override
    public E find(E item) {
        DisjointSetNode<E> node =
                ownerDisjointSet.find(ownerDisjointSet.getNode(item));
        
        while (node.getParent() != node) {
            node.setParent(node.getParent().getParent());
            node = node.getParent();
        }
        
        return node.getItem();
    }
}
