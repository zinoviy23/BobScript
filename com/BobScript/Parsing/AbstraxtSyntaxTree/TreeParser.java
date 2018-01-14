package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.Parsing.*;
import com.BobScript.Parsing.AbstraxtSyntaxTree.ConstantAndVariableNodes.*;
import com.BobScript.Support.TypeSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class TreeParser {

    private ArrayList<TreeNode> tmp;

    private Stack<ComplexNode> currentParent;

    // строка, в которой был do
    private Operand doSavedLine;
    // рассматривается ли сейчас do блок
    private boolean isDoBlock = false;

    // пропущена ли последняя строка
    private boolean isPreviousLinePass = false;
    // пропущенная строка
    private Operand passedLine;

    public TreeParser() {
        tmp = new ArrayList<>();
        currentParent = new Stack<>();
    }

    public TreeNode createNode(Operand line) {
        //tmp.clear();
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
        int index = line.next();

        if (line.size() == 0)
            return null;

        Token tk = line.get(index);

        if (tk.isForParsing()) {
            return tmp.get(Integer.parseInt(tk.getToken()));
        }

        if (!tk.getToken().equals("\\\\") && isPreviousLinePass) {
            isPreviousLinePass = false;
            passedLine.addTokens(line);
            return init(passedLine);
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
                case "/":
                case "%":
                case ",":
                case ".":
                case "++":
                case ":":
                case "..": {
                    Token left = line.get(index - 1);
                    Token right = line.get(index + 1);

                    TreeNode nodeLeft = null, nodeRight = null;
                    boolean isUnaryLeft = false;
                    if (left == null)
                        isUnaryLeft = true;
                    else if (!left.isForParsing()) {
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
                            if (left.isDelimiter()) {
                                isUnaryLeft = true;
                            }
                            else
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
                    TreeNode operation;
                    if (isUnaryLeft) {
                        operation = new UnaryNode(nodeRight, tk.getToken());;
                    }
                    else {
                        OperationNode op = new OperationNode(tk.getToken());
                        op.setRight(nodeRight);
                        op.setLeft(nodeLeft);
                        operation = op;
                    }


                    line.set(index, new Token(Integer.toString(tmp.size()), Token.TokenTypes.FOR_PARSING, 0));
                    if (!isUnaryLeft) {
                        line.remove(index - 1);
                        line.remove(index);
                    }
                    else {
                        line.remove(index + 1);
                    }
                    tmp.add(operation);
                    return init(line);
                }
                // лямбда операция
                case "->": {
                    //System.out.println(line);
                    ArrayList<FunctionDeclarationNode.ArgumentInfo> argumentInfo = new ArrayList<>();
                    int argStartIndex;
                    for (argStartIndex = index - 2; argStartIndex >= 0; argStartIndex--) {
                        if (line.get(argStartIndex).getToken().equals("|"))
                            break;
                    }

                    Operand arguments = line.extractFrom(argStartIndex + 1, index - 2);
                    //System.out.println(arguments);
                    for (int i = 0; i < arguments.size(); i++) {
                        if (!arguments.get(i).getToken().equals(",")) {
                            if (i < arguments.size() - 2 && arguments.get(i + 1).getToken().equals(":")) {
                                argumentInfo.add(new FunctionDeclarationNode.ArgumentInfo(
                                   arguments.get(i).getToken(),
                                   arguments.get(i + 2).getToken()
                                ));
                                i+=2;
                            }
                            else {
                                argumentInfo.add(new FunctionDeclarationNode.ArgumentInfo(
                                        arguments.get(i).getToken(), ""
                                ));
                            }
                        }
                    }

                    //Collections.reverse(argumentInfo);
                    LambdaFunctionNode lfn = new LambdaFunctionNode(argumentInfo, init(line.extractFrom(index + 1, line.size() - 1)));
                    line.removeAll(index + 1, line.size());
                    line.set(index, new Token(Integer.toString(tmp.size()), Token.TokenTypes.FOR_PARSING, 0));
                    tmp.add(lfn);
                    line.removeAll(argStartIndex, index);
                    return init(line);
                }
                // операция переноса строки
                case "\\\\": {
                    if (!isPreviousLinePass) {
                        passedLine = line.extractFrom(0, index - 1);
                        isPreviousLinePass = true;
                    }
                    else {
                        passedLine.addTokens(line.extractFrom(0, index - 1));
                    }
                    return null;
                }
                case "(": {
                    int closeIndex = line.getCloseParenthesis(index);
                    Token nameToken = line.get(index - 1);
                    Operand arguments = line.extractFromParenthesis(index, closeIndex);
                    FunctionCallNode fcn = new FunctionCallNode(nameToken.getToken());
                    TreeNode kek = init(arguments);
                    fcn.setComaNode(kek);
                    line.removeAll(index - 1, closeIndex);
                    line.set(index - 1, new Token(Integer.toString(tmp.size()), Token.TokenTypes.FOR_PARSING, 0));
                    tmp.add(fcn);
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

                case "break": {
                    return new BreakNode();
                }

                case "continue": {
                    return new ContinueNode();
                }

                case "if": {
                    line.remove(index);
                    IfNode ifNode = new IfNode(init(line), null);
                    currentParent.push(ifNode);
                    return null;
                }

                case "elif": {
                    IfNode parent = (IfNode)currentParent.pop();
                    line.remove(index);
                    IfNode ifNode = new IfNode(init(line), parent);
                    parent.setElseNode(ifNode);
                    currentParent.push(ifNode);
                    return null;
                }

                case "else": {
                    IfNode parent = (IfNode)currentParent.pop();
                    line.remove(index);
                    ElseNode elseNode = new ElseNode(parent);
                    parent.setElseNode(elseNode);
                    currentParent.push(elseNode);
                    return null;
                }

                case "func": {
                    String name = line.get(index + 1).getToken();
                    FunctionDeclarationNode fdn = new FunctionDeclarationNode(name);
                    int closeIndex = line.getCloseParenthesis(index + 2);
                    Operand arguments = line.extractFromParenthesis(index + 2, closeIndex);
                    for (int i = 0; i < arguments.size(); i++) {
                        if (!arguments.get(i).getToken().equals(",")) {
                            if (i < arguments.size() - 2 && arguments.get(i + 1).getToken().equals(":")) {
                                fdn.addArgument(new FunctionDeclarationNode.ArgumentInfo(
                                        arguments.get(i).getToken(),
                                        arguments.get(i + 2).getToken()
                                ));
                                i+=2;
                            }
                            else {
                                fdn.addArgument(new FunctionDeclarationNode.ArgumentInfo(
                                        arguments.get(i).getToken(),
                                        ""
                                ));
                            }
                        }
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
                    tmp.add(node);
                    return init(line);
                }

                case "do": {
                    //System.out.println("Line: " + line + "\n");
                    isDoBlock = true;
                    doSavedLine = line.extractFrom(0, index - 1);
                    currentParent.add(new DoBlockNode());
                    return null;
                }

                case "end": {
                    //System.out.println("LineEnd: " + line + "\n");
                    if (currentParent.peek() instanceof Parentable) {
                        return ((Parentable)currentParent.pop()).findRoot();
                    }
                    if (isDoBlock && currentParent.peek() instanceof DoBlockNode) {
                        isDoBlock = false;
                        TreeNode doBlockNode = currentParent.pop();
                        doSavedLine.addToken(new Token(Integer.toString(tmp.size()), Token.TokenTypes.FOR_PARSING, 0));
                        for (int i = index + 1; i < line.size(); i++)
                            doSavedLine.addToken(line.get(i));
                        tmp.add(doBlockNode);
                        //System.out.println("LineKek: " + doSavedLine + "\n");
                        return init(doSavedLine);
                    }
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
