package com.wallissoftware.zip.client;

public class LegacyBigEndianBinaryStream extends BigEndianBinaryStream {
    private final String binaryString;



    public LegacyBigEndianBinaryStream(final String binaryString) {
        this.binaryString = binaryString;
    }

    @Override
    public int getByteAt(final int index) {
        return binaryString.charAt(index);
    }

    @Override
    public long getByteRangeAsNumber(final int index, final int steps) {
        long result = 0;
        int i = index + steps - 1;
        while (i >= index) {
            result = (result << 8) + this.getByteAt(i);
            i--;
        }
        return result;
    }


    @Override
    protected int length() {
        return binaryString.length();
    }



}
