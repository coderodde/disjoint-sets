package com.github.coderodde.util.disjointset;

/**
 * This class implements the root finding algorithm for the disjoint-set data
 * structure using iterative path compression.
 * 
 * @param <E> the item type.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Sep 5, 2021)
 * @since 1.6 (Sep 5, 2021)
 */
public final class DisjointSetIterativePathCompressionNodeFinder<E>
extends AbstractDisjointSetRootFinder<E> {

    /**
     * {@inheritDoc } 
     */
    @Override
    public E find(E item) {
        DisjointSetNode<E> node = 
                ownerDisjointSet.find(ownerDisjointSet.getNode(item));

        DisjointSetNode<E> root = node;

        while (root.getParent() != root) {
            root = root.getParent();
        }

        while (node.getParent() != root) {
            DisjointSetNode<E> parent = node.getParent();
            node.setParent(root);
            node = parent;
        }

        return root.getItem();
    }
}
