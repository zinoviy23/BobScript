package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;

import java.util.ArrayList;
import java.util.Arrays;

public class IfNode extends ComplexNode implements Parentable {
    private ArrayList<TreeNode> body;
    private TreeNode condition;
    private TreeNode elseNode;
    private IfNode parent;

    public IfNode(TreeNode condition, IfNode parent) {
        this.condition = condition;
        body = new ArrayList<>();
        elseNode = null;
        this.parent = parent;
    }

    /**
     * Определяет следующий else
     * @param tn
     */
    public void setElseNode(TreeNode tn) {
        elseNode = tn;
    }

    @Override
    public TreeNode findRoot() {
        if (parent != null)
            return parent.findRoot();
        return this;
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

        if (elseNode != null) {
            drawLevel(level);
            debugWriter.println("Else:");
            elseNode.debugPrint(level + 1);
        }

    }

    @Override
    public Command[] compile() {
        ArrayList<Command> res = new ArrayList<>();

        Command[] conditionCommands = condition.compile();

        res.addAll(Arrays.asList(conditionCommands));
        res.add(new Command(Commands.CONDITION, ""));

        for (TreeNode tn : body)
            res.addAll(Arrays.asList(tn.compile()));

        Command[] elseCmd = new Command[0];
        if (elseNode != null)
            elseCmd = elseNode.compile();
        res.add(new Command(Commands.GOTO, elseCmd.length + 2));

        res.add(new Command(Commands.END_CONDITION, ""));
        res.addAll(Arrays.asList(elseCmd));

        return arrayListToArray(res);
    }
}
