import java.util.*;
public class Tree<V, E> {
    private V data;
    //here the edgeVal is the edge leading from the node's parent to the node itself,
    //not from the node to any of its children
    private E edgeVal;
    private Tree<V, E> parent;
    private LinkedHashSet<Tree<V, E>> children;
    private int depth;
    public Tree(V data) {
        this.data = data;
        this.parent = null;
        this.edgeVal = null;
    }
    public Tree(V data, Tree<V, E> parent, E edgeVal) {
        this.data = data;
        this.edgeVal = edgeVal;
        this.parent = parent;
        this.children = new LinkedHashSet<Tree<V, E>>();
        this.depth = 0;
    }
    public void addChild(V childData, E edgeVal) {
        Tree<V, E> child = new Tree<V, E>(childData, this, edgeVal);
        this.children.add(child);
    }
    public Tree<V, E> getParent() {
        return this.parent;
    }
    public LinkedHashSet<Tree<V, E>> getChildren() {
        return this.children;
    }
    public V getData() {
        return this.data;
    }
    public E getEdgeVal() {
        return this.edgeVal;
    }
    public boolean isRoot() {
        return this.parent == null;
    }
    public boolean isLeaf() {
        return this.children.isEmpty();
    }
    public String toString() {
        //assuming type of data has a nicely defined toString() method
        return this.data.toString();
    }
}
