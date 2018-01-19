package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;

import java.util.ArrayList;
import java.util.Arrays;

public class NewNode extends TreeNode {
    private String className;
    private ArrayList<TreeNode> arguments;

    public NewNode(String className) {
        this.className = className;
        arguments = new ArrayList<>();
    }

    public void initArguments(TreeNode comaNode) {
        if (comaNode == null)
            return;
        if (comaNode instanceof OperationNode) {
            OperationNode tmp = (OperationNode) comaNode;
            if (tmp.getOperation().equals(",")) {
                initArguments(tmp.getLeft());
                arguments.add(tmp.getRight());
            }
            else
                arguments.add(comaNode);
        }
        else
            arguments.add(comaNode);
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("New " + className);
        drawLevel(level);
        debugWriter.println("Arguments");
        for (TreeNode tn : arguments) {
            tn.debugPrint(level + 1);
        }
    }

    @Override
    public Command[] compile() {
        ArrayList<Command> res = new ArrayList<>();
        for (int i = arguments.size() - 1; i >= 0; i--)
            res.addAll(Arrays.asList(arguments.get(i).compile()));
        res.add(new Command(Commands.CREATE_INSTANCE, className, arguments.size()));

        return arrayListToArray(res);
    }
}
