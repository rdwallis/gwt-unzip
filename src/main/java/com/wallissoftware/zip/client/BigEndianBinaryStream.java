package com.wallissoftware.zip.client;

public abstract class BigEndianBinaryStream {

    private int currentByteIndex = 0;



    public abstract int getByteAt(final int index);

    public long getByteRangeAsNumber(final int index, final int steps) {
        long result = 0;
        int i = index + steps - 1;
        while (i >= index) {
            result = (result << 8) + this.getByteAt(i);
            i--;
        }
        return result;
    }

    public final String getByteRangeAsString(final int index, final int steps) {
        final char[] result = new char[steps];

        for (int i = 0; i < steps; i++) {
            result[i] = (char) getByteAt(index + i);
        }
        return String.valueOf(result);

    }

    public final int getCurrentPosition() {
        return this.currentByteIndex;
    }

    public final long getNextBytesAsNumber(final int steps) {
        final long result = getByteRangeAsNumber(currentByteIndex, steps);
        currentByteIndex += steps;
        return result;
    }



    public final String getNextBytesAsString(final int fileNameLength) {
        final String result = getByteRangeAsString(currentByteIndex, fileNameLength);
        currentByteIndex += fileNameLength;
        return result;
    }

    public final boolean hasNext() {
        return currentByteIndex < length();
    }

    protected abstract int length();

}
