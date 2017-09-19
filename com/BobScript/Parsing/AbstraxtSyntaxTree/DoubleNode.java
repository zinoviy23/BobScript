package com.BobScript.Parsing.AbstraxtSyntaxTree;

public class DoubleNode extends TreeNode {
    private double value;

    public DoubleNode(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Double.toString(value) + ": double";
    }

    @Override
    public void debugPrint(int level) {
        for (int i = 0; i < level; i++)
            System.out.print("  ");
        System.out.println(value + ": double");
    }
}
