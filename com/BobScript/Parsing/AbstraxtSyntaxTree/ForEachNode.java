package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;
import com.BobScript.Parsing.AbstraxtSyntaxTree.ConstantAndVariableNodes.VariableNode;

import java.util.ArrayList;
import java.util.Arrays;

public class ForEachNode extends ComplexNode {
    private ArrayList<TreeNode> body;
    private TreeNode variables;
    private TreeNode iterable;
    private static int foreachLoopsCounter = -1;

    public ForEachNode(TreeNode inNode) {
        body = new ArrayList<>();
        if (!(inNode instanceof OperationNode))
            return;
        if (!((OperationNode) inNode).getOperation().equals("in"))
            return;
        variables = ((OperationNode)inNode).getLeft();
        iterable = ((OperationNode)inNode).getRight();
    }

    @Override
    public void addToBody(TreeNode node) throws Exception {
        body.add(node);
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("For each");
        variables.debugPrint(level + 1);
        drawLevel(level);
        debugWriter.println("IN");
        iterable.debugPrint(level + 1);
        drawLevel(level);
        debugWriter.println("Body: ");
        for (TreeNode tn : body) {
            tn.debugPrint(level + 1);
        }
    }

    @Override
    public Command[] compile() {
        foreachLoopsCounter++;

        ArrayList<Command> res = new ArrayList<>();
        String iteratorVariableName = Integer.toString(foreachLoopsCounter) + "~it";
        res.add(new Command(Commands.CREATE_OR_PUSH, iteratorVariableName));
        res.addAll(Arrays.asList(iterable.compile()));
        res.add(new Command(Commands.CALL_FROM, "iterator", 0));
        res.add(new Command(Commands.ASSIGN));

        int condStart = res.size();
        res.add(new Command(Commands.PUSH, 'v', iteratorVariableName));
        res.add(new Command(Commands.CALL_FROM, "next?", 0));
        res.add(new Command(Commands.CONDITION));

        res.add(new Command(Commands.CREATE_OR_PUSH, ((VariableNode)variables).getName())); // TODO: need to change for tuples
        res.add(new Command(Commands.PUSH, 'v', iteratorVariableName));
        res.add(new Command(Commands.CALL_FROM, "next", 0));
        res.add(new Command(Commands.ASSIGN));

        for (TreeNode tn : body) {
            res.addAll(Arrays.asList(tn.compile()));
        }

        for (int i = 0; i < res.size(); i++)
            if (res.get(i).getCommand() == Commands.PARSE_CONTINUE) {
                res.set(i, new Command(Commands.GOTO, res.size() - i));
            }

        res.add(new Command(Commands.GOTO, -res.size() + condStart));
        res.add(new Command(Commands.END_CONDITION));

        for (int i = 0; i < res.size(); i++)
            if (res.get(i).getCommand() == Commands.PARSE_BREAK) {
                res.set(i, new Command(Commands.GOTO, res.size() - i));
            }

        foreachLoopsCounter++;
        return arrayListToArray(res);
    }
}
