package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;

import java.util.ArrayList;
import java.util.Arrays;

public class FileNode extends ComplexNode {
    private ArrayList<TreeNode> body;

    private String fileName;

    public FileNode(String fileName) {
        this.fileName = fileName;
        body = new ArrayList<>();
    }

    @Override
    public void addToBody(TreeNode node) {
        body.add(node);
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("File: " + fileName);
        for (TreeNode tn : body) {
            tn.debugPrint(level + 1);
        }
    }

    @Override
    public Command[] compile() {
        ArrayList<Command> res = new ArrayList<>();
        for (TreeNode tn : body)
            res.addAll(Arrays.asList(tn.compile()));

        return arrayListToArray(res);
    }
}
