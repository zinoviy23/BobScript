package com.BobScript.BobCode.Types;

import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Type;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * Class for Range type
 */
public class Range implements Iterable<StackData> {
    private long start, end;

    /**
     * Simple constructor
     * @param start start of range
     * @param end enf of range(exclusive)
     */
    public Range(long start, long end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Gets iterator for foreach-loop
     * @return iterator
     */
    @NotNull
    @Override
    public Iterator<StackData> iterator() {
        return new RangeIterator(start, end);
    }

    /**
     * Range iterator class for foreach-loop
     */
    public static class RangeIterator implements Iterator<StackData> {
        private long start, end;
        private long current;

        /**
         * Simple constructor
         * @param start start inclusive
         * @param end end exclusive
         */
        public RangeIterator(long start, long end) {
            this.start = start;
            this.end = end;
            current = this.start;
        }

        /**
         * It has next, if current state lesser than end
         * @return true, if it has
         */
        @Override
        public boolean hasNext() {
            return current < end;
        }

        /**
         * Gets next element
         * @return next element
         */
        @Override
        public StackData next() {
            return new StackData(current++, Type.INT);
        }

        /**
         * Do nothing
         */
        @Override
        public void remove() {

        }
    }

    /**
     * Overrided toString
     * @return
     */
    @Override
    public String toString() {
        return "(" + start + ".." + end + ")";
    }
}
