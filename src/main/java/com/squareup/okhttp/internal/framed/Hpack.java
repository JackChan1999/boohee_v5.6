package com.squareup.okhttp.internal.framed;

import com.alipay.sdk.cons.b;
import com.alipay.sdk.cons.c;
import com.alipay.sdk.packet.d;
import com.boohee.one.bet.BetBrowserFragment;
import com.tencent.connect.common.Constants;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import okio.Source;

final class Hpack {
    private static final Map<ByteString, Integer> NAME_TO_FIRST_INDEX = nameToFirstIndex();
    private static final int                      PREFIX_4_BITS       = 15;
    private static final int                      PREFIX_5_BITS       = 31;
    private static final int                      PREFIX_6_BITS       = 63;
    private static final int                      PREFIX_7_BITS       = 127;
    private static final Header[]                 STATIC_HEADER_TABLE = new Header[]{new Header
            (Header.TARGET_AUTHORITY, ""), new Header(Header.TARGET_METHOD, "GET"), new Header
            (Header.TARGET_METHOD, Constants.HTTP_POST), new Header(Header.TARGET_PATH, "/"), new
            Header(Header.TARGET_PATH, "/index.html"), new Header(Header.TARGET_SCHEME, "http"),
            new Header(Header.TARGET_SCHEME, b.a), new Header(Header.RESPONSE_STATUS, "200"), new
            Header(Header.RESPONSE_STATUS, "204"), new Header(Header.RESPONSE_STATUS, "206"), new
            Header(Header.RESPONSE_STATUS, "304"), new Header(Header.RESPONSE_STATUS, "400"), new
            Header(Header.RESPONSE_STATUS, "404"), new Header(Header.RESPONSE_STATUS, "500"), new
            Header("accept-charset", ""), new Header("accept-encoding", "gzip, deflate"), new
            Header("accept-language", ""), new Header("accept-ranges", ""), new Header("accept",
            ""), new Header("access-control-allow-origin", ""), new Header("age", ""), new Header
            ("allow", ""), new Header("authorization", ""), new Header("cache-control", ""), new
            Header("content-disposition", ""), new Header("content-encoding", ""), new Header
            ("content-language", ""), new Header("content-length", ""), new Header
            ("content-location", ""), new Header("content-range", ""), new Header(d.d, ""), new
            Header("cookie", ""), new Header("date", ""), new Header("etag", ""), new Header
            ("expect", ""), new Header("expires", ""), new Header("from", ""), new Header(c.f,
            ""), new Header("if-match", ""), new Header("if-modified-since", ""), new Header
            ("if-none-match", ""), new Header("if-range", ""), new Header("if-unmodified-since",
            ""), new Header("last-modified", ""), new Header("link", ""), new Header("location",
            ""), new Header("max-forwards", ""), new Header("proxy-authenticate", ""), new Header
            ("proxy-authorization", ""), new Header("range", ""), new Header("referer", ""), new
            Header(BetBrowserFragment.REFFRESH, ""), new Header("retry-after", ""), new Header
            ("server", ""), new Header("set-cookie", ""), new Header("strict-transport-security",
            ""), new Header("transfer-encoding", ""), new Header("user-agent", ""), new Header
            ("vary", ""), new Header(SocializeProtocolConstants.PROTOCOL_KEY_VERIFY_MEDIA, ""),
            new Header("www-authenticate", "")};

    static final class Reader {
        Header[] dynamicTable          = new Header[8];
        int      dynamicTableByteCount = 0;
        int      headerCount           = 0;
        private final List<Header> headerList = new ArrayList();
        private int headerTableSizeSetting;
        private int maxDynamicTableByteCount;
        int nextHeaderIndex = (this.dynamicTable.length - 1);
        private final BufferedSource source;

        Reader(int headerTableSizeSetting, Source source) {
            this.headerTableSizeSetting = headerTableSizeSetting;
            this.maxDynamicTableByteCount = headerTableSizeSetting;
            this.source = Okio.buffer(source);
        }

        int maxDynamicTableByteCount() {
            return this.maxDynamicTableByteCount;
        }

        void headerTableSizeSetting(int headerTableSizeSetting) {
            this.headerTableSizeSetting = headerTableSizeSetting;
            this.maxDynamicTableByteCount = headerTableSizeSetting;
            adjustDynamicTableByteCount();
        }

        private void adjustDynamicTableByteCount() {
            if (this.maxDynamicTableByteCount >= this.dynamicTableByteCount) {
                return;
            }
            if (this.maxDynamicTableByteCount == 0) {
                clearDynamicTable();
            } else {
                evictToRecoverBytes(this.dynamicTableByteCount - this.maxDynamicTableByteCount);
            }
        }

        private void clearDynamicTable() {
            this.headerList.clear();
            Arrays.fill(this.dynamicTable, null);
            this.nextHeaderIndex = this.dynamicTable.length - 1;
            this.headerCount = 0;
            this.dynamicTableByteCount = 0;
        }

        private int evictToRecoverBytes(int bytesToRecover) {
            int entriesToEvict = 0;
            if (bytesToRecover > 0) {
                for (int j = this.dynamicTable.length - 1; j >= this.nextHeaderIndex &&
                        bytesToRecover > 0; j--) {
                    bytesToRecover -= this.dynamicTable[j].hpackSize;
                    this.dynamicTableByteCount -= this.dynamicTable[j].hpackSize;
                    this.headerCount--;
                    entriesToEvict++;
                }
                System.arraycopy(this.dynamicTable, this.nextHeaderIndex + 1, this.dynamicTable,
                        (this.nextHeaderIndex + 1) + entriesToEvict, this.headerCount);
                this.nextHeaderIndex += entriesToEvict;
            }
            return entriesToEvict;
        }

        void readHeaders() throws IOException {
            while (!this.source.exhausted()) {
                int b = this.source.readByte() & 255;
                if (b == 128) {
                    throw new IOException("index == 0");
                } else if ((b & 128) == 128) {
                    readIndexedHeader(readInt(b, 127) - 1);
                } else if (b == 64) {
                    readLiteralHeaderWithIncrementalIndexingNewName();
                } else if ((b & 64) == 64) {
                    readLiteralHeaderWithIncrementalIndexingIndexedName(readInt(b, 63) - 1);
                } else if ((b & 32) == 32) {
                    this.maxDynamicTableByteCount = readInt(b, 31);
                    if (this.maxDynamicTableByteCount < 0 || this.maxDynamicTableByteCount > this
                            .headerTableSizeSetting) {
                        throw new IOException("Invalid dynamic table size update " + this
                                .maxDynamicTableByteCount);
                    }
                    adjustDynamicTableByteCount();
                } else if (b == 16 || b == 0) {
                    readLiteralHeaderWithoutIndexingNewName();
                } else {
                    readLiteralHeaderWithoutIndexingIndexedName(readInt(b, 15) - 1);
                }
            }
        }

        public List<Header> getAndResetHeaderList() {
            List<Header> result = new ArrayList(this.headerList);
            this.headerList.clear();
            return result;
        }

        private void readIndexedHeader(int index) throws IOException {
            if (isStaticHeader(index)) {
                this.headerList.add(Hpack.STATIC_HEADER_TABLE[index]);
                return;
            }
            int dynamicTableIndex = dynamicTableIndex(index - Hpack.STATIC_HEADER_TABLE.length);
            if (dynamicTableIndex < 0 || dynamicTableIndex > this.dynamicTable.length - 1) {
                throw new IOException("Header index too large " + (index + 1));
            }
            this.headerList.add(this.dynamicTable[dynamicTableIndex]);
        }

        private int dynamicTableIndex(int index) {
            return (this.nextHeaderIndex + 1) + index;
        }

        private void readLiteralHeaderWithoutIndexingIndexedName(int index) throws IOException {
            this.headerList.add(new Header(getName(index), readByteString()));
        }

        private void readLiteralHeaderWithoutIndexingNewName() throws IOException {
            this.headerList.add(new Header(Hpack.checkLowercase(readByteString()), readByteString
                    ()));
        }

        private void readLiteralHeaderWithIncrementalIndexingIndexedName(int nameIndex) throws
                IOException {
            insertIntoDynamicTable(-1, new Header(getName(nameIndex), readByteString()));
        }

        private void readLiteralHeaderWithIncrementalIndexingNewName() throws IOException {
            insertIntoDynamicTable(-1, new Header(Hpack.checkLowercase(readByteString()),
                    readByteString()));
        }

        private ByteString getName(int index) {
            if (isStaticHeader(index)) {
                return Hpack.STATIC_HEADER_TABLE[index].name;
            }
            return this.dynamicTable[dynamicTableIndex(index - Hpack.STATIC_HEADER_TABLE.length)]
                    .name;
        }

        private boolean isStaticHeader(int index) {
            return index >= 0 && index <= Hpack.STATIC_HEADER_TABLE.length - 1;
        }

        private void insertIntoDynamicTable(int index, Header entry) {
            this.headerList.add(entry);
            int delta = entry.hpackSize;
            if (index != -1) {
                delta -= this.dynamicTable[dynamicTableIndex(index)].hpackSize;
            }
            if (delta > this.maxDynamicTableByteCount) {
                clearDynamicTable();
                return;
            }
            int entriesEvicted = evictToRecoverBytes((this.dynamicTableByteCount + delta) - this
                    .maxDynamicTableByteCount);
            if (index == -1) {
                if (this.headerCount + 1 > this.dynamicTable.length) {
                    Header[] doubled = new Header[(this.dynamicTable.length * 2)];
                    System.arraycopy(this.dynamicTable, 0, doubled, this.dynamicTable.length,
                            this.dynamicTable.length);
                    this.nextHeaderIndex = this.dynamicTable.length - 1;
                    this.dynamicTable = doubled;
                }
                index = this.nextHeaderIndex;
                this.nextHeaderIndex = index - 1;
                this.dynamicTable[index] = entry;
                this.headerCount++;
            } else {
                this.dynamicTable[index + (dynamicTableIndex(index) + entriesEvicted)] = entry;
            }
            this.dynamicTableByteCount += delta;
        }

        private int readByte() throws IOException {
            return this.source.readByte() & 255;
        }

        int readInt(int firstByte, int prefixMask) throws IOException {
            int prefix = firstByte & prefixMask;
            if (prefix < prefixMask) {
                return prefix;
            }
            int result = prefixMask;
            int shift = 0;
            while (true) {
                int b = readByte();
                if ((b & 128) == 0) {
                    return result + (b << shift);
                }
                result += (b & 127) << shift;
                shift += 7;
            }
        }

        ByteString readByteString() throws IOException {
            int firstByte = readByte();
            boolean huffmanDecode = (firstByte & 128) == 128;
            int length = readInt(firstByte, 127);
            if (huffmanDecode) {
                return ByteString.of(Huffman.get().decode(this.source.readByteArray((long)
                        length)));
            }
            return this.source.readByteString((long) length);
        }
    }

    static final class Writer {
        private final Buffer out;

        Writer(Buffer out) {
            this.out = out;
        }

        void writeHeaders(List<Header> headerBlock) throws IOException {
            int size = headerBlock.size();
            for (int i = 0; i < size; i++) {
                ByteString name = ((Header) headerBlock.get(i)).name.toAsciiLowercase();
                Integer staticIndex = (Integer) Hpack.NAME_TO_FIRST_INDEX.get(name);
                if (staticIndex != null) {
                    writeInt(staticIndex.intValue() + 1, 15, 0);
                    writeByteString(((Header) headerBlock.get(i)).value);
                } else {
                    this.out.writeByte(0);
                    writeByteString(name);
                    writeByteString(((Header) headerBlock.get(i)).value);
                }
            }
        }

        void writeInt(int value, int prefixMask, int bits) throws IOException {
            if (value < prefixMask) {
                this.out.writeByte(bits | value);
                return;
            }
            this.out.writeByte(bits | prefixMask);
            value -= prefixMask;
            while (value >= 128) {
                this.out.writeByte((value & 127) | 128);
                value >>>= 7;
            }
            this.out.writeByte(value);
        }

        void writeByteString(ByteString data) throws IOException {
            writeInt(data.size(), 127, 0);
            this.out.write(data);
        }
    }

    private Hpack() {
    }

    private static Map<ByteString, Integer> nameToFirstIndex() {
        Map<ByteString, Integer> result = new LinkedHashMap(STATIC_HEADER_TABLE.length);
        for (int i = 0; i < STATIC_HEADER_TABLE.length; i++) {
            if (!result.containsKey(STATIC_HEADER_TABLE[i].name)) {
                result.put(STATIC_HEADER_TABLE[i].name, Integer.valueOf(i));
            }
        }
        return Collections.unmodifiableMap(result);
    }

    private static ByteString checkLowercase(ByteString name) throws IOException {
        int i = 0;
        int length = name.size();
        while (i < length) {
            byte c = name.getByte(i);
            if (c < (byte) 65 || c > (byte) 90) {
                i++;
            } else {
                throw new IOException("PROTOCOL_ERROR response malformed: mixed case name: " + name.utf8());
            }
        }
        return name;
    }
}
