package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;

import java.util.ArrayList;

public class FieldNode extends TreeNode {
    private ArrayList<String> fields;

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("Fields:");
        for (String name : fields) {
            drawLevel(level + 1);
            debugWriter.println(name);
        }
    }

    public ArrayList<String> getFields() {
        return fields;
    }

    public void setFields(ArrayList<String> fields) {
        this.fields = fields;
    }

    @Override
    public Command[] compile() {
        ArrayList<Command> res = new ArrayList<>();
        for (String name : fields)
            res.add(new Command(Commands.FIELD, name));

        return arrayListToArray(res);
    }
}
