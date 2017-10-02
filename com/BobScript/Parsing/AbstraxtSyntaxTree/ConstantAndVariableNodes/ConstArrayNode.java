package com.BobScript.Parsing.AbstraxtSyntaxTree.ConstantAndVariableNodes;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;
import com.BobScript.Parsing.AbstraxtSyntaxTree.OperationNode;
import com.BobScript.Parsing.AbstraxtSyntaxTree.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;

public class ConstArrayNode extends TreeNode {

    private TreeNode comaNode;
    private ArrayList<TreeNode> elements;

    public void setComaNode(TreeNode comaNode) {
        elements = new ArrayList<>();
        this.comaNode = comaNode;
        initElements(comaNode);
    }

    private void initElements(TreeNode current) {
        if (current == null)
            return;
        if (current instanceof OperationNode) {
            OperationNode tmp = (OperationNode)current;
            if (tmp.getOperation().equals(",")) {
                initElements(tmp.getLeft());
                elements.add(tmp.getRight());
            }
            else
                elements.add(current);
        }
        else
            elements.add(current);
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("Array of: ");
        for (TreeNode tn : elements)
            tn.debugPrint(level + 1);
    }

    @Override
    public Command[] compile() {
        ArrayList<Command> cmd = new ArrayList<>();
        for (int i = elements.size() - 1; i >= 0; i--)
            cmd.addAll(Arrays.asList(elements.get(i).compile()));
        cmd.add(new Command(Commands.CREATE_ARRAY, elements.size()));
        return arrayListToArray(cmd);
    }
}
