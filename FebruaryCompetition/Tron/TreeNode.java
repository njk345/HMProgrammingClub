import java.util.*;
public class TreeNode<T> {
    private T data;
    private TreeNode<T> parent;
    private ArrayList<TreeNode<T>> children;
    public TreeNode(T data, TreeNode<T> parent) {
        this.data = data;
        this.parent = parent;
        this.children = new ArrayList<TreeNode<T>>();
    }
    public void addChild(T childData) {
        TreeNode<T> child = new TreeNode<T>(childData, this);
        this.children.add(child);
    }
    public void addChild(TreeNode<T> child) {
        this.children.add(child);
    }
    public ArrayList<TreeNode<T>> getChildren() {
        return this.children;
    }
    public T getData() {
        return this.data;
    }
    public void printData(boolean stdPrint) {
        if (stdPrint) System.out.println(this.getData());
        else Tron.logln(this.getData().toString());
    }
    public boolean isRoot() {
        return this.parent == null;
    }
    public boolean isLeaf() {
        return this.children.size() == 0;
    }
}
