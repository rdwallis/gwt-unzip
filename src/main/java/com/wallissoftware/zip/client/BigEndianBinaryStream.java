package com.wallissoftware.zip.client;


public class BigEndianBinaryStream {

    private final String binaryString;

    private int currentByteIndex;

    public BigEndianBinaryStream(final String binaryString) {
        this.binaryString = binaryString;
        currentByteIndex = 0;
    }

    public int getByteAt(final int index) {
        return binaryString.charAt(index);
    }

    public long getByteRangeAsNumber(final int index, final int steps) {
        long result = 0;
        int i = index + steps - 1;
        while (i >= index) {
            result = (result << 8) + this.getByteAt(i);
            i--;
        }
        return result;
    }

    public String getByteRangeAsString(final int index, final int steps) {
        return binaryString.substring(index, index + steps);
    }

    public int getCurrentPosition() {
        return this.currentByteIndex;
    }

    public long getNextBytesAsNumber(final int steps) {
        final long result = getByteRangeAsNumber(currentByteIndex, steps);
        currentByteIndex += steps;
        return result;
    }

    public String getNextBytesAsString(final int fileNameLength) {
        final String result = getByteRangeAsString(currentByteIndex, fileNameLength);
        currentByteIndex += fileNameLength;
        return result;
    }

    public boolean hasNext() {
        return currentByteIndex < binaryString.length();
    }

}
