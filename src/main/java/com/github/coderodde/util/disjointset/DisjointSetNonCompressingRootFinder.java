package com.github.coderodde.util.disjointset;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public class DisjointSetNonCompressingRootFinder<E> 
        extends AbstractDisjointSetRootFinder<E> {

    @Override
    public E find(E item) {
        DisjointSetNode<E> node =
                ownerDisjointSet.find(ownerDisjointSet.getNode(item));
        int i = 0;
        while (node.getParent() != node) {
            node = node.getParent();
            System.out.println(i++);
        }

        return node.getItem();
    }
}
