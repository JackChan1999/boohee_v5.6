package com.boohee.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiUtils {
    public static String convetToHtml(String str) {
        String regex = "\\[e\\](.*?)\\[/e\\]";
        Matcher matcher = Pattern.compile(regex).matcher(str);
        String emo = "";
        while (matcher.find()) {
            emo = matcher.group();
            str = str.replaceFirst(regex, "<img src=\"emoji_0x" + emo.substring(emo.indexOf("]")
                    + 1, emo.lastIndexOf("[")) + "\"/>");
        }
        return str;
    }
}
