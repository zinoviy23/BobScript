package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Класс для ключевого слова array
 */
public class ArrayKeywordNode extends TreeNode{
    private TreeNode dimensions;


    public ArrayKeywordNode(TreeNode dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("Dimensional array");
        dimensions.debugPrint(level + 1);
    }

    @Override
    public Command[] compile() {
        ArrayList<Command> commands = new ArrayList<>();
        commands.addAll(Arrays.asList(dimensions.compile()));
        commands.add(new Command(Commands.CREATE_DIMENSIONAL_ARRAY, ""));
        return arrayListToArray(commands);
    }
}
