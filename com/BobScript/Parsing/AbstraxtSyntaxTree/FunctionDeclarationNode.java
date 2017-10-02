package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;

import java.util.ArrayList;
import java.util.Arrays;

public class FunctionDeclarationNode extends ComplexNode {
    private String name;
    private ArrayList<TreeNode> body;
    private ArrayList<String> arguments;

    public FunctionDeclarationNode(String name) {
        this.name = name;
        body = new ArrayList<>();
        arguments = new ArrayList<>();
    }

    public void addArgument(String argName) {
        arguments.add(argName);
    }


    @Override
    public void addToBody(TreeNode node) {
        body.add(node);
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("Function " + name);
        drawLevel(level);
        debugWriter.println("Arguments:");
        for (String s : arguments) {
            drawLevel(level + 1);
            debugWriter.println(s);
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
        for (int i = arguments.size() - 1; i >= 0; i--)
            cmd.add(new Command(Commands.ARGUMENT, arguments.get(i)));
        cmd.add(new Command(Commands.FUNCTION, name, arguments.size()));
        for (TreeNode tn : body) {
            cmd.addAll(Arrays.asList(tn.compile()));
        }
        cmd.add(new Command(Commands.END_FUNCTION, null));
        return arrayListToArray(cmd);
    }
}
