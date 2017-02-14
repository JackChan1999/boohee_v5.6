package com.google.zxing.client.result;

import com.google.zxing.Result;

import java.util.regex.Pattern;

public final class EmailDoCoMoResultParser extends AbstractDoCoMoResultParser {
    private static final Pattern ATEXT_ALPHANUMERIC = Pattern.compile("[a-zA-Z0-9@" +
            ".!#$%&'*+\\-/=?^_`{|}~]+");

    public EmailAddressParsedResult parse(Result result) {
        String rawText = ResultParser.getMassagedText(result);
        if (!rawText.startsWith("MATMSG:")) {
            return null;
        }
        String[] tos = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("TO:", rawText, true);
        if (tos == null) {
            return null;
        }
        for (String to : tos) {
            if (!isBasicallyValidEmailAddress(to)) {
                return null;
            }
        }
        return new EmailAddressParsedResult(tos, null, null, AbstractDoCoMoResultParser
                .matchSingleDoCoMoPrefixedField("SUB:", rawText, false),
                AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("BODY:", rawText, false));
    }

    static boolean isBasicallyValidEmailAddress(String email) {
        return email != null && ATEXT_ALPHANUMERIC.matcher(email).matches() && email.indexOf(64)
                >= 0;
    }
}
