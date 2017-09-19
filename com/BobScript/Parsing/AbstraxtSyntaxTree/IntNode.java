package com.BobScript.Parsing.AbstraxtSyntaxTree;

public class IntNode extends TreeNode {
    private long value;

    public IntNode(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Long.toString(value) + ": int";
    }

    @Override
    public void debugPrint(int level) {
        for (int i = 0; i < level; i++)
            System.out.print("  ");
        System.out.println(value + ": int");
    }
}
