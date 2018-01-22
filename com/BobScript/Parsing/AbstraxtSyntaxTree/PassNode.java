package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;

public class PassNode extends TreeNode{
    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("Pass");
    }

    @Override
    public Command[] compile() {
        return new Command[0];
    }
}
