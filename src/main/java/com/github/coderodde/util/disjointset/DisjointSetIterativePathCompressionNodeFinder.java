package com.github.coderodde.util.disjointset;

/**
 *
 * @author rodde
 */
public class DisjointSetIterativePathCompressionNodeFinder<E>
extends DisjointSetRootFinder<E> {

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
