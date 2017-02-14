package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;

public class SymbolTable {
    public static final int DEFAULT_TABLE_SIZE = 512;
    public static final int MAX_BUCKET_LENTH = 8;
    public static final int MAX_SIZE = 4096;
    private final Entry[] buckets;
    private final int indexMask;
    private int size;
    private final String[] symbols;
    private final char[][] symbols_char;

    protected static final class Entry {
        public final byte[] bytes;
        public final char[] characters;
        public final int hashCode;
        public Entry next;
        public final String symbol;

        public Entry(char[] ch, int offset, int length, int hash, Entry next) {
            this.characters = new char[length];
            System.arraycopy(ch, offset, this.characters, 0, length);
            this.symbol = new String(this.characters).intern();
            this.next = next;
            this.hashCode = hash;
            this.bytes = null;
        }

        public Entry(String text, int offset, int length, int hash, Entry next) {
            this.symbol = SymbolTable.subString(text, offset, length).intern();
            this.characters = this.symbol.toCharArray();
            this.next = next;
            this.hashCode = hash;
            this.bytes = null;
        }
    }

    public SymbolTable() {
        this(512);
        addSymbol("$ref", 0, 4, "$ref".hashCode());
        addSymbol(JSON.DEFAULT_TYPE_KEY, 0, 5, JSON.DEFAULT_TYPE_KEY.hashCode());
    }

    public SymbolTable(int tableSize) {
        this.size = 0;
        this.indexMask = tableSize - 1;
        this.buckets = new Entry[tableSize];
        this.symbols = new String[tableSize];
        this.symbols_char = new char[tableSize][];
    }

    public String addSymbol(char[] buffer, int offset, int len) {
        return addSymbol(buffer, offset, len, hash(buffer, offset, len));
    }

    public String addSymbol(char[] buffer, int offset, int len, int hash) {
        char[] characters;
        int i;
        int bucket = hash & this.indexMask;
        String sym = this.symbols[bucket];
        boolean match = true;
        if (sym != null) {
            if (sym.length() == len) {
                characters = this.symbols_char[bucket];
                for (i = 0; i < len; i++) {
                    if (buffer[offset + i] != characters[i]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return sym;
                }
            }
            match = false;
        }
        int entryIndex = 0;
        Entry entry = this.buckets[bucket];
        while (entry != null) {
            characters = entry.characters;
            if (len == characters.length && hash == entry.hashCode) {
                boolean eq = true;
                for (i = 0; i < len; i++) {
                    if (buffer[offset + i] != characters[i]) {
                        eq = false;
                        break;
                    }
                }
                if (eq) {
                    return entry.symbol;
                }
                entryIndex++;
            }
            entry = entry.next;
        }
        if (entryIndex >= 8) {
            return new String(buffer, offset, len);
        }
        if (this.size >= 4096) {
            return new String(buffer, offset, len);
        }
        entry = new Entry(buffer, offset, len, hash, this.buckets[bucket]);
        this.buckets[bucket] = entry;
        if (match) {
            this.symbols[bucket] = entry.symbol;
            this.symbols_char[bucket] = entry.characters;
        }
        this.size++;
        return entry.symbol;
    }

    public String addSymbol(String buffer, int offset, int len, int hash) {
        char[] characters;
        int i;
        int bucket = hash & this.indexMask;
        String sym = this.symbols[bucket];
        boolean match = true;
        if (sym != null) {
            if (sym.length() == len) {
                characters = this.symbols_char[bucket];
                for (i = 0; i < len; i++) {
                    if (buffer.charAt(offset + i) != characters[i]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return sym;
                }
            }
            match = false;
        }
        int entryIndex = 0;
        Entry entry = this.buckets[bucket];
        while (entry != null) {
            characters = entry.characters;
            if (len == characters.length && hash == entry.hashCode) {
                boolean eq = true;
                for (i = 0; i < len; i++) {
                    if (buffer.charAt(offset + i) != characters[i]) {
                        eq = false;
                        break;
                    }
                }
                if (eq) {
                    return entry.symbol;
                }
                entryIndex++;
            }
            entry = entry.next;
        }
        if (entryIndex >= 8) {
            return subString(buffer, offset, len);
        }
        if (this.size >= 4096) {
            return subString(buffer, offset, len);
        }
        entry = new Entry(buffer, offset, len, hash, this.buckets[bucket]);
        this.buckets[bucket] = entry;
        if (match) {
            this.symbols[bucket] = entry.symbol;
            this.symbols_char[bucket] = entry.characters;
        }
        this.size++;
        return entry.symbol;
    }

    private static String subString(String src, int offset, int len) {
        char[] chars = new char[len];
        for (int i = offset; i < offset + len; i++) {
            chars[i - offset] = src.charAt(i);
        }
        return new String(chars);
    }

    public int size() {
        return this.size;
    }

    public static final int hash(char[] buffer, int offset, int len) {
        int h = 0;
        int i = 0;
        int off = offset;
        while (i < len) {
            h = (h * 31) + buffer[off];
            i++;
            off++;
        }
        return h;
    }
}
