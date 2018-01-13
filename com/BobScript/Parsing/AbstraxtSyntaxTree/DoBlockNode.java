package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;

import java.util.ArrayList;
import java.util.Arrays;

public class DoBlockNode extends ComplexNode {
    private ArrayList<TreeNode> body = new ArrayList<>();

    @Override
    public void addToBody(TreeNode node) {
        body.add(node);
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("Do block");
        drawLevel(level);
        debugWriter.println("Body: ");
        for (TreeNode tn : body) {
            tn.debugPrint(level + 1);
        }
    }

    @Override
    public Command[] compile() {
        ArrayList<Command> commands = new ArrayList<>();
        for (TreeNode tn : body)
            commands.addAll(Arrays.asList(tn.compile()));
        return arrayListToArray(commands);
    }
}
