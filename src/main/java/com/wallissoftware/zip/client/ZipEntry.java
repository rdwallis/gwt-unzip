package com.wallissoftware.zip.client;





public class ZipEntry {

    private int signature;

    private int versionNeeded;

    private int bitFlag;

    private int compressionMethod;

    private int timeBlob;

    private int crc32;

    private int compressedSize;

    private int uncompressedSize;

    private int fileNameLength;

    private int extraFieldLength;

    private String fileName;

    private String extra;

    private String data;

    private String inflated;

    protected ZipEntry(final BigEndianBinaryStream stream) {
        signature = stream.getNextBytesAsNumber(4);

        if (signature != ZipArchive.MAGIC_NUMBER) {
            return;
        }
        versionNeeded = stream.getNextBytesAsNumber(2);
        bitFlag = stream.getNextBytesAsNumber(2);
        compressionMethod = stream.getNextBytesAsNumber(2);
        timeBlob = stream.getNextBytesAsNumber(4);

        crc32 = stream.getNextBytesAsNumber(4);
        compressedSize = stream.getNextBytesAsNumber(4);
        uncompressedSize = stream.getNextBytesAsNumber(4);
        fileNameLength = stream.getNextBytesAsNumber(2);
        extraFieldLength = stream.getNextBytesAsNumber(2);
        fileName = stream.getNextBytesAsString(fileNameLength);
        extra = stream.getNextBytesAsString(extraFieldLength);
        data = stream.getNextBytesAsString(compressedSize);
        if (isUsingBit3TrailingDataDescriptor()) {
            stream.getNextBytesAsNumber(16);
        }
    }

    protected long getBitFlage() {
        return bitFlag;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    protected long getCompressionMethod() {
        return compressionMethod;
    }

    public long getCrc32() {
        return crc32;
    }

    public String getData() {
        return data;
    }

    protected String getExtra() {
        return extra;
    }

    protected long getExtraFieldLength() {
        return extraFieldLength;
    }

    public String getFileName() {
        return fileName;
    }

    protected long getFileNameLength() {
        return fileNameLength;
    }

    public String getInflated() {
        if (inflated == null) {
            if (compressionMethod == 0) {
                inflated = data;
            } else {
                inflated = Inflate.asString(getData());
            }
            return inflated;
        } else {
            return inflated;
        }
    }

    protected long getSignature() {
        return signature;
    }

    protected long getTimeBlob() {
        return timeBlob;
    }

    public long getUncompressedSize() {
        return uncompressedSize;
    }

    protected long getVersionNeeded() {
        return versionNeeded;
    }

    protected boolean isEncrypted() {
        return (this.bitFlag & 0x01) == 0x01;
    }

    protected boolean isUsingBit3TrailingDataDescriptor() {
        return (this.bitFlag & 0x0008) == 0x0008;
    }

    protected boolean isUsingUtf8() {
        return (this.bitFlag & 0x0800) == 0x0800;
    }

    protected boolean isUsingZip64() {
        return this.compressedSize == 0xFFFFFFFF
                || this.uncompressedSize == 0xFFFFFFFF;
    }

    @Override
    public String toString() {
        return fileName;
    }

}
