package com.wallissoftware.zip.client;

import com.google.gwt.junit.client.GWTTestCase;

public class InflatTest extends GWTTestCase {

    private static String str1 = "A simple Test of Compression";

    @Override
    public String getModuleName() {
        return "com.wallissoftware.zip.Zip";
    }

    public void testCompressDecompress() {
        assertEquals(str1, Inflate.inflate(Inflate.deflate(str1)));
    }

    public void testRawCompressDecompress() {
        assertEquals(str1, Inflate.inflateRaw(Inflate.deflateRaw(str1)));
    }

}
