package com.wallissoftware.zip.client;

import java.util.ArrayList;
import java.util.List;

public class ZipArchive {

    private final BinaryStream stream;

    private final List<ZipEntry> zipEntries;

    public final static int MAGIC_NUMBER = 0x04034b50;

    public final static int EXT_SIG = 0x08074B50;

    public static boolean isZipFile(final BinaryStream stream) {
        return stream.getByteRangeAsNumber(0, 4) == MAGIC_NUMBER;
    }

    public ZipArchive(final BinaryStream stream) throws NotAZipArchiveException {
        this.stream = stream;
        zipEntries = new ArrayList<>();
        if (!isZipFile(stream)) {
            throw new NotAZipArchiveException();
        }
        while (readEntry()) {
        }
    }

    public String getMd5() {
        return stream.getMd5();
    }

    public List<ZipEntry> getZipEntries() {
        return zipEntries;
    }

    private boolean readEntry() {
        final ZipEntry e = new ZipEntry(stream);
        if (e.getData() != null) {
            zipEntries.add(e);
            return true;
        }
        return false;
    }
}
