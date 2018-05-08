package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;

public class BreakNode extends TreeNode {
    private String point = null;

    public BreakNode(String point) {
        this.point = point;
    }

    public BreakNode() {
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("Break" + (point == null ? "" : " : " + point));
    }

    @Override
    public Command[] compile() {
        if (point == null)
            return new Command[] {new Command(Commands.PARSE_BREAK)};
        else
            return new Command[] {new Command(Commands.PARSE_BREAK, point)};
    }
}
