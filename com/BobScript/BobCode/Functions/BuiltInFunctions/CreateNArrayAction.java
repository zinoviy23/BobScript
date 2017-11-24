package com.BobScript.BobCode.Functions.BuiltInFunctions;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Type;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Класс для функции, генерирующей н мерный массив
 */
public class CreateNArrayAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 1;
    }

    @Override
    public void Action(InterpreterInfo info) {
        StackData st = info.stack.pop();
        if (st.getType() != Type.ARRAY)
            return;
        else {
            ArrayList<StackData> sizes = (ArrayList<StackData>) st.getData();
            info.stack.push(new StackData(createArrayN(sizes.size(), 1, sizes), Type.ARRAY));
        }
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
        long size = (long)sizes.get(current - 1).getData();
        ArrayList<StackData> res = new ArrayList<>();
        res.ensureCapacity((int)size + 1);
        for (int i = 0; i < size; i++) {
            if (n == current)
                res.add(new StackData(null, Type.NULL));
            else
                res.add(new StackData(createArrayN(n, current + 1, sizes), Type.ARRAY));
        }
        return res;
    }


}
