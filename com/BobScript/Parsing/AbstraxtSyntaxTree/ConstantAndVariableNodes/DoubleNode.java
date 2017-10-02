package com.BobScript.Parsing.AbstraxtSyntaxTree.ConstantAndVariableNodes;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;
import com.BobScript.Parsing.AbstraxtSyntaxTree.TreeNode;

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
        drawLevel(level);
        debugWriter.println(value + ": double");
    }

    @Override
    public Command[] compile() {
        return new Command[] {new Command(Commands.PUSH, 'f', value)};
    }
}
