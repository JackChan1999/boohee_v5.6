package com.google.zxing.oned;

public final class CodaBarWriter extends OneDimensionalCodeWriter {
    private static final char[] ALT_START_END_CHARS                           = new char[]{'T',
            'N', '*', 'E'};
    private static final char[] CHARS_WHICH_ARE_TEN_LENGTH_EACH_AFTER_DECODED = new char[]{'/',
            ':', '+', '.'};
    private static final char   DEFAULT_GUARD                                 = START_END_CHARS[0];
    private static final char[] START_END_CHARS                               = new char[]{'A',
            'B', 'C', 'D'};

    public boolean[] encode(String contents) {
        if (contents.length() < 2) {
            contents = DEFAULT_GUARD + contents + DEFAULT_GUARD;
        } else {
            char firstChar = Character.toUpperCase(contents.charAt(0));
            char lastChar = Character.toUpperCase(contents.charAt(contents.length() - 1));
            boolean startsNormal = CodaBarReader.arrayContains(START_END_CHARS, firstChar);
            boolean endsNormal = CodaBarReader.arrayContains(START_END_CHARS, lastChar);
            boolean startsAlt = CodaBarReader.arrayContains(ALT_START_END_CHARS, firstChar);
            boolean endsAlt = CodaBarReader.arrayContains(ALT_START_END_CHARS, lastChar);
            if (startsNormal) {
                if (!endsNormal) {
                    throw new IllegalArgumentException("Invalid start/end guards: " + contents);
                }
            } else if (startsAlt) {
                if (!endsAlt) {
                    throw new IllegalArgumentException("Invalid start/end guards: " + contents);
                }
            } else if (endsNormal || endsAlt) {
                throw new IllegalArgumentException("Invalid start/end guards: " + contents);
            } else {
                contents = DEFAULT_GUARD + contents + DEFAULT_GUARD;
            }
        }
        int resultLength = 20;
        int i = 1;
        while (i < contents.length() - 1) {
            if (Character.isDigit(contents.charAt(i)) || contents.charAt(i) == '-' || contents
                    .charAt(i) == '$') {
                resultLength += 9;
            } else if (CodaBarReader.arrayContains(CHARS_WHICH_ARE_TEN_LENGTH_EACH_AFTER_DECODED,
                    contents.charAt(i))) {
                resultLength += 10;
            } else {
                throw new IllegalArgumentException("Cannot encode : '" + contents.charAt(i) + '\'');
            }
            i++;
        }
        boolean[] result = new boolean[(resultLength + (contents.length() - 1))];
        int position = 0;
        int index = 0;
        while (index < contents.length()) {
            boolean color;
            int counter;
            int bit;
            char c = Character.toUpperCase(contents.charAt(index));
            if (index == 0 || index == contents.length() - 1) {
                switch (c) {
                    case '*':
                        c = 'C';
                        break;
                    case 'E':
                        c = 'D';
                        break;
                    case 'N':
                        c = 'B';
                        break;
                    case 'T':
                        c = 'A';
                        break;
                }
            }
            int code = 0;
            i = 0;
            while (i < CodaBarReader.ALPHABET.length) {
                if (c == CodaBarReader.ALPHABET[i]) {
                    code = CodaBarReader.CHARACTER_ENCODINGS[i];
                    color = true;
                    counter = 0;
                    bit = 0;
                    while (bit < 7) {
                        result[position] = color;
                        position++;
                        if (((code >> (6 - bit)) & 1) != 0 || counter == 1) {
                            color = color;
                            bit++;
                            counter = 0;
                        } else {
                            counter++;
                        }
                    }
                    if (index < contents.length() - 1) {
                        result[position] = false;
                        position++;
                    }
                    index++;
                } else {
                    i++;
                }
            }
            color = true;
            counter = 0;
            bit = 0;
            while (bit < 7) {
                result[position] = color;
                position++;
                if (((code >> (6 - bit)) & 1) != 0) {
                }
                if (color) {
                }
                bit++;
                counter = 0;
            }
            if (index < contents.length() - 1) {
                result[position] = false;
                position++;
            }
            index++;
        }
        return result;
    }
}
