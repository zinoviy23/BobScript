package com.BobScript.BobCode.Types;

import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Type;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class Range implements Iterable<StackData> {
    private long start, end;

    public Range(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @NotNull
    @Override
    public Iterator<StackData> iterator() {
        return new RangeIterator(start, end);
    }

    public static class RangeIterator implements Iterator<StackData> {
        private long start, end;
        private long current;

        public RangeIterator(long start, long end) {
            this.start = start;
            this.end = end;
            current = this.start;
        }

        @Override
        public boolean hasNext() {
            return current < end;
        }

        @Override
        public StackData next() {
            return new StackData(current++, Type.INT);
        }

        @Override
        public void remove() {

        }
    }

    @Override
    public String toString() {
        return "(" + start + ".." + end + ")";
    }
}
