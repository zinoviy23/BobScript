package com.BobScript.Parsing.AbstraxtSyntaxTree.ConstantAndVariableNodes;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;
import com.BobScript.Parsing.AbstraxtSyntaxTree.TreeNode;

public class ConstStringNode extends TreeNode {
    private String value;

    public ConstStringNode(String value) {
        this.value = value;
        this.value = this.value.substring(1, this.value.length() - 1);
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("Const string: '" + value + "'");
    }

    @Override
    public Command[] compile() {
        return new Command[] {new Command(Commands.PUSH, 's',"'" + value + "'")};
    }
}
