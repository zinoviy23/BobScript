package com.BobScript.Parsing.AbstraxtSyntaxTree.ConstantAndVariableNodes;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;
import com.BobScript.Parsing.AbstraxtSyntaxTree.TreeNode;

public class NullNode extends TreeNode {
    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("null");
    }

    @Override
    public Command[] compile() {
        return new Command[] {new Command(Commands.PUSH, "null")};
    }
}
