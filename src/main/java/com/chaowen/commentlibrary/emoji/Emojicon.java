package com.chaowen.commentlibrary.emoji;

import java.io.Serializable;

public class Emojicon implements Serializable {
    private static final long serialVersionUID = 1;
    private String emoji;
    private int    icon;
    private char   value;

    private Emojicon() {
    }

    public Emojicon(String emoji) {
        this.emoji = emoji;
    }

    public static Emojicon fromResource(int icon, int value) {
        Emojicon emoji = new Emojicon();
        emoji.icon = icon;
        emoji.value = (char) value;
        return emoji;
    }

    public static Emojicon fromCodePoint(int codePoint) {
        Emojicon emoji = new Emojicon();
        emoji.emoji = newString(codePoint);
        return emoji;
    }

    public static Emojicon fromChar(char ch) {
        Emojicon emoji = new Emojicon();
        emoji.emoji = Character.toString(ch);
        return emoji;
    }

    public static Emojicon fromChars(String chars) {
        Emojicon emoji = new Emojicon();
        emoji.emoji = chars;
        return emoji;
    }

    public static final String newString(int codePoint) {
        if (Character.charCount(codePoint) == 1) {
            return String.valueOf(codePoint);
        }
        return new String(Character.toChars(codePoint));
    }

    public char getValue() {
        return this.value;
    }

    public int getIcon() {
        return this.icon;
    }

    public String getEmoji() {
        return this.emoji;
    }

    public boolean equals(Object o) {
        return (o instanceof Emojicon) && this.emoji.equals(((Emojicon) o).emoji);
    }

    public int hashCode() {
        return this.emoji.hashCode();
    }
}
