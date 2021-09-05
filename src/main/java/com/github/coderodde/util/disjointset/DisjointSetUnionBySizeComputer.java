package com.github.coderodde.util.disjointset;

/**
 *
 * @author rodde
 */
public final class DisjointSetUnionBySizeComputer<E>
extends DisjointSetUnionComputer<E> {

    @Override
    public void union(E item1, E item2) {
        DisjointSetNode<E> node1 = 
                ownerDisjointSet.find(ownerDisjointSet.getNode(item1));
        
        DisjointSetNode<E> node2 = 
                ownerDisjointSet.find(ownerDisjointSet.getNode(item2));
        
        if (node1 == node2) {
            return;
        }
        
        if (node1.getSize() < node2.getSize()) {
            DisjointSetNode<E> tempNode = node1;
            node1 = node2;
            node2 = tempNode;
        }
        
        node2.setParent(node1);
        node1.setSize(node1.getSize() + node2.getSize());
    }
}
