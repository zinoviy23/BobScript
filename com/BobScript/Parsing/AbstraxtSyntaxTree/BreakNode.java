package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;

public class BreakNode extends TreeNode {
    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("Break");
    }

    @Override
    public Command[] compile() {
        return new Command[] {new Command(Commands.PARSE_BREAK)};
    }
}
