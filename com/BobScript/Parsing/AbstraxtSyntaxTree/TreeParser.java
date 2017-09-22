package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.Parsing.*;
import com.BobScript.Parsing.AbstraxtSyntaxTree.ConstantAndVariableNodes.*;
import com.BobScript.Support.TypeSupport;

import java.util.ArrayList;
import java.util.Stack;

public class TreeParser {

    private ArrayList<TreeNode> tmp;

    private Stack<ComplexNode> currentParent;

    public TreeParser() {
        tmp = new ArrayList<>();
        currentParent = new Stack<>();
    }

    public TreeNode createNode(Operand line) {
        tmp.clear();
        //System.out.println(line);
        TreeNode tmpRes = init(line);
        //System.out.print(tmpRes + " " + currentParent.size());
        if (currentParent.empty())
            return tmpRes;
        else {
            TreeNode tmp = currentParent.peek();
            if (currentParent.peek() == tmpRes) {
                currentParent.pop();
            }
            //System.out.println(" " + currentParent.size());
            if (!currentParent.empty()) {
                if (tmpRes != null)
                    currentParent.peek().addToBody(tmpRes);
            }
            else {
                    return tmpRes;
            }
            return null;
        }
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
                case "=":
                case ",":{
                    Token left = line.get(index - 1);
                    Token right = line.get(index + 1);

                    OperationNode operation = new OperationNode(tk.getToken());
                    TreeNode nodeLeft, nodeRight;

                    if (!left.isForParsing()) {
                        if (TypeSupport.tryInt(left.getToken()))
                            nodeLeft = new IntNode(Long.parseLong(left.getToken()));
                        else if (TypeSupport.tryDouble(left.getToken()))
                            nodeLeft = new DoubleNode(Double.parseDouble(left.getToken()));
                        else if (TypeSupport.tryConstString(left.getToken()))
                            nodeLeft = new ConstStringNode(left.getToken());
                        else if (TypeSupport.tryNull(left.getToken()))
                            nodeLeft = new NullNode();
                        else if (TypeSupport.tryBoolean(left.getToken()))
                            nodeLeft = new BooleanNode(left.getToken().equals("true"));
                        else
                            nodeLeft = new VariableNode(left.getToken());
                    }
                    else {
                        nodeLeft = tmp.get(Integer.parseInt(left.getToken()));
                    }

                    if (!right.isForParsing()) {
                        if (TypeSupport.tryInt(right.getToken()))
                            nodeRight = new IntNode(Long.parseLong(right.getToken()));
                        else if (TypeSupport.tryDouble(right.getToken()))
                            nodeRight = new DoubleNode(Double.parseDouble(right.getToken()));
                        else if (TypeSupport.tryConstString(right.getToken()))
                            nodeRight = new ConstStringNode(right.getToken());
                        else if (TypeSupport.tryNull(right.getToken()))
                            nodeRight = new NullNode();
                        else if (TypeSupport.tryBoolean(right.getToken()))
                            nodeRight = new BooleanNode(right.getToken().equals("true"));
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
        else if (tk.isKeyword()) {
            switch (tk.getToken()) {
                case "while": {
                    line.remove(index);
                    WhileNode whileNode = new WhileNode(init(line));
                    currentParent.push(whileNode);
                    return null;
                }

                case "if": {
                    line.remove(index);
                    IfNode ifNode = new IfNode(init(line));
                    currentParent.push(ifNode);
                    return null;
                }

                case "end": {
                    return currentParent.peek();
                }
            }
        }

        return null;
    }
}
