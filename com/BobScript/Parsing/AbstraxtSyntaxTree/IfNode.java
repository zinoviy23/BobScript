package com.BobScript.Parsing.AbstraxtSyntaxTree;

import java.util.ArrayList;

public class IfNode extends ComplexNode {
    private ArrayList<TreeNode> body;
    private TreeNode condition;

    public IfNode(TreeNode condition) {
        this.condition = condition;
        body = new ArrayList<>();
    }

    @Override
    public void addToBody(TreeNode node) {
        body.add(node);
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("If");

        drawLevel(level);
        debugWriter.println("Condition: ");
        condition.debugPrint(level + 1);
        drawLevel(level);
        debugWriter.println("Body: ");
        for (TreeNode tn : body)
            tn.debugPrint(level + 1);
    }
}
