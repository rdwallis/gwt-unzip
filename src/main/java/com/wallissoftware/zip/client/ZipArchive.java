package com.wallissoftware.zip.client;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class ZipArchive {

    private final BigEndianBinaryStream stream_;

    private final Set<ZipEntry> zipEntries;

    public final static int MAGIC_NUMBER = 0x04034b50;

    public final static int EXT_SIG = 0x08074B50;

    public static void createZipArchive(final String binaryString, final AsyncCallback<ZipArchive> callback) {
        try {
            final ZipArchive archive = new ZipArchive(binaryString);
            while (archive.readEntry()) {
            }
            callback.onSuccess(archive);
        } catch (final NotAZipArchiveException e) {
            callback.onFailure(e);
        }
    }

    private ZipArchive(final String binaryString) throws NotAZipArchiveException {
        stream_ = new BigEndianBinaryStream(binaryString);
        zipEntries = new HashSet<ZipEntry>();
        if (!isZipFile()) {
            throw new NotAZipArchiveException();
        }
    }

    public Set<ZipEntry> getZipEntries() {
        return zipEntries;
    }

    private boolean isZipFile() {
        return stream_.getByteRangeAsNumber(0, 4) == MAGIC_NUMBER;
    }

    private boolean readEntry() {
        final ZipEntry e = new ZipEntry(stream_);
        if (e.getData() != null) {
            zipEntries.add(e);
            return true;
        }
        return false;
    }
}
