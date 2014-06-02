package com.wallissoftware.zip.client;

import com.google.gwt.typedarrays.shared.Int8Array;


public class Int8BigEndianBinaryStream extends BigEndianBinaryStream {

    private final Int8Array file;



    public Int8BigEndianBinaryStream(final Int8Array file) {
        this.file = file;

    }

    @Override
    public int getByteAt(final int index) {
        return (file.get(index) + 256) % 256;
    }




    @Override
    protected int length() {
        return file.length();
    }


}
