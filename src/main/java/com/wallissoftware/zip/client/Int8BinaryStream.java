package com.wallissoftware.zip.client;

import com.google.gwt.typedarrays.shared.Int8Array;


public class Int8BinaryStream extends BinaryStream {

    private final Int8Array file;



    public Int8BinaryStream(final Int8Array file) {
        this.file = file;

    }

    @Override
    BinaryStream doSplit(final int start, final int end) {
        return new Int8BinaryStream(file.subarray(start, end));
    }




    @Override
    public int getByteAt(final int index) {
        return (file.get(index) + 256) % 256;
    }

    @Override
    public int getLength() {
        return file.length();
    }

}
