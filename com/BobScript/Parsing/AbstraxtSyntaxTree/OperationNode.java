package com.BobScript.Parsing.AbstraxtSyntaxTree;

public class OperationNode extends TreeNode {
    private TreeNode left, right;
    private String operation;

    public OperationNode(String operation) {
        this.left = null;
        this.right = null;
        this.operation = operation;
    }

    public TreeNode getLeft() {
        return left;
    }

    public TreeNode getRight() {
        return right;
    }

    public String getOperation() {
        return operation;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return operation + ": {" + left + "; " + right + "}";
    }

    @Override
    public void debugPrint(int level) {
        for (int i = 0; i < level; i++)
            System.out.print("  ");
        System.out.println(operation + ":");
        left.debugPrint(level + 1);
        right.debugPrint(level + 1);

    }
}
