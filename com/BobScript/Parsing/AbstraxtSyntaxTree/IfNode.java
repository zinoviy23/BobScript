package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;

import java.util.ArrayList;
import java.util.Arrays;

public class IfNode extends ComplexNode {
    private ArrayList<TreeNode> body;
    private TreeNode condition;

    public IfNode(TreeNode condition) {
        this.condition = condition;
        body = new ArrayList<>();
    }

    @Override
    public void addToBody(TreeNode node) {
        body.add(node);
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("If");

        drawLevel(level);
        debugWriter.println("Condition: ");
        condition.debugPrint(level + 1);
        drawLevel(level);
        debugWriter.println("Body: ");
        for (TreeNode tn : body)
            tn.debugPrint(level + 1);
    }

    @Override
    public Command[] compile() {
        ArrayList<Command> res = new ArrayList<>();

        Command[] conditionCommands = condition.compile();

        res.addAll(Arrays.asList(conditionCommands));
        res.add(new Command(Commands.CONDITION, ""));

        for (TreeNode tn : body)
            res.addAll(Arrays.asList(tn.compile()));

        res.add(new Command(Commands.END_CONDITION, ""));

        return arrayListToArray(res);
    }
}
