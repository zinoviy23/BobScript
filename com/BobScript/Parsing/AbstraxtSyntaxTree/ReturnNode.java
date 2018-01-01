package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;

import java.util.ArrayList;
import java.util.Arrays;

public class ReturnNode extends TreeNode {
    private TreeNode returnValue;

    public ReturnNode(TreeNode returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("Return: ");
        if (returnValue != null)
            returnValue.debugPrint(level + 1);
    }

    @Override
    public Command[] compile() {
        ArrayList<Command> commands = new ArrayList<>();
        if (returnValue != null) {
            commands.addAll(Arrays.asList(returnValue.compile()));
            commands.add(new Command(Commands.RETURN, true));
        }
        else
            commands.add(new Command(Commands.RETURN, false));
        return arrayListToArray(commands);
    }
}
