package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;

import java.util.ArrayList;
import java.util.Arrays;

public class WhileNode extends ComplexNode {
    private ArrayList<TreeNode> body;

    private TreeNode condition;

    public WhileNode(TreeNode condition) {
        body = new ArrayList<>();
        this.condition = condition;
    }

    @Override
    public void addToBody(TreeNode node) {
        body.add(node);
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("While");

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

        for (TreeNode tn : body) {
            res.addAll(Arrays.asList(tn.compile()));
        }

        for (int i = 0; i < res.size(); i++)
            if (res.get(i).getCommand() == Commands.PARSE_CONTINUE) {
                res.set(i, new Command(Commands.GOTO, res.size() - i));
            }

        res.add(new Command(Commands.GOTO, -res.size()));

        res.add(new Command(Commands.END_CONDITION, ""));

        for (int i = 0; i < res.size(); i++)
            if (res.get(i).getCommand() == Commands.PARSE_BREAK) {
                res.set(i, new Command(Commands.GOTO, res.size() - i));
            }

        return arrayListToArray(res);
    }
}
