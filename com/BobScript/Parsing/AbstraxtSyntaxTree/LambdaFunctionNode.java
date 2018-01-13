package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Класс для представления вершины лямбда функции
 */
public class LambdaFunctionNode extends TreeNode {
    /**
     * Счётчик лямбда функций
     */
    private static int lambdaFunctionCounter = 0;
    /**
     * аргументы функции
     */
    private ArrayList<FunctionDeclarationNode.ArgumentInfo> argumentInfo = new ArrayList<>();
    /**
     * Тело функции
     */
    private TreeNode body;

    /**
     * Конструктор
     * @param argumentInfo аргументы
     * @param body тело
     */
    public LambdaFunctionNode(ArrayList<FunctionDeclarationNode.ArgumentInfo> argumentInfo, TreeNode body) {
        this.argumentInfo = argumentInfo;
        this.body = body;
    }

    @Override
    public void debugPrint(int level) {
        drawLevel(level);
        debugWriter.println("Lambda Function");
        drawLevel(level);
        debugWriter.println("Arguments");
        for (FunctionDeclarationNode.ArgumentInfo info : argumentInfo) {
            drawLevel(level + 1);
            debugWriter.print(info.name);
            if (info.type != null) {
                debugWriter.println(" : " + info.type);
            }
            else
                debugWriter.println();
        }
        drawLevel(level);
        debugWriter.println("Body: ");
        body.debugPrint(level + 1);
    }

    @Override
    public Command[] compile() {
        ArrayList<Command> commands = new ArrayList<>();
        for (int i = argumentInfo.size() - 1; i >= 0; i--)
            commands.add(new Command(Commands.ARGUMENT, argumentInfo.get(i).name,
                    (!argumentInfo.get(i).type.equals("") ? argumentInfo.get(i).type : null)));
        String name = Integer.toString(lambdaFunctionCounter) + "_L";
        commands.add(new Command(Commands.FUNCTION, name, argumentInfo.size()));
        lambdaFunctionCounter++;
        commands.addAll(Arrays.asList(body.compile()));
        if (!(body instanceof DoBlockNode))
            commands.add(new Command(Commands.RETURN, true));
        commands.add(new Command(Commands.END_FUNCTION));
        commands.add(new Command(Commands.PUSH, 'v', name));
        return arrayListToArray(commands);
    }
}
