package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;

import java.util.ArrayList;
import java.util.Arrays;

public class FunctionDeclarationNode extends ComplexNode {
    private String name;
    private ArrayList<TreeNode> body;
    private ArrayList<ArgumentInfo> arguments;
    private TreeNode objectNode;

    public static class ArgumentInfo {
        public String name, type;

        public ArgumentInfo(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    public FunctionDeclarationNode(String name) {
        this.name = name;
        body = new ArrayList<>();
        arguments = new ArrayList<>();
        objectNode = null;
    }

    public TreeNode getObjectNode() {
        return objectNode;
    }

    public void setObjectNode(TreeNode objectNode) {
        this.objectNode = objectNode;
    }

    public void addArgument(ArgumentInfo argName) {
        arguments.add(argName);
    }


    @Override
    public void addToBody(TreeNode node) {
        body.add(node);
    }

    @Override
    public void debugPrint(int level) {
        if (objectNode != null) {
            drawLevel(level);
            debugWriter.println("Object to function");
            objectNode.debugPrint(level + 1);
        }
        drawLevel(level);
        debugWriter.println("Function " + name);
        drawLevel(level);
        debugWriter.println("Arguments:");
        for (ArgumentInfo s : arguments) {
            drawLevel(level + 1);
            debugWriter.print(s.name);
            if (!s.type.equals("")) {
                debugWriter.println(" : " + s.type);
            }
            else
                debugWriter.println();
        }
        drawLevel(level);
        debugWriter.println("Body:");
        for (TreeNode tn : body) {
            tn.debugPrint(level + 1);
        }
    }

    @Override
    public Command[] compile() {
        ArrayList<Command> cmd = new ArrayList<>();
        if (objectNode != null) {
            cmd.addAll(Arrays.asList(objectNode.compile()));
        }
        for (int i = arguments.size() - 1; i >= 0; i--)
            cmd.add(new Command(Commands.ARGUMENT, arguments.get(i).name,
                    (!arguments.get(i).type.equals("") ? arguments.get(i).type : null)));
        if (objectNode != null) {
            name = "~m_" + name;
            cmd.add(new Command(Commands.FUNCTION, name, arguments.size(), 'm'));
        } else {
            cmd.add(new Command(Commands.FUNCTION, name, arguments.size()));
        }
        for (TreeNode tn : body) {
            cmd.addAll(Arrays.asList(tn.compile()));
        }
        cmd.add(new Command(Commands.END_FUNCTION));
        if (objectNode != null) {
            cmd.add(new Command(Commands.PUSH, 'v', name));
            cmd.add(new Command(Commands.ADD_METHOD, name));
        }
        return arrayListToArray(cmd);
    }
}
