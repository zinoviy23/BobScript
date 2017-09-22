package com.BobScript.Parsing.AbstraxtSyntaxTree;

import java.util.ArrayList;

public class WhileNode extends ComplexNode {
    private ArrayList<TreeNode> body;

    private TreeNode condition;

    public WhileNode(TreeNode condition) {
        body = new ArrayList<>();
        this.condition = condition;
    }

    @Override
    public void addToBody(TreeNode node) {
        body.add(node);
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("While");

        drawLevel(level);
        debugWriter.println("Condition: ");
        condition.debugPrint(level + 1);
        drawLevel(level);
        debugWriter.println("Body: ");
        for (TreeNode tn : body)
            tn.debugPrint(level + 1);
    }
}
