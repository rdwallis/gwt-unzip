package com.wallissoftware.zip.client;

import com.google.gwt.user.client.Window;

public class BinaryStreamComparer extends BinaryStream {

    private final BinaryStream stream1, stream2;

    public BinaryStreamComparer(final BinaryStream stream1, final BinaryStream stream2) {
        this.stream1 = stream1;
        this.stream2 = stream2;
        if (stream2.getLength() != stream1.getLength()) {
            Window.alert("lengths don't match: " + stream1.getLength() + "!=" + stream2.getLength());
        }
    }

    @Override
    BinaryStream doSplit(final int start, final int end) {
        return new BinaryStreamComparer(stream1.doSplit(start, end), stream2.doSplit(start, end));
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
    public int getLength() {
        return stream1.getLength();
    }

}
