package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;
import com.BobScript.Parsing.AbstraxtSyntaxTree.ConstantAndVariableNodes.VariableNode;
import com.BobScript.Support.TypeSupport;

import java.util.ArrayList;
import java.util.Arrays;

public class OperationNode extends TreeNode {
    private TreeNode left, right;
    private String operation;

    public OperationNode(String operation) {
        this.left = null;
        this.right = null;
        this.operation = operation;
    }

    public TreeNode getLeft() {
        return left;
    }

    public TreeNode getRight() {
        return right;
    }

    public String getOperation() {
        return operation;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return operation + ": {" + left + "; " + right + "}";
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println(operation + ":");
        left.debugPrint(level + 1);
        right.debugPrint(level + 1);
    }



    @Override
    public Command[] compile() {
        ArrayList<Command> res = new ArrayList<>();
        if (TypeSupport.isArithmeticOperation(operation))
            res.addAll(Arrays.asList(left.compile()));
        res.addAll(Arrays.asList(right.compile()));
        switch (operation) {
            case "+":
                res.add(new Command(Commands.ADD, ""));
                break;
            case "*":
                res.add(new Command(Commands.MULT, ""));
                break;
            case "<":
                res.add(new Command(Commands.LESSER, ""));
                break;
            case ">":
                res.add(new Command(Commands.GREATER, ""));
                break;
            case "==":
                res.add(new Command(Commands.EQUAL, ""));
                break;
            case "=":
                res.add(new Command(Commands.ASSIGN, ((VariableNode)left).getName()));
                break;
        }
        return arrayListToArray(res);
    }
}
