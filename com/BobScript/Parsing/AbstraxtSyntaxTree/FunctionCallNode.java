package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;

import java.util.ArrayList;
import java.util.Arrays;

public class FunctionCallNode extends TreeNode {
    private ArrayList<TreeNode> arguments;
    private String name;
    private TreeNode comaNode;
    private boolean isPointCall;

    public FunctionCallNode(String name) {
        this.name = name;
        arguments = new ArrayList<>();
    }

    public void setComaNode(TreeNode tn) {
        comaNode = tn;
        initArguments(comaNode);
    }

    public boolean isPointCall() {
        return isPointCall;
    }

    public void setPointCall(boolean pointCall) {
        isPointCall = pointCall;
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("Function Call: " + name);
        drawLevel(level);
        debugWriter.println("Arguments");
        for (TreeNode tn : arguments)
            tn.debugPrint(level + 1);
    }

    private void initArguments(TreeNode current) {
        if (current == null)
            return;
        if (current instanceof OperationNode) {
            OperationNode tmp = (OperationNode)current;
            if (tmp.getOperation().equals(",")) {
                initArguments(tmp.getLeft());
                arguments.add(tmp.getRight());
            }
            else
                arguments.add(current);
        }
        else
            arguments.add(current);
    }

    @Override
    public Command[] compile() {
        ArrayList<Command> cmd = new ArrayList<>();
        for (int i = arguments.size() - 1; i >= 0; i--)
            cmd.addAll(Arrays.asList(arguments.get(i).compile()));
        if (!isPointCall())
            cmd.add(new Command(Commands.CALL, name, arguments.size()));
        else
            cmd.add(new Command(Commands.CALL_FROM, name, arguments.size()));

        return arrayListToArray(cmd);
    }
}
