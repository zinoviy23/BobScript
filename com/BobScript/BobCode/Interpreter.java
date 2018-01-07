package com.BobScript.BobCode;

import com.BobScript.BobCode.Functions.BuiltInFunctions.*;
import com.BobScript.BobCode.Functions.Function;
import com.BobScript.BobCode.Functions.FunctionAction;

import java.util.*;

import com.BobScript.BobCode.Functions.UsersFunctionAction;
import com.BobScript.Support.*;
import org.jetbrains.annotations.Nullable;

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
        info.interpreter = this;
        initCommandActions();
        initBuiltInFunctions();
    }

    private void initBuiltInFunctions() {
        info.functions.put("print", new PrintAction());
        info.functions.put("readLine", new ReadLineAction());
        info.functions.put("parseInt", new ParseIntAction());
        info.functions.put("createArray", new CreateArrayFunctionAction());
        info.functions.put("println", new PrintlnAction());
        info.functions.put("random", new RandomAction());
        info.functions.put("createNArray", new CreateNArrayAction());
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
        commandActions[Commands.ARGUMENT.ordinal()] = new ArgumentAction();
        commandActions[Commands.CALL.ordinal()] = new CallAction();
        commandActions[Commands.END_FUNCTION.ordinal()] = new EndFunctionAction();
        commandActions[Commands.ASSIGN_ADD.ordinal()] = new AssignAddAction();
        commandActions[Commands.CREATE_ARRAY.ordinal()] = new CreateArrayAction();
        commandActions[Commands.CREATE_OR_PUSH.ordinal()] = new CreateOrPushAction();
        commandActions[Commands.GET_FROM.ordinal()] = new GetFromAction();
        commandActions[Commands.RETURN.ordinal()] = new ReturnAction();
        commandActions[Commands.CREATE_DIMENSIONAL_ARRAY.ordinal()] = new CreateDimensionalArrayAction();
        commandActions[Commands.PARSE_BREAK.ordinal()] = new ParseErrorAction();
        commandActions[Commands.PARSE_CONTINUE.ordinal()] = new ParseErrorAction();
        commandActions[Commands.UNARY_MINUS.ordinal()] = new UnaryMinusAction();
        commandActions[Commands.INCREMENT.ordinal()] = new IncrementAction();
        commandActions[Commands.GET_FIELD_FROM.ordinal()] = new GetFieldFromAction();
        commandActions[Commands.CALL_FROM.ordinal()] = new CallFromAction();
    }

    private Command[] currentProgram;

    // run commands
    public int execute(Command[] program) {
        Command currentCommand;
        currentProgram = program;
        info.commandIndex = 0;
        while (info.commandIndex < program.length) {

            currentCommand = program[info.commandIndex];
            //System.out.println(info.commandIndex + ": " + currentCommand);
            CommandAction currentAction = commandActions[currentCommand.getCommand().ordinal()];
            switch (currentAction.Action(currentCommand)) {
                case OK:
                    break;
                case ERROR:
                    Log.printError("Error! " + info.commandIndex + " : " + currentCommand);
                    return -1;
                case CONTINUE_OK:
                    continue;
            }
            info.commandIndex++;
        }
        return 0;
    }

    public void callFunction(FunctionAction func) {
        //System.out.println("lollll");
        if (func.isBuiltinFunction()) {
            func.Action(info);
            return;
        }
        else {
            int prevSize = info.functionStackSize;
            int prevCommandIndex = info.commandIndex;
            info.functionStack.push(new Function(info.commandIndex, func));
            info.functionStackSize++;
            func.Action(info);
            Command currentCommand;
            info.commandIndex++;
            while (info.functionStackSize != prevSize) {
                currentCommand = currentProgram[info.commandIndex];
                //System.out.println(info.commandIndex + ": " + currentCommand);
                CommandAction currentAction = commandActions[currentCommand.getCommand().ordinal()];
                switch (currentAction.Action(currentCommand)) {
                    case OK:
                        break;
                    case ERROR:
                        Log.printError("Error! " + info.commandIndex + " : " + currentCommand);
                        return;
                    case CONTINUE_OK:
                        continue;
                }
                info.commandIndex++;
            }
            info.commandIndex = prevCommandIndex;
        }
    }


    public static boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }


    /**
        push data_type data <br>

        push i 10 <br>
        push f 0.5 <br>
        push s 'abc' <br>
        push v a <br>
        push n null <br>
        push b true
     */
    // класс для добавления в стек
    private class PushAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            char pushParam = (char)currentCommand.getArgs()[0];
            Object pushData = currentCommand.getArgs()[1];
            //System.err.println(currentCommand);
            switch (pushParam) {
                // integer
                case 'i':
                    info.stack.add(new StackData(pushData, Type.INT));
                    break;
                // float
                case 'f':
                    info.stack.add(new StackData(pushData, Type.FLOAT));
                    break;
                // string
                case 's':
                    //info.stack.add(new StackData(TypeSupport.toConstString((String)pushData), Type.STRING));
                    info.stack.add(ObjectsFactory.createString(TypeSupport.toConstString((String)pushData)));
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
                    if (info.variables.containsKey(info.functionStackSize + "#" + pushData)) {
                        info.stack.add(info.variables.get(info.functionStackSize + "#" + pushData));
                    } else if (info.variables.containsKey(pushData)) {
                        info.stack.add(info.variables.get(pushData));
                    } else if(info.functions.containsKey(pushData)) {
                        info.stack.push(ObjectsFactory.createFunction(info.functions.get(pushData)));
                    }
                    else {
                        System.out.println("lol");
                        Log.printError("Error: " + currentCommand + " nothing variables");
                        return CommandResult.ERROR;
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
                stack.add(new StackData(Double.parseDouble(args[0]), Type.FLOAT));
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
            } else if ((a.getType() == Type.FLOAT && b.getType() == Type.FLOAT)
                    || (a.getType() == Type.FLOAT && b.getType() == Type.INT)
                    || (a.getType() == Type.INT && b.getType() == Type.FLOAT)) {

                double tmpA = Double.parseDouble(a.getData().toString()), tmpB = Double.parseDouble(b.getData().toString());
                answ = new StackData(tmpA + tmpB, Type.FLOAT);
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
            } else if ((a.getType() == Type.FLOAT && b.getType() == Type.FLOAT)
                    || (a.getType() == Type.FLOAT && b.getType() == Type.INT)
                    || (a.getType() == Type.INT && b.getType() == Type.FLOAT)) {

                double tmpA = Double.parseDouble(a.getData().toString()), tmpB = Double.parseDouble(b.getData().toString());
                answ = new StackData(tmpA - tmpB, Type.FLOAT);
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
            if (tmp.isBuiltinFunction())
                tmp.Action(info);
            else {
                info.functionStack.push(new Function(info.commandIndex, tmp));
                info.functionStackSize++;
                tmp.Action(info);
            }
            return CommandResult.OK;
        }
    }

    // Добавление аргумента
    private class ArgumentAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            info.stack.push(new StackData(currentCommand.getArgs()[0], Type.STRING));
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

            info.stack.push(ObjectsFactory.createArray(info, arraySize));
            return CommandResult.OK;
        }
    }

    // класс для создания переменных и добавления их встек
    private class CreateOrPushAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            //System.err.println(currentCommand);
            String name = (String)currentCommand.getArgs()[0];
            if (info.functionStackSize == 0) {
                if (!info.variables.containsKey(name)) {
                    info.variables.put(name, new Variable());
                }

                info.stack.push(info.variables.get(name));
            }
            else {
                if (!info.variables.containsKey(info.functionStackSize + "#" + name)) {
                    info.variables.put(info.functionStackSize + "#" + name, new Variable());
                    ((UsersFunctionAction)info.functionStack.peek().action).addVariable(info.functionStackSize + "#" + name);
                }

                info.stack.push(info.variables.get(info.functionStackSize + "#" + name));
            }
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

    // Объявление функции
    private class FunctionCommandAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            String name = (String)currentCommand.getArgs()[0];
            int countArgs = (int)currentCommand.getArgs()[1];
            ArrayList<String> args = new ArrayList<>();
            for (int i = 0; i < countArgs; i++)
                args.add((String)info.stack.pop().data);

            UsersFunctionAction newFunction = new UsersFunctionAction(info.commandIndex + 1, args);
            if (!info.functions.containsKey(name))
                info.functions.put(name, newFunction);

            for (int i = info.commandIndex; i < currentProgram.length; i++) {
                if (currentProgram[i].getCommand() == Commands.END_FUNCTION) {
                    info.commandIndex = i;
                    return CommandResult.OK;
                }
            }

            return CommandResult.ERROR;
        }
    }

    // Завершение функции
    private class EndFunctionAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            Function function = info.functionStack.pop();
            ((UsersFunctionAction)function.action).clearStackExit(info, false);
            ((UsersFunctionAction)function.action).endFunction(info);
            info.commandIndex = function.returnPosition;
            info.functionStackSize--;
            return CommandResult.OK;
        }
    }

    // Выход из функции
    private class ReturnAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            Function function = info.functionStack.pop();
            boolean isReturnSomething = (boolean)currentCommand.getArgs()[0];
            ((UsersFunctionAction)function.action).clearStackExit(info, isReturnSomething);
            ((UsersFunctionAction)function.action).endFunction(info);
            info.commandIndex = function.returnPosition;
            info.functionStackSize--;
            return CommandResult.OK;
        }
    }

    // Создание многомерного массива
    private class CreateDimensionalArrayAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            StackData st = info.stack.pop();
            if (st.getType() != Type.ARRAY)
                return CommandResult.ERROR;
            else {
                ArrayList<StackData> sizes = (ArrayList<StackData>) st.getData();
                if (sizes.size() != 0)
                    info.stack.push(new StackData(createArrayN(sizes.size(), 1, sizes), Type.ARRAY));
                else info.stack.push(new StackData(null, Type.NULL));
            }
            return CommandResult.OK;
        }

        /**
         * Создаёт н мерный массив StackData
         * @param n размерность
         * @param current размерность сейчас
         * @param sizes длинны
         * @return массив
         */
        @Nullable
        private ArrayList<StackData> createArrayN(int n, int current, ArrayList<StackData> sizes) {
            if (sizes.get(current - 1).getType() != Type.INT)
                return null;
            long size = (long) sizes.get(current - 1).getData();
            ArrayList<StackData> res = new ArrayList<>();
            res.ensureCapacity((int) size + 1);
            for (int i = 0; i < size; i++) {
                if (n == current)
                    res.add(new StackData(null, Type.NULL));
                else
                    res.add(new StackData(createArrayN(n, current + 1, sizes), Type.ARRAY));
            }
            return res;
        }
    }

    // Обработка ошибки при парсинге
    private class ParseErrorAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            Log.printError(currentCommand + "Error!");
            return CommandResult.ERROR;
        }
    }

    // Унарный минус
    private class UnaryMinusAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            if (info.stack.empty()) {
                Log.printError(currentCommand + "Error! stack size too small");
                return CommandResult.ERROR;
            }
            StackData sd = info.stack.pop();
            if (sd.getType() == Type.INT) {
                info.stack.push(new StackData(-(long)sd.getData(), Type.INT));
            }
            else if (sd.getType() == Type.FLOAT) {
                info.stack.push(new StackData(-(double)sd.getData(), Type.FLOAT));
            }
            else {
                Log.printError(currentCommand + "There isn't - operator for " + sd);
                return CommandResult.ERROR;
            }
            return CommandResult.OK;
        }
    }

    // инкремент
    private class IncrementAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            if (info.stack.empty()) {
                Log.printError(currentCommand + "Error! stack size too small");
                return CommandResult.ERROR;
            }
            StackData sd = info.stack.peek();
            if (sd.getType() == Type.INT) {
                sd.data = (long)sd.data + 1;
            }
            else if (sd.getType() == Type.FLOAT) {
                sd.data = (double)sd.data + 1.0;
            }
            else {
                Log.printError(currentCommand + "There isn't ++ operator for " + sd);
                return CommandResult.ERROR;
            }
            return CommandResult.OK;
        }
    }

    // получение поля пока криво
    private class GetFieldFromAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            if (info.stack.empty()) {
                Log.printError("ERROR! Stack size too small " + currentCommand);
                return CommandResult.ERROR;
            }
            StackData obj = info.stack.pop();
            String field = (String)currentCommand.getArgs()[0];
            switch (field) {
                case "length":
                    if (obj.getType() != Type.ARRAY) {
                        Log.printError("Nothing length");
                        return CommandResult.ERROR;
                    }
                    info.stack.push(new StackData((long)((ArrayList<StackData>)obj.getData()).size(), Type.INT));
                    break;
                case "str":
                    if (obj.getType() != Type.NULL)
                        info.stack.push(new StackData(obj.getData().toString(), Type.STRING));
                    else {
                        Log.printError("NullPointerException");
                        return CommandResult.ERROR;
                    }
            }
            return CommandResult.OK;
        }
    }

    // вызов метода у объекта пока криво
    private class CallFromAction implements CommandAction {
        @Override
        public CommandResult Action(Command currentCommand) {
            if (info.stack.empty()) {
                Log.printError("Error! stack too small " + currentCommand);
                return CommandResult.ERROR;
            }
            int argCount = (int)currentCommand.getArgs()[1];
            StackData arguments[] = new StackData[argCount];

            for (int i = 0; i < argCount; i++) {
                if (!info.stack.empty()) {
                    arguments[i] = info.stack.pop();
                }
                else {
                    Log.printError("Error! stack too small " + currentCommand);
                    return CommandResult.ERROR;
                }
            }

            StackData callObject = info.stack.pop();
            String name = (String)currentCommand.getArgs()[0];

            /*if (callObject.getType() == Type.ARRAY && name.equals("add") && argCount == 1) {
                ArrayList<StackData> array = (ArrayList<StackData>)callObject.getData();
                array.add(arguments[0]);
            }
            else {
                Log.printError("Error! nothing methods " + currentCommand);
                return CommandResult.ERROR;
            }*/
            FunctionAction func;
            if ((func  = callObject.getMethod(name)) != null) {
                if (func.getArgumentsCount() == arguments.length) {
                    for (int i = arguments.length - 1; i >= 0; i--)
                        info.stack.push(arguments[i]);
                    func.Action(info);
                }
            }
            else {
                Log.printError("Error! nothing methods " + currentCommand);
                return CommandResult.ERROR;
            }

            return CommandResult.OK;
        }
    }

    public Stack<StackData> getStack() {
        return info.stack;
    }

    public Map<String, Variable> getVariables() {
        return info.variables;
    }

    public Map<String, FunctionAction> getFunctions() { return info.functions; }
}
