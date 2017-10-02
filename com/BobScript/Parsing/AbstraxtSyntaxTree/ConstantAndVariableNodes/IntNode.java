package com.BobScript.Parsing.AbstraxtSyntaxTree.ConstantAndVariableNodes;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;
import com.BobScript.Parsing.AbstraxtSyntaxTree.TreeNode;

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
        drawLevel(level);
        debugWriter.println(value + ": int");
    }

    @Override
    public Command[] compile() {
        return new Command[] {new Command(Commands.PUSH, 'i', value)};
    }
}
