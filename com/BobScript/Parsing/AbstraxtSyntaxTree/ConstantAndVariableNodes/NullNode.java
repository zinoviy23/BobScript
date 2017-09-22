package com.BobScript.Parsing.AbstraxtSyntaxTree.ConstantAndVariableNodes;

import com.BobScript.Parsing.AbstraxtSyntaxTree.TreeNode;

public class NullNode extends TreeNode {
    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("null");
    }
}
