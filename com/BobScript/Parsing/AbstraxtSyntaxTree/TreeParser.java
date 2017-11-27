package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Interpreter;
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
        TreeNode tmpRes = init(line);
        if (currentParent.empty())
            return tmpRes;
        else {
            TreeNode tmp = currentParent.peek();
            if (currentParent.peek() == tmpRes) {
                currentParent.pop();
            }
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
        //System.out.println(line);
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
                case "+=":
                case ",":
                case ".":
                case ":": {
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
                        else {
                            //System.out.println(left.getToken());
                            if (left.isDelimiter())
                                System.out.println("это левый унарный оператор, они пока не поддреживаются");
                            nodeLeft = new VariableNode(left.getToken());
                        }
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
                case "(": {
                    int closeIndex = line.getCloseParenthesis(index);
                    Token nameToken = line.get(index - 1);
                    Operand arguments = line.extractFromParenthesis(index, closeIndex);
                    FunctionCallNode fcn = new FunctionCallNode(nameToken.getToken());
                    TreeNode kek = init(arguments);
                    //kek.debugPrint(0);
                    fcn.setComaNode(kek);
                    line.removeAll(index - 1, closeIndex);
                    line.set(index - 1, new Token(Integer.toString(tmp.size()), Token.TokenTypes.FOR_PARSING, 0));
                    tmp.add(fcn);
                    //fcn.debugPrint(0);
                    return init(line);
                }
                case "[": {
                    int closeIndex = line.getCloseBoxParenthesis(index);
                    Operand elements = line.extractFromParenthesis(index, closeIndex);
                    if (index > 0 && !line.get(index - 1).isDelimiter()) {
                        TreeNode name;
                        if (line.get(index - 1).isForParsing()) {
                            name = tmp.get(Integer.parseInt(line.get(index - 1).getToken()));
                        }
                        else {
                            name = new VariableNode(line.get(index - 1).getToken());
                        }
                        ArrayElementNode aen = new ArrayElementNode(name);
                        TreeNode indexes = init(elements);
                        aen.setComaNode(indexes);
                        line.removeAll(index - 1, closeIndex);
                        line.set(index - 1, new Token(Integer.toString(tmp.size()), Token.TokenTypes.FOR_PARSING, 0));
                        tmp.add(aen);
                    }
                    else {
                        //System.out.println(index + " " + closeIndex);
                        //System.out.println(elements);
                        ConstArrayNode can = new ConstArrayNode();
                        TreeNode elementsNode = init(elements);
                        //elementsNode.debugPrint(0);
                        can.setComaNode(elementsNode);
                        line.removeAll(index, closeIndex);
                        line.set(index, new Token(Integer.toString(tmp.size()), Token.TokenTypes.FOR_PARSING, 0));
                        tmp.add(can);
                    }
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

                case "for": {
                    line.remove(index);
                    Operand[] statements = line.split(";");
                    Operand condition = statements[1];
                    Operand change = statements[2];
                    Operand initSt = statements[0];
                    ForNode forNode = new ForNode(init(condition));
                    forNode.setInitializationState(init(initSt));
                    forNode.setChangeStatement(init(change));
                    currentParent.push(forNode);
                    return null;
                }

                case "if": {
                    line.remove(index);
                    IfNode ifNode = new IfNode(init(line));
                    currentParent.push(ifNode);
                    return null;
                }

                case "func": {
                    String name = line.get(index + 1).getToken();
                    FunctionDeclarationNode fdn = new FunctionDeclarationNode(name);
                    int closeIndex = line.getCloseParenthesis(index + 2);
                    Operand arguments = line.extractFromParenthesis(index + 2, closeIndex);
                    for (int i = 0; i < arguments.size(); i++) {
                        if (!arguments.get(i).getToken().equals(","))
                            fdn.addArgument(arguments.get(i).getToken());
                    }
                    currentParent.push(fdn);
                    line.removeAll(index, closeIndex);
                    return null;
                }

                case "return": {
                    line.remove(index);
                    ReturnNode returnNode = new ReturnNode(init(line));
                    return returnNode;
                }

                case "array": {
                    Operand dimensions = line.extractFrom(index + 1, line.size() - 1);
                    ArrayKeywordNode node = new ArrayKeywordNode(init(dimensions));
                    line.removeAll(index + 1, line.size());
                    line.set(index, new Token(Integer.toString(tmp.size()), Token.TokenTypes.FOR_PARSING, 0));
                    //System.err.println(line);
                    tmp.add(node);
                    return init(line);
                }

                case "end": {
                    return currentParent.peek();
                }
            }
        } else {
            TreeNode node;
            if (TypeSupport.tryInt(tk.getToken()))
                node = new IntNode(Long.parseLong(tk.getToken()));
            else if (TypeSupport.tryDouble(tk.getToken()))
                node = new DoubleNode(Double.parseDouble(tk.getToken()));
            else if (TypeSupport.tryConstString(tk.getToken()))
                node = new ConstStringNode(tk.getToken());
            else if (TypeSupport.tryNull(tk.getToken()))
                node = new NullNode();
            else if (TypeSupport.tryBoolean(tk.getToken()))
                node = new BooleanNode(tk.getToken().equals("true"));
            else
                node = new VariableNode(tk.getToken());
            return node;
        }

        return null;
    }

}
