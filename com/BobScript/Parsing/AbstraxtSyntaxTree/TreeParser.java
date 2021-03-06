package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.Parsing.*;
import com.BobScript.Parsing.AbstraxtSyntaxTree.ConstantAndVariableNodes.*;
import com.BobScript.Support.TypeSupport;

import java.util.ArrayList;
import java.util.Stack;

public class TreeParser {

    private ArrayList<TreeNode> tmp;

    private Stack<ComplexNode> currentParent;

    // строка, в которой был do
    private Stack<Operand> doSavedLineStack;

    // пропущена ли последняя строка
    private boolean isPreviousLinePass = false;
    // пропущенная строка
    private Operand passedLine;

    public TreeParser(FileNode root) {
        tmp = new ArrayList<>();
        currentParent = new Stack<>();
        currentParent.push(root);
        doSavedLineStack = new Stack<>();
    }


    public FileNode getFile() {
        return (FileNode) currentParent.firstElement();
    }

    public TreeNode createNode(Operand line) {  // TODO: big refactoring
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
                    try {

                        currentParent.peek().addToBody(tmpRes);
                    } catch (Exception ex) {
                        if (currentParent.peek() instanceof Parentable) {
                            if (currentParent.size() == 1)
                                return ((Parentable) currentParent.pop()).findRoot();
                            else {
                                TreeNode currentNode = ((Parentable) currentParent.pop()).findRoot();
                                try {
                                    currentParent.peek().addToBody(currentNode);  // TODO: needs some refactoring and fixes
                                    currentParent.peek().addToBody(tmpRes);
                                } catch (Exception ex1) {
                                    ex1.printStackTrace();
                                }
                            }
                        }
                        else
                            ex.printStackTrace();
                    }
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
                case "..":
                case "in": {
                    Token left = line.get(index - 1);
                    Token right = line.get(index + 1);

                    TreeNode nodeLeft = null, nodeRight = null;
                    boolean isUnaryLeft = false;
                    if (left == null || tk.getToken().equals("++"))
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
                        operation = new UnaryNode(nodeRight, tk.getToken());
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
                    boolean isVoid = false;
                    if (index < line.size() - 1 && line.get(index + 1).getToken().equals("void")) {
                        isVoid = true;
                        line.remove(index + 1);
                    }

                    LambdaFunctionNode lfn = new LambdaFunctionNode(argumentInfo, init(line.extractFrom(index + 1, line.size() - 1)));
                    lfn.setVoid(isVoid);
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
                    init(line);
                    //System.out.println(line);
                    Operand condition = line.extractFrom(0, 0);
                    WhileNode whileNode = new WhileNode(init(condition));
                    currentParent.push(whileNode);
                    if (line.size() == 1) {
                        return null;
                    }
                    else {
                        Operand body = line.extractFrom(1, 1);
                        whileNode.addToBody(init(body));
                        return currentParent.peek();
                    }
                }

                case "for": {
                    line.remove(index);
                    String name = null;
                    if (line.get(index).getToken().equals(":")) {
                        name = line.get(index + 1).getToken();
                        line.removeAll(index, index + 2);
                    }
                    Operand[] statements = line.split(";");

                    if (statements.length == 3) {
                        Operand condition = statements[1];
                        Operand change = statements[2];
                        Operand initSt = statements[0];
                        ForNode forNode = new ForNode(init(condition), name);
                        forNode.setInitializationState(init(initSt));
                        currentParent.push(forNode);
                        init(change);
                        if (change.size() == 1)
                            forNode.setChangeStatement(init(change));
                        else {
                            Operand body = change.extractFrom(1, 1);
                            change = change.extractFrom(0, 0);
                            forNode.setChangeStatement(init(change));
                            forNode.addToBody(init(body));
                            return currentParent.peek();
                        }
                    } else {
                        init(line);
                        Operand inOperation = line.extractFrom(0, 0);
                        ForEachNode forEachNode = new ForEachNode(init(inOperation));
                        currentParent.push(forEachNode);
                        if (line.size() == 1) {
                            return null;
                        } else {
                            Operand body = line.extractFrom(1, 1);
                            try {
                                forEachNode.addToBody(init(body));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            return currentParent.peek();
                        }
                    }
                    return null;
                }

                case "break": {
                    BreakNode node;
                    if (line.size() > index + 1) {
                        node = new BreakNode(line.get(index + 1).getToken());
                        line.remove(index + 1);
                    } else {
                        node = new BreakNode();
                    }
                    line.set(index, new Token(Integer.toString(tmp.size()), Token.TokenTypes.FOR_PARSING, 0));
                    tmp.add(node);
                    return init(line);
                }

                case "continue": {
                    return new ContinueNode();
                }

                case "if": {
                    line.remove(index);
                    init(line);
                    if (line.size() == 1) {
                        IfNode ifNode = new IfNode(init(line), null);
                        currentParent.push(ifNode);
                        return null;
                    } else {
                        Operand condition = line.extractFrom(0, 0);
                        Operand body = line.extractFrom(1, 1);
                        IfNode ifNode = new IfNode(init(condition), null);
                        try {
                            ifNode.addToBody(init(body));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        ifNode.setOneLineBlock(true);
                        currentParent.push(ifNode);
                        return null;
                    }
                }

                case "elif": {
                    IfNode parent = (IfNode)currentParent.pop();
                    line.remove(index);
                    IfNode ifNode;
                    init(line);
                    if (line.size() == 1) {
                        ifNode = new IfNode(init(line), parent);
                    } else {
                        Operand condition = line.extractFrom(0, 0);
                        Operand body = line.extractFrom(1, 1);
                        ifNode = new IfNode(init(condition), parent);
                        try {
                            ifNode.addToBody(init(body));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        ifNode.setOneLineBlock(true);
                    }
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
                    if (line.size() == 0) {
                        return null;
                    } else {
                        elseNode.addToBody(init(line));
                        return ((Parentable)currentParent.pop()).findRoot();
                    }
                }

                case "func": {
                    //System.out.println(line);
                    String name = line.get(index + 1).getToken();
                    FunctionDeclarationNode fdn;
                    int nameEndIndex;
                    if (!line.get(index + 2).getToken().equals(".")) {
                        fdn = new FunctionDeclarationNode(name);
                        nameEndIndex = index + 2;

                    } else {
                        Operand argName = new Operand(name);

                        for (nameEndIndex = index + 2; nameEndIndex < line.size(); nameEndIndex++) {
                            if (line.get(nameEndIndex).getToken().equals("("))
                                break;
                            argName.addToken(line.get(nameEndIndex));
                        }
                        String currentName = argName.get(argName.size() - 1).getToken();
                        argName.removeAll(argName.size() - 2, argName.size());
                        fdn = new FunctionDeclarationNode(currentName);
                        fdn.setObjectNode(init(argName));
                    }

                    int closeIndex = line.getCloseParenthesis(nameEndIndex);
                    Operand arguments = line.extractFromParenthesis(nameEndIndex, closeIndex);
                    //System.out.println(arguments);
                    for (int i = 0; i < arguments.size(); i++) {
                        if (!arguments.get(i).getToken().equals(",")) {
                            if (i < arguments.size() - 2 && arguments.get(i + 1).getToken().equals(":")) {
                                fdn.addArgument(new FunctionDeclarationNode.ArgumentInfo(
                                        arguments.get(i).getToken(),
                                        arguments.get(i + 2).getToken()
                                ));
                                i += 2;
                            } else {
                                fdn.addArgument(new FunctionDeclarationNode.ArgumentInfo(
                                        arguments.get(i).getToken(),
                                        ""
                                ));
                            }
                        }
                    }
                    currentParent.push(fdn);
                    line.removeAll(index, closeIndex + 1);
                    if (line.size() > 0) {
                        if (!line.get(0).getToken().equals("void")) {
                            Operand newLine = new Operand("return");
                            newLine.addTokens(line);
                            line = newLine;
                        }
                        else
                            line.remove(0);
                        //System.out.println(line);
                        fdn.addToBody(init(line));
                        return currentParent.peek();
                    }
                    return null;
                }

                case "return": {
                    line.remove(index);
                    return new ReturnNode(init(line));
                }

                case "class": {
                    String name = line.get(index + 1).getToken();
                    ClassDeclarationNode cdn = new ClassDeclarationNode(name);
                    currentParent.push(cdn);
                    return null;
                }

                case "field": {
                    ArrayList<String> fields = new ArrayList<>();
                    for (int i = index + 1; i < line.size(); i++) {
                        if (!line.get(i).getToken().equals(","))
                            fields.add(line.get(i).getToken());
                    }

                    FieldNode fieldNode = new FieldNode();
                    fieldNode.setFields(fields);
                    return fieldNode;
                }

                case "new": {
                    //System.out.println(line);
                    String className = line.get(index + 1).getToken();  // TODO: in the future there is must be dot cheking and etc
                    NewNode newNode = new NewNode(className);


                    int openParenthesisIndex;
                    for (openParenthesisIndex = index + 2; openParenthesisIndex < line.size(); openParenthesisIndex++) {
                        if (line.get(openParenthesisIndex).getToken().equals("("))
                            break;
                    }

                    int closeIndex = line.getCloseParenthesis(openParenthesisIndex);
                    Operand arguments = line.extractFromParenthesis(openParenthesisIndex, closeIndex);
                    //System.out.println("Arguments" + arguments);
                    TreeNode comaNode = init(arguments);
                    newNode.initArguments(comaNode);

                    line.set(index, new Token(Integer.toString(tmp.size()), Token.TokenTypes.FOR_PARSING, 0));
                    tmp.add(newNode);

                    line.removeAll(index + 1, closeIndex + 1);
                    //System.out.println(line);
                    return init(line);
                }

                case "array": {
                    Operand dimensions = line.extractFrom(index + 1, line.size() - 1);
                    ArrayKeywordNode node = new ArrayKeywordNode(init(dimensions));
                    line.removeAll(index + 1, line.size());
                    line.set(index, new Token(Integer.toString(tmp.size()), Token.TokenTypes.FOR_PARSING, 0));
                    tmp.add(node);
                    return init(line);
                }

                case "pass": {
                    return new PassNode();
                }

                case "do": {
                    doSavedLineStack.push(line.extractFrom(0, index - 1));
                    currentParent.add(new DoBlockNode());
                    return null;
                }

                case "end": {
                    if (currentParent.peek() instanceof FileNode) {  // TODO: exception
                        System.err.println("Unexpected end!");
                        return null;
                    }
                    if (currentParent.peek() instanceof Parentable) {
                        if (currentParent.peek() instanceof IfNode && ((IfNode)currentParent.peek()).isOneLineBlock()) {
                            TreeNode tmp = ((Parentable)currentParent.pop()).findRoot();
                            try {
                                currentParent.peek().addToBody(tmp);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            return  currentParent.peek();
                        }
                        return ((Parentable)currentParent.pop()).findRoot();
                    }
                    if (!doSavedLineStack.empty() && currentParent.peek() instanceof DoBlockNode) {
                        Operand doSavedLine = doSavedLineStack.pop();
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
