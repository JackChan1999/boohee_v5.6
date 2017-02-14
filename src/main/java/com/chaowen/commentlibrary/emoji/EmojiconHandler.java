package com.chaowen.commentlibrary.emoji;

import android.content.Context;
import android.text.Spannable;
import android.util.SparseIntArray;

import com.chaowen.commentlibrary.R;

public final class EmojiconHandler {
    private static final SparseIntArray sEmojisMap    = new SparseIntArray(846);
    private static final SparseIntArray sSoftbanksMap = new SparseIntArray(471);

    static {
        sEmojisMap.put(128516, R.drawable.emoji_1f604);
        sEmojisMap.put(128522, R.drawable.emoji_1f60a);
        sEmojisMap.put(128521, R.drawable.emoji_1f609);
        sEmojisMap.put(128525, R.drawable.emoji_1f60d);
        sEmojisMap.put(128536, R.drawable.emoji_1f618);
        sEmojisMap.put(128540, R.drawable.emoji_1f61c);
        sEmojisMap.put(128541, R.drawable.emoji_1f61d);
        sEmojisMap.put(128563, R.drawable.emoji_1f633);
        sEmojisMap.put(128556, R.drawable.emoji_1f62c);
        sEmojisMap.put(128532, R.drawable.emoji_1f614);
        sEmojisMap.put(128530, R.drawable.emoji_1f612);
        sEmojisMap.put(128546, R.drawable.emoji_1f622);
        sEmojisMap.put(128514, R.drawable.emoji_1f602);
        sEmojisMap.put(128557, R.drawable.emoji_1f62d);
        sEmojisMap.put(128531, R.drawable.emoji_1f613);
        sEmojisMap.put(128552, R.drawable.emoji_1f628);
        sEmojisMap.put(128561, R.drawable.emoji_1f631);
        sEmojisMap.put(128545, R.drawable.emoji_1f621);
        sEmojisMap.put(128534, R.drawable.emoji_1f616);
        sEmojisMap.put(128523, R.drawable.emoji_1f60b);
        sEmojisMap.put(128567, R.drawable.emoji_1f637);
        sEmojisMap.put(128526, R.drawable.emoji_1f60e);
        sEmojisMap.put(128565, R.drawable.emoji_1f635);
        sEmojisMap.put(128127, R.drawable.emoji_1f47f);
        sEmojisMap.put(128528, R.drawable.emoji_1f610);
        sEmojisMap.put(128519, R.drawable.emoji_1f607);
        sEmojisMap.put(128527, R.drawable.emoji_1f60f);
        sEmojisMap.put(128169, R.drawable.emoji_1f4a9);
        sEmojisMap.put(128293, R.drawable.emoji_1f525);
        sEmojisMap.put(10024, R.drawable.emoji_2728);
        sEmojisMap.put(11088, R.drawable.emoji_2b50);
        sEmojisMap.put(9889, R.drawable.emoji_26a1);
        sEmojisMap.put(128162, R.drawable.emoji_1f4a2);
        sEmojisMap.put(128167, R.drawable.emoji_1f4a7);
        sEmojisMap.put(128164, R.drawable.emoji_1f4a4);
        sEmojisMap.put(128064, R.drawable.emoji_1f440);
        sEmojisMap.put(128068, R.drawable.emoji_1f444);
        sEmojisMap.put(128077, R.drawable.emoji_1f44d);
        sEmojisMap.put(128078, R.drawable.emoji_1f44e);
        sEmojisMap.put(128076, R.drawable.emoji_1f44c);
        sEmojisMap.put(128074, R.drawable.emoji_1f44a);
        sEmojisMap.put(9994, R.drawable.emoji_270a);
        sEmojisMap.put(9996, R.drawable.emoji_270c);
        sEmojisMap.put(128591, R.drawable.emoji_1f64f);
        sEmojisMap.put(128079, R.drawable.emoji_1f44f);
        sEmojisMap.put(128170, R.drawable.emoji_1f4aa);
        sEmojisMap.put(128694, R.drawable.emoji_1f6b6);
        sEmojisMap.put(127939, R.drawable.emoji_1f3c3);
        sEmojisMap.put(128131, R.drawable.emoji_1f483);
        sEmojisMap.put(128089, R.drawable.emoji_1f459);
        sEmojisMap.put(10084, R.drawable.emoji_2764);
        sEmojisMap.put(128148, R.drawable.emoji_1f494);
        sEmojisMap.put(128152, R.drawable.emoji_1f498);
        sEmojisMap.put(128142, R.drawable.emoji_1f48e);
        sEmojisMap.put(128099, R.drawable.emoji_1f463);
        sEmojisMap.put(127801, R.drawable.emoji_1f339);
        sEmojisMap.put(127876, R.drawable.emoji_1f384);
        sEmojisMap.put(128684, R.drawable.emoji_1f6ac);
        sEmojisMap.put(127881, R.drawable.emoji_1f389);
        sEmojisMap.put(10067, R.drawable.emoji_2753);
        sEmojisMap.put(128226, R.drawable.emoji_1f4e2);
        sEmojisMap.put(128123, R.drawable.emoji_1f47b);
        sEmojisMap.put(127834, R.drawable.emoji_1f35a);
        sEmojisMap.put(127837, R.drawable.emoji_1f35d);
        sEmojisMap.put(127836, R.drawable.emoji_1f35c);
        sEmojisMap.put(127833, R.drawable.emoji_1f359);
        sEmojisMap.put(127847, R.drawable.emoji_1f367);
        sEmojisMap.put(127843, R.drawable.emoji_1f363);
        sEmojisMap.put(127874, R.drawable.emoji_1f382);
        sEmojisMap.put(127838, R.drawable.emoji_1f35e);
        sEmojisMap.put(127828, R.drawable.emoji_1f354);
        sEmojisMap.put(127859, R.drawable.emoji_1f373);
        sEmojisMap.put(127839, R.drawable.emoji_1f35f);
        sEmojisMap.put(127866, R.drawable.emoji_1f37a);
        sEmojisMap.put(127867, R.drawable.emoji_1f37b);
        sEmojisMap.put(127864, R.drawable.emoji_1f378);
        sEmojisMap.put(9749, R.drawable.emoji_2615);
        sEmojisMap.put(127822, R.drawable.emoji_1f34e);
        sEmojisMap.put(127818, R.drawable.emoji_1f34a);
        sEmojisMap.put(127827, R.drawable.emoji_1f353);
        sEmojisMap.put(127817, R.drawable.emoji_1f349);
    }

    private EmojiconHandler() {
    }

    private static boolean isSoftBankEmoji(char c) {
        return (c >> 12) == 14;
    }

    private static int getEmojiResource(Context context, int codePoint) {
        return sEmojisMap.get(codePoint);
    }

    private static int getSoftbankEmojiResource(char c) {
        return sSoftbanksMap.get(c);
    }

    public static void addEmojis(Context context, Spannable text, int emojiSize) {
        addEmojis(context, text, emojiSize, 0, -1, false);
    }

    public static void addEmojis(Context context, Spannable text, int emojiSize, int index, int
            length) {
        addEmojis(context, text, emojiSize, index, length, false);
    }

    public static void addEmojis(Context context, Spannable text, int emojiSize, boolean
            useSystemDefault) {
        addEmojis(context, text, emojiSize, 0, -1, useSystemDefault);
    }

    public static void addEmojis(Context context, Spannable text, int emojiSize, int index, int
            length, boolean useSystemDefault) {
        if (!useSystemDefault) {
            int textLength = text.length();
            int textLengthToProcess = (length < 0 || length >= textLength - index) ? textLength :
                    length + index;
            EmojiconSpan[] oldSpans = (EmojiconSpan[]) text.getSpans(0, textLength, EmojiconSpan
                    .class);
            for (Object removeSpan : oldSpans) {
                text.removeSpan(removeSpan);
            }
            int i = index;
            while (i < textLengthToProcess) {
                int skip = 0;
                int icon = 0;
                char c = text.charAt(i);
                if (isSoftBankEmoji(c)) {
                    icon = getSoftbankEmojiResource(c);
                    skip = icon == 0 ? 0 : 1;
                }
                if (icon == 0) {
                    int unicode = Character.codePointAt(text, i);
                    skip = Character.charCount(unicode);
                    if (unicode > 255) {
                        icon = getEmojiResource(context, unicode);
                    }
                    if (icon == 0 && i + skip < textLengthToProcess) {
                        Character.codePointAt(text, i + skip);
                    }
                }
                if (icon > 0) {
                    text.setSpan(new EmojiconSpan(context, icon, emojiSize), i, i + skip, 33);
                }
                i += skip;
            }
        }
    }
}
