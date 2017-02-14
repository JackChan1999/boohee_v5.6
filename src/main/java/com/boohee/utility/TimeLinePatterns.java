package com.boohee.utility;

import java.util.regex.Pattern;

public class TimeLinePatterns {
    public static final String  BOOHEE_SCHEME  = "boohee://";
    public static final Pattern BOOHEE_URL     = Pattern.compile
            ("boohee://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[\\u4e00-\\u9fa5]*[a-zA-Z0-9+&@#/%=~_|]");
    public static final Pattern EMOTION_URL    = Pattern.compile("\\[(\\S+?)\\]");
    public static final String  MENTION_SCHEME = "com.boohee.one://";
    public static final Pattern MENTION_URL    = Pattern.compile
            ("@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}");
    public static final String  TOPIC_SCHEME   = "com.boohee.one.topic://";
    public static final Pattern TOPIC_URL      = Pattern.compile
            ("#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#");
    public static final String  WEB_SCHEME     = "http://";
    public static final Pattern WEB_URL        = Pattern.compile("http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]");
}
