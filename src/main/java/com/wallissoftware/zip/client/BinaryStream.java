package com.wallissoftware.zip.client;

import com.wallissoftware.codec.client.Hash;

public abstract class BinaryStream {

    private int currentByteIndex = 0;

    private String md5 = null;

    private boolean littleEndian = false;



    public int[] byteArray(final int steps) {
        final int[] result = new int[steps];
        for (int i = 0; i < steps; i++) {
            result[i] = getByteAt(i + currentByteIndex);
        }
        skip(steps);
        return result;
    }

    abstract BinaryStream doSplit(int start, int end);

    public abstract int getByteAt(final int index);

    public int getByteRangeAsNumber(final int index, final int steps) {
        int result = 0;
        if (littleEndian) {
            for (int i = index; i < index + steps; i++) {
                result = (result << 8) + this.getByteAt(i);
            }
        } else {
            int i = index + steps - 1;
            while (i >= index) {
                result = (result << 8) + this.getByteAt(i);
                i--;
            }

        }
        return result;
    }

    public final int getCurrentPosition() {
        return this.currentByteIndex;
    }

    public abstract int getLength();

    public final String getMd5() {
        if (md5 == null) {
            md5 = Hash.MD5(rangeString(0, Math.min(2000, getLength())));
        }
        return md5;
    }

    public final boolean hasNext() {
        return currentByteIndex < getLength();
    }

    public final int num(final int steps) {
        final int result = getByteRangeAsNumber(currentByteIndex, steps);
        currentByteIndex += steps;
        return result;
    }

    public final String rangeString(final int index, final int steps) {
        final StringBuilder result = new StringBuilder();

        for (int i = 0; i < steps; i++) {
            result.append((char) getByteAt(index + i));
        }
        return result.toString();

    }

    public void setCursorAt(final int cursorPos) {
        currentByteIndex = cursorPos;
    }

    public void setLittleEndian(final boolean littleEndian) {
        this.littleEndian = littleEndian;
    }

    public void skip(final int steps) {
        currentByteIndex += steps;

    }

    public final BinaryStream split(final int start, final int end) {
        final BinaryStream result = doSplit(start, end);
        result.setLittleEndian(littleEndian);
        return result;

    }

    public final String string(final int steps) {
        final String result = rangeString(currentByteIndex, steps);
        currentByteIndex += steps;
        return result;
    }

}
