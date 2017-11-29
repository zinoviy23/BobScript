package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Класс для представления else
 */
public class ElseNode extends ComplexNode implements Parentable {
    private ArrayList<TreeNode> body;
    private IfNode parent;
    /**
     * Конструктор без параметров, другого не надо
     */
    public ElseNode(IfNode parent) {
        body = new ArrayList<>();
        this.parent = parent;
    }

    @Override
    public TreeNode findRoot() {
        return parent.findRoot();
    }

    @Override
    public void addToBody(TreeNode node) {
        body.add(node);
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("Else:");
        drawLevel(level);
        debugWriter.println("Body:");
        for (TreeNode tn : body) {
            tn.debugPrint(level + 1);
        }
    }

    @Override
    public Command[] compile() {
        ArrayList<Command> cmd = new ArrayList<>();

        for (TreeNode tn : body) {
            cmd.addAll(Arrays.asList(tn.compile()));
        }

        return arrayListToArray(cmd);
    }
}
