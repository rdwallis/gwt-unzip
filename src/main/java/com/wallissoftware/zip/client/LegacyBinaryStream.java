package com.wallissoftware.zip.client;

public class LegacyBinaryStream extends BinaryStream {
    private final String binaryString;



    public LegacyBinaryStream(final String binaryString) {
        this.binaryString = binaryString;
    }

    @Override
    BinaryStream doSplit(final int start, final int end) {
        return new LegacyBinaryStream(binaryString.substring(start, end));
    }

    @Override
    public int getByteAt(final int index) {
        return binaryString.charAt(index);
    }

    @Override
    public int getLength() {
        return binaryString.length();
    }


}
