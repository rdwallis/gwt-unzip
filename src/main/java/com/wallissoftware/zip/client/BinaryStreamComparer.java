package com.wallissoftware.zip.client;

import com.google.gwt.user.client.Window;

public class BinaryStreamComparer extends BigEndianBinaryStream {

    private final BigEndianBinaryStream stream1, stream2;

    public BinaryStreamComparer(final BigEndianBinaryStream stream1, final BigEndianBinaryStream stream2) {
        this.stream1 = stream1;
        this.stream2 = stream2;
        if (stream2.length() != stream1.length()) {
            Window.alert("lengths don't match: " + stream1.length() + "!=" + stream2.length());
        }
    }

    @Override
    public int getByteAt(final int index) {
        final int result1 = stream1.getByteAt(index);
        final int result2 = stream2.getByteAt(index);
        if (result1 != result2) {
            Window.alert("getByteAt:" + index + " " + result1 + "!=" + result2 + " diff=" + (result1 - result2));
            throw new RuntimeException();
        }
        return result1;
    }


    @Override
    int length() {
        return stream1.length();
    }

}
