package com.wallissoftware.zip.client;

public abstract class BigEndianBinaryStream {

    private int currentByteIndex = 0;



    public abstract int getByteAt(final int index);

    public int getByteRangeAsNumber(final int index, final int steps) {
        int result = 0;
        int i = index + steps - 1;
        while (i >= index) {
            result = (result << 8) + this.getByteAt(i);
            i--;
        }
        return result;
    }

    public final String getByteRangeAsString(final int index, final int steps) {
        final StringBuilder result = new StringBuilder();

        for (int i = 0; i < steps; i++) {
            result.append((char) getByteAt(index + i));
        }
        return result.toString();

    }

    public final int getCurrentPosition() {
        return this.currentByteIndex;
    }

    public final int getNextBytesAsNumber(final int steps) {
        final int result = getByteRangeAsNumber(currentByteIndex, steps);
        currentByteIndex += steps;
        return result;
    }



    public final String getNextBytesAsString(final int steps) {
        final String result = getByteRangeAsString(currentByteIndex, steps);
        currentByteIndex += steps;
        return result;
    }

    public final boolean hasNext() {
        return currentByteIndex < length();
    }

    protected abstract int length();

}
