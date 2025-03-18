package org.appliedenergistics.yoga.style;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SmallValueBufferTest {

    private static final int BUFFER_SIZE = 4;

    @Test
    void copyAssignmentWithOverflow() {
        // In Java we'll simulate copy assignment with a copy constructor
        short[] handles = new short[BUFFER_SIZE + 1];

        SmallValueBuffer buffer1 = new SmallValueBuffer();
        for (int i = 0; i < BUFFER_SIZE + 1; ++i) {
            handles[i] = buffer1.push(i);
        }

        // Create a clone of buffer1 (simulating copy assignment in C++)
        SmallValueBuffer buffer2 = cloneBuffer(buffer1);
        
        for (int i = 0; i < BUFFER_SIZE + 1; ++i) {
            assertEquals(i, buffer2.get32(handles[i]));
        }

        short handle = buffer1.push(42);
        assertEquals(42, buffer1.get32(handle));

        // This should throw IndexOutOfBoundsException in our Java implementation
        assertThrows(IndexOutOfBoundsException.class, () -> buffer2.get32(handle));
    }

    @Test
    void push32() {
        int magic = 88567114;

        SmallValueBuffer buffer = new SmallValueBuffer();
        short handle = buffer.push(magic);
        assertEquals(magic, buffer.get32(handle));
    }

    @Test
    void pushOverflow() {
        int magic1 = 88567114;
        int magic2 = 351012214;
        int magic3 = 146122128;
        int magic4 = Integer.parseUnsignedInt("2171092154"); // Using unsigned parsing for large values
        int magic5 = Integer.parseUnsignedInt("2269016953");

        SmallValueBuffer buffer = new SmallValueBuffer();
        assertEquals(magic1, buffer.get32(buffer.push(magic1)));
        assertEquals(magic2, buffer.get32(buffer.push(magic2)));
        assertEquals(magic3, buffer.get32(buffer.push(magic3)));
        assertEquals(magic4, buffer.get32(buffer.push(magic4)));
        assertEquals(magic5, buffer.get32(buffer.push(magic5)));
    }

    @Test
    void push64() {
        long magic = 118138934255546108L;

        SmallValueBuffer buffer = new SmallValueBuffer();
        short handle = buffer.push(magic);
        assertEquals(magic, buffer.get64(handle));
    }

    @Test
    void push64Overflow() {
        long magic1 = 1401612388342512L;
        long magic2 = 118712305386210L;
        long magic3 = 752431801563359011L;
        long magic4 = 118138934255546108L;
        long magic5 = 237115443124116111L;

        SmallValueBuffer buffer = new SmallValueBuffer();
        assertEquals(magic1, buffer.get64(buffer.push(magic1)));
        assertEquals(magic2, buffer.get64(buffer.push(magic2)));
        assertEquals(magic3, buffer.get64(buffer.push(magic3)));
        assertEquals(magic4, buffer.get64(buffer.push(magic4)));
        assertEquals(magic5, buffer.get64(buffer.push(magic5)));
    }

    @Test
    void push64After32() {
        int magic32 = 88567114;
        long magic64 = 118712305386210L;

        SmallValueBuffer buffer = new SmallValueBuffer();
        short handle32 = buffer.push(magic32);
        assertEquals(magic32, buffer.get32(handle32));

        short handle64 = buffer.push(magic64);
        assertEquals(magic64, buffer.get64(handle64));
    }

    @Test
    void push32After64() {
        int magic32 = 88567114;
        long magic64 = 118712305386210L;

        SmallValueBuffer buffer = new SmallValueBuffer();
        short handle64 = buffer.push(magic64);
        assertEquals(magic64, buffer.get64(handle64));

        short handle32 = buffer.push(magic32);
        assertEquals(magic32, buffer.get32(handle32));
    }

    @Test
    void replace32With32() {
        int magic1 = 88567114;
        int magic2 = 351012214;

        SmallValueBuffer buffer = new SmallValueBuffer();
        short handle = buffer.push(magic1);

        assertEquals(magic2, buffer.get32(buffer.replace(handle, magic2)));
    }

    @Test
    void replace32With64() {
        int magic32 = 88567114;
        long magic64 = 118712305386210L;

        SmallValueBuffer buffer = new SmallValueBuffer();
        short handle = buffer.push(magic32);

        assertEquals(magic64, buffer.get64(buffer.replace(handle, magic64)));
    }

    @Test
    void replace32With64CausesOverflow() {
        int magic1 = 88567114;
        int magic2 = 351012214;
        int magic3 = 146122128;
        int magic4 = Integer.parseUnsignedInt("2171092154");

        long magic64 = 118712305386210L;

        SmallValueBuffer buffer = new SmallValueBuffer();
        short handle1 = buffer.push(magic1);
        buffer.push(magic2);
        buffer.push(magic3);
        buffer.push(magic4);

        assertEquals(magic64, buffer.get64(buffer.replace(handle1, magic64)));
    }

    @Test
    void replace64With32() {
        int magic32 = 88567114;
        long magic64 = 118712305386210L;

        SmallValueBuffer buffer = new SmallValueBuffer();
        short handle = buffer.push(magic64);

        assertEquals(magic32, buffer.get32(buffer.replace(handle, magic32)));
    }

    @Test
    void replace64With64() {
        long magic1 = 1401612388342512L;
        long magic2 = 118712305386210L;

        SmallValueBuffer buffer = new SmallValueBuffer();
        short handle = buffer.push(magic1);

        assertEquals(magic2, buffer.get64(buffer.replace(handle, magic2)));
    }

    // Helper method to clone a buffer (simulating copy constructor)
    private SmallValueBuffer cloneBuffer(SmallValueBuffer original) {
        // This is a simplistic clone that assumes we know the internal state
        // In a real implementation, you would add a proper clone method to SmallValueBuffer
        SmallValueBuffer clone = new SmallValueBuffer();
        
        // Since we can't directly access the internal state of the original buffer,
        // we'll need to recreate it by pushing values from the original
        // This is not efficient but works for testing
        int size = 0;
        try {
            // Keep trying to get values until we get an exception
            while (true) {
                int value = original.get32((short)size);
                clone.push(value);
                size++;
            }
        } catch (IndexOutOfBoundsException e) {
            // Expected exception when we've reached the end
        }
        
        return clone;
    }
}