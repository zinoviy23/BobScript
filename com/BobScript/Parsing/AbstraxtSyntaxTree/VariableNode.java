package com.BobScript.Parsing.AbstraxtSyntaxTree;

public class VariableNode extends TreeNode {
    private String name;

    public VariableNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + ": variable";
    }

    @Override
    public void debugPrint(int level) {
        for (int i = 0; i < level; i++)
            System.out.print("  ");
        System.out.println(name + ": variable");
    }
}
