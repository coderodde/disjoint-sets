package com.github.coderodde.util.disjointset;

/**
 * This class implements the method for returning the item root node using path
 * compression.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Sep 5, 2021)
 * @since 1.6 (Sep 5, 2021)
 */
public final class DisjointSetRecursivePathCompressionNodeFinder<E>
extends AbstractDisjointSetRootFinder<E> {
    
    @Override
    public E find(E item) {
        DisjointSetNode<E> node = 
                ownerDisjointSet.find(ownerDisjointSet.getNode(item));
        
        if (node == node.getParent()) {
            return node.getItem();
        }
        
        node.setParent(ownerDisjointSet.find(node.getParent()));
        return node.getParent().getItem();
    }
}
