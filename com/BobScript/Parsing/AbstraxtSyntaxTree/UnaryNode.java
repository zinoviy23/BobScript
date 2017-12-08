package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;
import com.BobScript.Parsing.AbstraxtSyntaxTree.ConstantAndVariableNodes.DoubleNode;
import com.BobScript.Parsing.AbstraxtSyntaxTree.ConstantAndVariableNodes.IntNode;
import com.BobScript.Parsing.AbstraxtSyntaxTree.ConstantAndVariableNodes.VariableNode;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Класс для представления унарных операций в AST
 */
public class UnaryNode extends TreeNode {
    private TreeNode value;
    private String operation;

    public UnaryNode(TreeNode value, String operation) {
        this.value = value;
        this.operation = operation;
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("Unary operation: " + operation);
        value.debugPrint(level + 1);
    }

    @Override
    public Command[] compile() {
        switch (operation) {
            case "-": {
                if (value instanceof IntNode) {
                    IntNode tmp = (IntNode)value;
                    value = new IntNode(-tmp.getValue());
                    return value.compile();
                }
                else if (value instanceof DoubleNode) {
                    DoubleNode tmp = (DoubleNode)value;
                    value = new DoubleNode(-tmp.getValue());
                    return value.compile();
                }
                else {
                    ArrayList<Command> commands = new ArrayList<>();
                    commands.addAll(Arrays.asList(value.compile()));
                    commands.add(new Command(Commands.UNARY_MINUS));
                    return arrayListToArray(commands);
                }
            }
            // где-то нужна проверка на переменная это или нет
            case "++": {
                ArrayList<Command> commands = new ArrayList<>();
                commands.addAll(Arrays.asList(value.compile()));
                commands.add(new Command(Commands.INCREMENT));
                return arrayListToArray(commands);
            }
        }
        return new Command[0];
    }
}
