package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Класс для представления цикла фор в AST
 */
public class ForNode extends ComplexNode {
    private ArrayList<TreeNode> body;  // тело цикла
    private TreeNode condition;  // условие цикла
    private TreeNode initializationStatement;  // иницилизации в for
    private TreeNode changeStatement;
    private String name;

    private ArrayList<TreeNode> initializations;
    private ArrayList<TreeNode> changes;

    public ForNode(TreeNode condition, String name) {
        this.body = new ArrayList<>();
        this.condition = condition;
        initializations = new ArrayList<>();
        changes = new ArrayList<>();
        this.name = name;
    }



    public void setInitializationState(TreeNode node) {
        initializationStatement = node;
        initInitializations(node);
    }

    private void initInitializations(TreeNode comaNode) {
        if (comaNode == null)
            return;
        if (comaNode instanceof OperationNode) {
            OperationNode tmp = (OperationNode)comaNode;
            if (tmp.getOperation().equals(",")) {
                initInitializations(tmp.getLeft());
                initializations.add(tmp.getRight());
            }
            else
                initializations.add(tmp);
        }
        else
            initializations.add(comaNode);
    }

    public void setChangeStatement(TreeNode node) {
        changeStatement = node;
        initChanges(node);
    }

    private void initChanges(TreeNode comaNode) {
        if (comaNode == null)
            return;
        if (comaNode instanceof OperationNode) {
            OperationNode tmp = (OperationNode)comaNode;
            if (tmp.getOperation().equals(",")) {
                initChanges(tmp.getLeft());
                changes.add(tmp.getRight());
            }
            else
                changes.add(tmp);
        }
        else
            changes.add(comaNode);
    }

    @Override
    public void addToBody(TreeNode node) {
        body.add(node);
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("For loop" + ((name == null) ? "" :  " : " + name));

        drawLevel(level);
        debugWriter.println("Init st:");
        for (TreeNode tn : initializations) {
            tn.debugPrint(level + 1);
        }

        drawLevel(level);
        debugWriter.println("Condition:");
        condition.debugPrint(level + 1);
        drawLevel(level);

        debugWriter.println("Change st:");
        for (TreeNode tn : changes)
            tn.debugPrint(level + 1);

        drawLevel(level);
        debugWriter.println("Body:");
        for (TreeNode tn : body)
            tn.debugPrint(level + 1);
    }

    /**
     * Компилирует как:
     * initializationStatement
     * condition
     *  loop
     *  changeStatement
     * delete loopVariables
     * @return
     */
    @Override
    public Command[] compile() {
        ArrayList<Command> res = new ArrayList<>();

        for (TreeNode tn : initializations) {
            res.addAll(Arrays.asList(tn.compile()));
        }
        int condInd = res.size() - 1;

        Command[] condCom = condition.compile();
        res.addAll(Arrays.asList(condCom));
        res.add(new Command(Commands.CONDITION));

        for (TreeNode tn : body) {
            res.addAll(Arrays.asList(tn.compile()));
        }

        for (int i = 0; i < res.size(); i++)
            if (res.get(i).getCommand() == Commands.PARSE_CONTINUE) {
                res.set(i, new Command(Commands.GOTO, res.size() - i));
            }

        for (TreeNode tn : changes) {
            res.addAll(Arrays.asList(tn.compile()));
        }

        //Command[] changeCom = changeStatement.compile();
        //res.addAll(Arrays.asList(changeCom));

        res.add(new Command(Commands.GOTO, -res.size() + condInd + 1));
        res.add(new Command(Commands.END_CONDITION));

        for (int i = 0; i < res.size(); i++)
            if (res.get(i).getCommand() == Commands.PARSE_BREAK &&
                    (res.get(i).getArgs()[0].equals(name) || res.get(i).getArgs().length == 0)) {
                res.set(i, new Command(Commands.GOTO, res.size() - i));
            }

        return arrayListToArray(res);
    }
}
