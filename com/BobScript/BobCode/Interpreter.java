package com.BobScript.BobCode;

import com.BobScript.Parsing.Parser;

import java.util.HashMap;
import java.util.Stack;
import java.util.Map;
import com.BobScript.Support.*;

/**
 * Created by zinov on 20.02.2016.
 *
 *  execute class, that run compiled code
 */
public class Interpreter {
    private Stack<StackData> stack;
    private Map<String, Variable> variables;
    CommandAction[] commandActions;

    public Interpreter() {
        stack = new Stack<>();
        variables = new HashMap<>();

        initCommandActions();
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
        commandActions[Commands.SUB.ordinal()] = new NothingAction();
        commandActions[Commands.LESSER.ordinal()] = new LesserAction();
        commandActions[Commands.GREATER.ordinal()] = new GreaterAction();
        commandActions[Commands.EQUAL.ordinal()] = new EqualAction();
        commandActions[Commands.CONDITION.ordinal()] = new ConditionAction();
        commandActions[Commands.END_CONDITION.ordinal()] = new NothingAction();
        commandActions[Commands.GOTO.ordinal()] = new GotoAction();
        commandActions[Commands.MULT.ordinal()] = new MultAction();
        commandActions[Commands.FUNCTION.ordinal()] = new NothingAction();
        commandActions[Commands.ARG_COUNT.ordinal()] = new NothingAction();
        commandActions[Commands.ARGUMENT.ordinal()] = new NothingAction();
        commandActions[Commands.CALL.ordinal()] = new NothingAction();
        commandActions[Commands.END_FUNCTION.ordinal()] = new NothingAction();
    }

    private int commandIndex;
    private Command currentCommand;
    private Command[] currentProgram;

    // run commands
    public int execute(Command[] program) {
        currentProgram = program;
        commandIndex = 0;
        while (commandIndex < program.length) {
            currentCommand = program[commandIndex];
            CommandAction currentAction = commandActions[currentCommand.getCommand().ordinal()];
            switch (currentAction.Action(currentCommand.getArgs(), currentCommand)) {
                case OK:
                    break;
                case ERROR:
                    return -1;
                case CONTINUE_OK:
                    continue;
            }
            commandIndex++;
        }
        return 0;
    }

    public static boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }
    

    // класс для добавления в стек
    private class PushAction implements CommandAction {
        @Override
        public CommandResult Action(String[] args, Command currentCommand) {
            if (TypeSupport.tryInt(args[0])) {
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
            }
            return CommandResult.OK;
        }
    }

    // класс для присвоения переменной
    private class AssignAction implements CommandAction {
        @Override
        public CommandResult Action(String[] args, Command currentCommand) {
            if (stack.size() < 1) {
                Log.printError("ERROR: " + currentCommand + "stack size too small");
                return CommandResult.ERROR;
            }

            if (!variables.containsKey(args[0])) {
                variables.put(args[0], new Variable());
            }

            StackData sd = stack.pop();
            variables.get(args[0]).assignCopy(sd);

            return CommandResult.OK;
        }
    }

    // класс для сложения
    private class AddAction implements CommandAction {
        @Override
        public CommandResult Action(String[] args, Command currentCommand) {
            if (stack.size() < 2) {
                Log.printError("ERROR " + currentCommand + "stack size too small");
                return CommandResult.ERROR;
            }

            StackData b = stack.pop();
            StackData a = stack.pop();

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

            stack.push(answ);

            return CommandResult.OK;
        }
    }

    // класс для проверки условия
    private class ConditionAction implements CommandAction {
        @Override
        public CommandResult Action(String[] args, Command currentCommand) {
            Command[] program = currentProgram;

            if (stack.size() < 1) {
                Log.printError("ERROR " + currentCommand + "stack size too small");
                return CommandResult.ERROR;
            }

            StackData cond = stack.pop();
            if (cond.getType() != Type.BOOLEAN) {
                Log.printError("ERROR " + cond + " condition must be boolean");
                return CommandResult.ERROR;
            }

            if (!(boolean)cond.getData()) {
                int tmp = -1, balance = 0;
                for (int ind = commandIndex; ind < program.length; ind++) {
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
                    commandIndex = tmp;
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
        public CommandResult Action(String[] args, Command currentCommand) {
            commandIndex += Integer.parseInt(currentCommand.getArgs()[0]);
            return CommandResult.CONTINUE_OK;
        }
    }

    // класс для завершения программы
    private class ExitAction implements CommandAction {
        @Override
        public CommandResult Action(String[] args, Command currentCommand) {
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
        public CommandResult Action(String[] args, Command currentCommand) {
            if (!variables.containsKey(args[0])) {
                Log.printError("ERROR " + args[0] + "unknown name");
                return CommandResult.ERROR;
            }

            variables.remove(args[0]);
            return CommandResult.OK;
        }
    }

    // класс для умножения
    private class MultAction implements CommandAction {
        @Override
        public CommandResult Action(String[] args, Command currentCommand) {
            if (stack.size() < 2) {
                Log.printError("Error: stack size too small");
                return CommandResult.ERROR;
            }

            StackData b = stack.pop();
            StackData a = stack.pop();
            StackData answ = new StackData();
            if (a.getType() == Type.INT && b.getType() == Type.INT) {
                long tmpA = Long.parseLong(a.getData().toString()), tmpB = Long.parseLong(b.getData().toString());
                answ = new StackData(tmpA * tmpB, Type.INT);
            }

            stack.push(answ);

            return CommandResult.OK;
        }
    }

    // класс для сравнения меньше
    private class LesserAction implements CommandAction {
        @Override
        public CommandResult Action(String[] args, Command currentCommand) {
            if (stack.size() < 2) {
                Log.printError("ERROR " + currentCommand + "stack size too small");
                return CommandResult.ERROR;
            }

            StackData b = stack.pop();
            StackData a = stack.pop();

            StackData answ = new StackData(null, Type.NULL);

            if (a.getType() == Type.INT && b.getType() == Type.INT) {
                answ = new StackData((long)a.getData() < (long)b.getData(), Type.BOOLEAN);
            }

            stack.push(answ);
            return CommandResult.OK;
        }
    }

    // класс для сравнения больше
    private class GreaterAction implements CommandAction {
        @Override
        public CommandResult Action(String[] args, Command currentCommand) {
            if (stack.size() < 2) {
                Log.printError("ERROR " + currentCommand + " stack size too small");
                return CommandResult.ERROR;
            }

            StackData b = stack.pop();
            StackData a = stack.pop();

            StackData answ = new StackData(null, Type.NULL);

            if (a.getType() == Type.INT && b.getType() == Type.INT) {
                answ = new StackData((long)a.getData() > (long)b.getData(), Type.BOOLEAN);
            }

            stack.push(answ);
            return CommandResult.OK;
        }
    }

    // класс для сравнения равно
    private class EqualAction implements CommandAction {
        @Override
        public CommandResult Action(String[] args, Command currentCommand) {
            if (stack.size() < 2) {
                Log.printError("ERROR " + currentCommand + " stack size too small");
                return CommandResult.ERROR;
            }

            StackData b = stack.pop();
            StackData a = stack.pop();

            StackData answ = new StackData(null, Type.NULL);

            if (a.getType() == Type.INT && b.getType() == Type.INT) {
                long tmpA = (long)a.getData(), tmpB = (long)b.getData();
                answ = new StackData(tmpA == tmpB, Type.BOOLEAN);
            }

            stack.push(answ);
            return CommandResult.OK;
        }
    }

    // класс для ничего
    private class NothingAction implements CommandAction {
        @Override
        public CommandResult Action(String[] args, Command currentCommand) {
            return CommandResult.OK;
        }
    }

    public Stack<StackData> getStack() {
        return stack;
    }

    public Map<String, Variable> getVariables() {
        return variables;
    }
}
