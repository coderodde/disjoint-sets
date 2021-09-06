package com.github.coderodde.util.disjointset;

/**
 * This class implements the root finding algorithm for the disjoint-set data
 * structure using path halving.
 * 
 * @param <E> the satellite data type.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Sep 5, 2021)
 * @since 1.6 (Sep 5, 2021)
 */
public final class DisjointSetPathHalvingRootFinder<E>
extends AbstractDisjointSetRootFinder<E> {

    /**
     * {@inheritDoc }
     */
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
