package com.BobScript.BobCode.Functions;

import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;

import java.util.Stack;

/**
 * Абстрактный класс для представления действия функции
 */
public abstract class FunctionAction {
    protected boolean isBuiltin = true;

    /**
     * Количество аргументов функции
     * @return целое число, кол-во аргументов
     */
    public abstract int getArgumentsCount();

    /**
     * Действие функции
     * @param info информация об состоянии интерпретатора
     */
    public abstract void Action(InterpreterInfo info);

    /**
     * Является ли функция встроенной
     * @return true, если является
     */
    public boolean isBuiltinFunction() {return isBuiltin; }

}
