package com.BobScript.Parsing.AbstraxtSyntaxTree.ConstantAndVariableNodes;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;
import com.BobScript.Parsing.AbstraxtSyntaxTree.TreeNode;

public class BooleanNode extends TreeNode {
    private boolean value;

    public BooleanNode(boolean value) {
        this.value = value;
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println(value + ": boolean");
    }

    @Override
    public Command[] compile() {
        return new Command[] {new Command(Commands.PUSH, 'b', value)};
    }
}
