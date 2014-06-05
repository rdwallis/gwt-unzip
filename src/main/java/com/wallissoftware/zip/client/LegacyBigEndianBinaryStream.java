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
    protected int length() {
        return binaryString.length();
    }



}
