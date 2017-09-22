package com.BobScript.Parsing.AbstraxtSyntaxTree.ConstantAndVariableNodes;

import com.BobScript.Parsing.AbstraxtSyntaxTree.TreeNode;

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
        drawLevel(level);
        debugWriter.println(name + ": variable");
    }
}
