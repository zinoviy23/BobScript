package com.BobScript.Parsing.AbstraxtSyntaxTree;

public class IntNode extends TreeNode {
    private int value;

    public IntNode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value) + ": int";
    }

    @Override
    public void debugPrint(int level) {
        for (int i = 0; i < level; i++)
            System.out.print("    ");
        System.out.println(value + ": int");
    }
}
