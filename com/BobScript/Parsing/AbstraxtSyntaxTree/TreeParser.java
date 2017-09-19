package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Interpreter;
import com.BobScript.Parsing.*;

import java.util.ArrayList;

public class TreeParser {

    private ArrayList<TreeNode> tmp;

    public TreeParser() {
        tmp = new ArrayList<>();
    }

    public TreeNode createNode(Operand line) {
        tmp.clear();
        System.out.println(line);
        return init(line);
    }

    private TreeNode init(Operand line) {
        int index = line.next();

        if (line.size() == 0)
            return null;

        Token tk = line.get(index);

        if (tk.isForParsing()) {
            return tmp.get(Integer.parseInt(tk.getToken()));
        }

        if (tk.isDelimiter()) {
            switch (tk.getToken()) {
                case "+":
                case "*":
                case "-":
                case "<":
                case ">":
                case "==":
                case "=": {
                    Token left = line.get(index - 1);
                    Token right = line.get(index + 1);

                    OperationNode operation = new OperationNode(tk.getToken());
                    TreeNode nodeLeft, nodeRight;

                    if (!left.isForParsing()) {
                        if (Interpreter.tryInt(left.getToken()))
                            nodeLeft = new IntNode(Long.parseLong(left.getToken()));
                        else if (Interpreter.tryDouble(left.getToken()))
                            nodeLeft = new DoubleNode(Double.parseDouble(left.getToken()));
                        else
                            nodeLeft = new VariableNode(left.getToken());
                    }
                    else {
                        nodeLeft = tmp.get(Integer.parseInt(left.getToken()));
                    }

                    if (!right.isForParsing()) {
                        if (Interpreter.tryInt(right.getToken()))
                            nodeRight = new IntNode(Long.parseLong(right.getToken()));
                        else if (Interpreter.tryDouble(left.getToken()))
                            nodeRight = new DoubleNode(Double.parseDouble(right.getToken()));
                        else
                            nodeRight = new VariableNode(right.getToken());
                    }
                    else {
                        nodeRight = tmp.get(Integer.parseInt(right.getToken()));
                    }

                    operation.setLeft(nodeLeft);
                    operation.setRight(nodeRight);

                    line.set(index, new Token(Integer.toString(tmp.size()), Token.TokenTypes.FOR_PARSING, 0));
                    line.remove(index - 1);
                    line.remove(index);

                    tmp.add(operation);
                    return init(line);
                }
            }
        }
        return null;
    }
}
