package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;

import java.util.ArrayList;
import java.util.Arrays;

public class ClassDeclarationNode extends ComplexNode {
    private ArrayList<TreeNode> body;
    private String name;

    public ClassDeclarationNode(String name) {
        this.name = name;
        this.body = new ArrayList<>();
    }

    @Override
    public void addToBody(TreeNode node) {
        body.add(node);
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("Class: " + name);
        drawLevel(level);
        debugWriter.println("Body:");
        for (TreeNode tn : body)
            tn.debugPrint(level + 1);
    }

    @Override
    public Command[] compile() {
        ArrayList<Command> res = new ArrayList<>();
        res.add(new Command(Commands.CLASS, name));
        for (TreeNode tn : body) {
            res.addAll(Arrays.asList(tn.compile()));
        }
        res.add(new Command(Commands.END_CLASS));
        return arrayListToArray(res);
    }
}
