package com.github.coderodde.util.disjointset;

/**
 *
 * This class implements the root finding algorithm for the disjoint-set data
 * structure using path splitting.
 * 
 * @param <E> the satellite data type.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Sep 5, 2021)
 * @since 1.6 (Sep 5, 2021)
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
