package com.BobScript.BobCode;

import com.BobScript.BobCode.Functions.BuiltInFunctions.PrintAction;
import com.BobScript.BobCode.Functions.BuiltInFunctions.ReadLineAction;
import com.BobScript.BobCode.Functions.Function;
import com.BobScript.BobCode.Functions.FunctionAction;

import java.util.*;

import com.BobScript.Support.*;

/**
 * Created by zinov on 20.02.2016.
 *
 *  execute class, that run compiled code
 */
public class Interpreter {
    private InterpreterInfo info;
    private CommandAction[] commandActions;

    public Interpreter() {
        info = new InterpreterInfo();
        initCommandActions();
        initBuiltInFunctions();
    }

    private void initBuiltInFunctions() {
        info.functions.put("print", new PrintAction());
        info.functions.put("readLine", new ReadLineAction());
    }

    // иницилизирует действия команд
    private void initCommandActions() {
        commandActions = new CommandAction[Commands.values().length];

        // каждому номеру перечисления присваивается экземпляр действия, с нужным переопределённым методом
        commandActions[Commands.EXIT.ordinal()] = new ExitAction();
        commandActions[Commands.PUSH.ordinal()] = new PushAction();
        commandActions[Commands.ASSIGN.ordinal()] = new AssignAction();
        commandActions[Commands.DELETE.ordinal()] = new DeleteAction();
        commandActions[Commands.ADD.ordinal()] = new AddAction();
        commandActions[Commands.SUB.ordinal()] = new SubAction();
        commandActions[Commands.LESSER.ordinal()] = new LesserAction();
        commandActions[Commands.GREATER.ordinal()] = new GreaterAction();
        commandActions[Commands.EQUAL.ordinal()] = new EqualAction();
        commandActions[Commands.CONDITION.ordinal()] = new ConditionAction();
        commandActions[Commands.END_CONDITION.ordinal()] = new NothingAction();
        commandActions[Commands.GOTO.ordinal()] = new GotoAction();
        commandActions[Commands.MULT.ordinal()] = new MultAction();
        commandActions[Commands.FUNCTION.ordinal()] = new FunctionCommandAction();
        commandActions[Commands.ARG_COUNT.ordinal()] = new NothingAction();
        commandActions[Commands.ARGUMENT.ordinal()] = new NothingAction();
        commandActions[Commands.CALL.ordinal()] = new CallAction();
        commandActions[Commands.END_FUNCTION.ordinal()] = new NothingAction();
        commandActions[Commands.ASSIGN_ADD.ordinal()] = new AssignAddAction();
        commandActions[Commands.CREATE_ARRAY.ordinal()] = new CreateArrayAction();
        commandActions[Commands.CREATE_OR_PUSH.ordinal()] = new CreateOrPushAction();
        commandActions[Commands.GET_FROM.ordinal()] = new GetFromAction();
    }

    private Command[] currentProgram;

    // run commands
    public int execute(Command[] program) {
        Command currentCommand;
        currentProgram = program;
        info.commandIndex = 0;
        while (info.commandIndex < program.length) {
            currentCommand = program[info.commandIndex];
            CommandAction currentAction = commandActions[currentCommand.getCommand().ordinal()];
            switch (currentAction.Action(currentCommand)) {
                case OK:
                    break;
                case ERROR:
                    Log.printError("Error! " + currentCommand);
                    return -1;
                case CONTINUE_OK:
                    continue;
            }
            info.commandIndex++;
        }
        return 0;
    }

    public static boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }


    /*
        push data_type data

        push i 10
        push f 0.5
        push s 'abc'
        push v a
        push n null
        push b true
     */
    // класс для добавления в стек
    private class PushAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            char pushParam = (char)currentCommand.getArgs()[0];
            Object pushData = currentCommand.getArgs()[1];
            switch (pushParam) {
                // integer
                case 'i':
                    info.stack.add(new StackData(pushData, Type.INT));
                    break;
                // float
                case 'f':
                    info.stack.add(new StackData(pushData, Type.DOUBLE));
                    break;
                // string
                case 's':
                    info.stack.add(new StackData(TypeSupport.toConstString((String)pushData), Type.STRING));
                    break;
                // null
                case 'n':
                    info.stack.add(new StackData(null, Type.NULL));
                    break;
                // boolean
                case 'b':
                    info.stack.add(new StackData(pushData, Type.BOOLEAN));
                    break;
                // variable
                case 'v':
                    if (info.variables.containsKey(pushData)) {
                        info.stack.add(info.variables.get(pushData));
                    } else {
                        Log.printError("Error: " + currentCommand + " nothing variables");
                    }
                    break;
                // error
                default:
                    Log.printError("ERROR: " + currentCommand + "unknown type");
                    return CommandResult.ERROR;
            }

            /*if (TypeSupport.tryInt(args[0])) {
                stack.add(new StackData(Long.parseLong(args[0]), Type.INT));
            } else if (TypeSupport.tryDouble(args[0])) {
                stack.add(new StackData(Double.parseDouble(args[0]), Type.DOUBLE));
            } else if (TypeSupport.tryConstString(args[0])) {
                stack.add(new StackData(TypeSupport.toConstString(args[0]), Type.STRING));
            } else if (TypeSupport.tryBoolean(args[0])) {
                stack.add(new StackData(Boolean.parseBoolean(args[0]), Type.BOOLEAN));
            } else if (TypeSupport.tryNull(args[0])) {
                stack.add(new StackData(null, Type.NULL));
            } else if (variables.containsKey(args[0])) {
                stack.add(variables.get(args[0]).clone());
            } else {
                Log.printError("ERROR: " + currentCommand + "unknown type");
                return CommandResult.ERROR;
            }*/
            return CommandResult.OK;
        }
    }

    // класс для присвоения переменной
    private class AssignAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            if (info.stack.size() < 2) {
                Log.printError("ERROR: " + currentCommand + "stack size too small");
                return CommandResult.ERROR;
            }
            StackData result = info.stack.pop();
            StackData variablePointer = info.stack.pop();
            variablePointer.assignCopy(result);

            return CommandResult.OK;
        }
    }

    // класс для сложения
    private class AddAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            if (info.stack.size() < 2) {
                Log.printError("ERROR " + currentCommand + "stack size too small");
                return CommandResult.ERROR;
            }

            StackData b = info.stack.pop();
            StackData a = info.stack.pop();

            StackData answ;

            if (a.getType() == Type.INT && b.getType() == Type.INT) {
                answ = new StackData((long)a.getData() + (long)b.getData(), Type.INT);
            } else if ((a.getType() == Type.DOUBLE && b.getType() == Type.DOUBLE)
                    || (a.getType() == Type.DOUBLE && b.getType() == Type.INT)
                    || (a.getType() == Type.INT && b.getType() == Type.DOUBLE)) {

                double tmpA = Double.parseDouble(a.getData().toString()), tmpB = Double.parseDouble(b.getData().toString());
                answ = new StackData(tmpA + tmpB, Type.DOUBLE);
            } else if (a.getType() == Type.STRING && b.getType() == Type.STRING) {
                answ = new StackData((String)a.getData() + (String)b.getData(), Type.STRING);
            } else {
                Log.printError("ERROR: operator + not defined for " + a.toString() + " " + b.toString());
                return CommandResult.ERROR;
            }

            info.stack.push(answ);

            return CommandResult.OK;
        }
    }

    private class SubAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            if (info.stack.size() < 2) {
                Log.printError("ERROR " + currentCommand + "stack size too small");
                return CommandResult.ERROR;
            }

            StackData b = info.stack.pop();
            StackData a = info.stack.pop();

            StackData answ;

            if (a.getType() == Type.INT && b.getType() == Type.INT) {
                answ = new StackData((long)a.getData() - (long)b.getData(), Type.INT);
            } else if ((a.getType() == Type.DOUBLE && b.getType() == Type.DOUBLE)
                    || (a.getType() == Type.DOUBLE && b.getType() == Type.INT)
                    || (a.getType() == Type.INT && b.getType() == Type.DOUBLE)) {

                double tmpA = Double.parseDouble(a.getData().toString()), tmpB = Double.parseDouble(b.getData().toString());
                answ = new StackData(tmpA - tmpB, Type.DOUBLE);
            } else {
                Log.printError("ERROR: operator - not defined for " + a.toString() + " " + b.toString());
                return CommandResult.ERROR;
            }

            info.stack.push(answ);

            return CommandResult.OK;

        }
    }

    // класс для проверки условия
    private class ConditionAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            Command[] program = currentProgram;

            if (info.stack.size() < 1) {
                Log.printError("ERROR " + currentCommand + "stack size too small");
                return CommandResult.ERROR;
            }

            StackData cond = info.stack.pop();
            if (cond.getType() != Type.BOOLEAN) {
                Log.printError("ERROR " + cond + " condition must be boolean");
                return CommandResult.ERROR;
            }

            if (!(boolean)cond.getData()) {
                int tmp = -1, balance = 0;
                for (int ind = info.commandIndex; ind < program.length; ind++) {
                    if (program[ind].getCommand() == Commands.CONDITION) {
                        balance++;
                    }
                    else if (program[ind].getCommand() == Commands.END_CONDITION) {
                        balance--;
                    }
                    if (balance == 0) {
                        tmp = ind + 1;
                        break;
                    }
                }
                if (tmp != -1) {
                    info.commandIndex = tmp;
                    return CommandResult.CONTINUE_OK;
                }
                Log.printError("Error: no condition end");
                return CommandResult.ERROR;
            }
            return CommandResult.OK;
        }
    }

    // класс для перехода к другой команде
    private class GotoAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            int delta = (int)currentCommand.getArgs()[0];
            info.commandIndex += delta;
            return CommandResult.CONTINUE_OK;
        }
    }

    // класс для завершения программы
    private class ExitAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            if (!currentCommand.getArgs()[0].equals("0")) {
                Log.printError("FAILED: return code - " + currentCommand.getArgs()[0]);
                return CommandResult.ERROR;
            }
            return CommandResult.OK;
        }
    }

    // класс для удаления переменных
    private class DeleteAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            String variableName = (String)currentCommand.getArgs()[0];
            if (!info.variables.containsKey(variableName)) {
                Log.printError("ERROR " + variableName + "unknown name");
                return CommandResult.ERROR;
            }

            info.variables.remove(variableName);
            return CommandResult.OK;
        }
    }

    // класс для умножения
    private class MultAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            if (info.stack.size() < 2) {
                Log.printError("Error: stack size too small");
                return CommandResult.ERROR;
            }

            StackData b = info.stack.pop();
            StackData a = info.stack.pop();
            StackData answ = new StackData();
            if (a.getType() == Type.INT && b.getType() == Type.INT) {
                long tmpA = Long.parseLong(a.getData().toString()), tmpB = Long.parseLong(b.getData().toString());
                answ = new StackData(tmpA * tmpB, Type.INT);
            }

            info.stack.push(answ);

            return CommandResult.OK;
        }
    }

    // класс для сравнения меньше
    private class LesserAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            if (info.stack.size() < 2) {
                Log.printError("ERROR " + currentCommand + "stack size too small");
                return CommandResult.ERROR;
            }

            StackData b = info.stack.pop();
            StackData a = info.stack.pop();

            StackData answ = new StackData(null, Type.NULL);

            if (a.getType() == Type.INT && b.getType() == Type.INT) {
                answ = new StackData((long)a.getData() < (long)b.getData(), Type.BOOLEAN);
            }

            info.stack.push(answ);
            return CommandResult.OK;
        }
    }

    // класс для сравнения больше
    private class GreaterAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            if (info.stack.size() < 2) {
                Log.printError("ERROR " + currentCommand + " stack size too small");
                return CommandResult.ERROR;
            }

            StackData b = info.stack.pop();
            StackData a = info.stack.pop();

            StackData answ = new StackData(null, Type.NULL);

            if (a.getType() == Type.INT && b.getType() == Type.INT) {
                answ = new StackData((long)a.getData() > (long)b.getData(), Type.BOOLEAN);
            }

            info.stack.push(answ);
            return CommandResult.OK;
        }
    }

    // класс для сравнения равно
    private class EqualAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            if (info.stack.size() < 2) {
                Log.printError("ERROR " + currentCommand + " stack size too small");
                return CommandResult.ERROR;
            }

            StackData b = info.stack.pop();
            StackData a = info.stack.pop();

            StackData answ = new StackData(null, Type.NULL);

            if (a.getType() == Type.INT && b.getType() == Type.INT) {
                long tmpA = (long)a.getData(), tmpB = (long)b.getData();
                answ = new StackData(tmpA == tmpB, Type.BOOLEAN);
            }

            info.stack.push(answ);
            return CommandResult.OK;
        }
    }

    // класс для ничего
    private class NothingAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            return CommandResult.OK;
        }
    }

    // класс для присваивания и сложения
    // пока что для интов
    private class AssignAddAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            if (info.stack.size() < 2) {
                Log.printError("ERROR " + currentCommand + " stack size too small");
                return CommandResult.ERROR;
            }
            StackData a = info.stack.pop();
            Variable variable = (Variable)info.stack.pop();
            variable.data = (long)variable.data + (long)a.data;

            return CommandResult.OK;
        }
    }

    // класс для вызова функций
    private class CallAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            String functionName = (String)currentCommand.getArgs()[0];
            if (!functionName.contains(functionName))
                return CommandResult.ERROR;
            FunctionAction tmp = info.functions.get(functionName);
            if (tmp.getArgumentsCount() != (int)currentCommand.getArgs()[1])
                return CommandResult.ERROR;
            tmp.Action(info.stack);
            return CommandResult.OK;
        }
    }

    // класс для создания массивов
    private class CreateArrayAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            int arraySize = (int)currentCommand.getArgs()[0];

            if (info.stack.size() < arraySize)
                return CommandResult.ERROR;

            ArrayList<StackData> array = new ArrayList<>();

            for (int i = 0; i < arraySize; i++)
                array.add(info.stack.pop().clone());

            info.stack.push(new StackData(array, Type.ARRAY));
            return CommandResult.OK;
        }
    }

    // класс для создания переменных и добавления их встек
    private class CreateOrPushAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            String name = (String)currentCommand.getArgs()[0];
            if (!info.variables.containsKey(name)) {
                info.variables.put(name, new Variable());
            }

            info.stack.push(info.variables.get(name));
            return CommandResult.OK;
        }
    }

    // получение эл-та из коллекции
    private class GetFromAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            int countIndexes = (int)currentCommand.getArgs()[0];
            if (countIndexes != 1)
                return CommandResult.ERROR;

            StackData index = info.stack.pop();
            StackData array = info.stack.pop();
            if (array.data instanceof ArrayList) {
                ArrayList<StackData> tmp = (ArrayList<StackData>)array.data;
                info.stack.push((tmp.get((int)((long)index.data))));
            }
            else
                return CommandResult.ERROR;

            return CommandResult.OK;
        }
    }

    private class FunctionCommandAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            return CommandResult.OK;
        }
    }

    public Stack<StackData> getStack() {
        return info.stack;
    }

    public Map<String, Variable> getVariables() {
        return info.variables;
    }
}
