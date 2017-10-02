package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayElementNode extends TreeNode {
    private TreeNode gettingElement;
    private TreeNode comaNode;
    private ArrayList<TreeNode> indexes;

    public ArrayElementNode(TreeNode gettingElement) {
        this.gettingElement = gettingElement;
        indexes = new ArrayList<>();
    }

    public void setComaNode(TreeNode comaNode) {
        this.comaNode = comaNode;
        initIndexes(comaNode);
    }

    private void initIndexes(TreeNode current) {
        if (current == null)
            return;
        if (current instanceof OperationNode) {
            OperationNode tmp = (OperationNode)current;
            if (tmp.getOperation().equals(",")) {
                initIndexes(tmp.getLeft());
                indexes.add(tmp.getRight());
            }
            else
                indexes.add(current);
        }
        else
            indexes.add(current);
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("Getting");
        drawLevel(level);
        debugWriter.println("From: ");
        gettingElement.debugPrint(level + 1);
        drawLevel(level);
        debugWriter.println("Indexes: ");
        for (TreeNode tn : indexes)
            tn.debugPrint(level + 1);
    }

    @Override
    public Command[] compile() {
        ArrayList<Command> cmd = new ArrayList<>();
        cmd.addAll(Arrays.asList(gettingElement.compile()));
        for (int i = indexes.size() - 1; i >= 0; i--)
            cmd.addAll(Arrays.asList(indexes.get(i).compile()));
        cmd.add(new Command(Commands.GET_FROM, indexes.size()));
        return arrayListToArray(cmd);
    }
}
