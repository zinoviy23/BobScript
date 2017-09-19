package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.Parsing.*;

import java.util.ArrayList;

public class TreeParser {

    private ArrayList<TreeNode> tmp;

    private TreeNode result;

    public TreeParser() {
        tmp = new ArrayList<>();
    }

    public TreeNode createNode(Operand line) {
        tmp.clear();
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
                case "+": {
                    Token left = line.get(index - 1);
                    Token right = line.get(index + 1);

                    OperationNode plusOperation = new OperationNode("+");
                    TreeNode nodeLeft, nodeRight;

                    if (!left.isForParsing()) {
                        nodeLeft = new IntNode(Integer.parseInt(left.getToken()));
                    }
                    else {
                        nodeLeft = tmp.get(Integer.parseInt(left.getToken()));
                    }

                    if (!right.isForParsing()) {
                        nodeRight = new IntNode(Integer.parseInt(right.getToken()));
                    }
                    else {
                        nodeRight = tmp.get(Integer.parseInt(right.getToken()));
                    }

                    plusOperation.setLeft(nodeLeft);
                    plusOperation.setRight(nodeRight);

                    line.set(index, new Token(Integer.toString(tmp.size()), Token.TokenTypes.FOR_PARSING, 0));
                    line.remove(index - 1);
                    line.remove(index);

                    tmp.add(plusOperation);
                    return init(line);
                }

                case "*":{
                    Token left = line.get(index - 1);
                    Token right = line.get(index + 1);

                    OperationNode multOperation = new OperationNode("*");
                    TreeNode nodeLeft, nodeRight;

                    if (!left.isForParsing()) {
                        nodeLeft = new IntNode(Integer.parseInt(left.getToken()));
                    }
                    else {
                        nodeLeft = tmp.get(Integer.parseInt(left.getToken()));
                    }

                    if (!right.isForParsing()) {
                        nodeRight = new IntNode(Integer.parseInt(right.getToken()));
                    }
                    else {
                        nodeRight = tmp.get(Integer.parseInt(right.getToken()));
                    }

                    multOperation.setLeft(nodeLeft);
                    multOperation.setRight(nodeRight);

                    line.set(index, new Token(Integer.toString(tmp.size()), Token.TokenTypes.FOR_PARSING, 0));
                    line.remove(index - 1);
                    line.remove(index);

                    tmp.add(multOperation);
                    return init(line);
                }
            }
        }
        return null;
    }



    public TreeNode getNode() {
        return result;
    }

}
