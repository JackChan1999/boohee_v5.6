package com.google.zxing.client.result;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.tencent.connect.common.Constants;

import java.util.HashMap;
import java.util.Map;

public final class ExpandedProductResultParser extends ResultParser {
    public ExpandedProductParsedResult parse(Result result) {
        if (result.getBarcodeFormat() != BarcodeFormat.RSS_EXPANDED) {
            return null;
        }
        String rawText = ResultParser.getMassagedText(result);
        String productID = null;
        String sscc = null;
        String lotNumber = null;
        String productionDate = null;
        String packagingDate = null;
        String bestBeforeDate = null;
        String expirationDate = null;
        String weight = null;
        String weightType = null;
        String weightIncrement = null;
        String price = null;
        String priceIncrement = null;
        String priceCurrency = null;
        Map<String, String> uncommonAIs = new HashMap();
        int i = 0;
        while (i < rawText.length()) {
            String ai = findAIvalue(i, rawText);
            if (ai != null) {
                i += ai.length() + 2;
                String value = findValue(i, rawText);
                i += value.length();
                Object obj = -1;
                switch (ai.hashCode()) {
                    case 1536:
                        if (ai.equals("00")) {
                            obj = null;
                            break;
                        }
                        break;
                    case 1537:
                        if (ai.equals("01")) {
                            obj = 1;
                            break;
                        }
                        break;
                    case 1567:
                        if (ai.equals(Constants.VIA_REPORT_TYPE_SHARE_TO_QQ)) {
                            obj = 2;
                            break;
                        }
                        break;
                    case 1568:
                        if (ai.equals(Constants.VIA_REPORT_TYPE_SHARE_TO_QZONE)) {
                            obj = 3;
                            break;
                        }
                        break;
                    case 1570:
                        if (ai.equals(Constants.VIA_REPORT_TYPE_JOININ_GROUP)) {
                            obj = 4;
                            break;
                        }
                        break;
                    case 1572:
                        if (ai.equals(Constants.VIA_REPORT_TYPE_WPA_STATE)) {
                            obj = 5;
                            break;
                        }
                        break;
                    case 1574:
                        if (ai.equals("17")) {
                            obj = 6;
                            break;
                        }
                        break;
                    case 1567966:
                        if (ai.equals("3100")) {
                            obj = 7;
                            break;
                        }
                        break;
                    case 1567967:
                        if (ai.equals("3101")) {
                            obj = 8;
                            break;
                        }
                        break;
                    case 1567968:
                        if (ai.equals("3102")) {
                            obj = 9;
                            break;
                        }
                        break;
                    case 1567969:
                        if (ai.equals("3103")) {
                            obj = 10;
                            break;
                        }
                        break;
                    case 1567970:
                        if (ai.equals("3104")) {
                            obj = 11;
                            break;
                        }
                        break;
                    case 1567971:
                        if (ai.equals("3105")) {
                            obj = 12;
                            break;
                        }
                        break;
                    case 1567972:
                        if (ai.equals("3106")) {
                            obj = 13;
                            break;
                        }
                        break;
                    case 1567973:
                        if (ai.equals("3107")) {
                            obj = 14;
                            break;
                        }
                        break;
                    case 1567974:
                        if (ai.equals("3108")) {
                            obj = 15;
                            break;
                        }
                        break;
                    case 1567975:
                        if (ai.equals("3109")) {
                            obj = 16;
                            break;
                        }
                        break;
                    case 1568927:
                        if (ai.equals("3200")) {
                            obj = 17;
                            break;
                        }
                        break;
                    case 1568928:
                        if (ai.equals("3201")) {
                            obj = 18;
                            break;
                        }
                        break;
                    case 1568929:
                        if (ai.equals("3202")) {
                            obj = 19;
                            break;
                        }
                        break;
                    case 1568930:
                        if (ai.equals("3203")) {
                            obj = 20;
                            break;
                        }
                        break;
                    case 1568931:
                        if (ai.equals("3204")) {
                            obj = 21;
                            break;
                        }
                        break;
                    case 1568932:
                        if (ai.equals("3205")) {
                            obj = 22;
                            break;
                        }
                        break;
                    case 1568933:
                        if (ai.equals("3206")) {
                            obj = 23;
                            break;
                        }
                        break;
                    case 1568934:
                        if (ai.equals("3207")) {
                            obj = 24;
                            break;
                        }
                        break;
                    case 1568935:
                        if (ai.equals("3208")) {
                            obj = 25;
                            break;
                        }
                        break;
                    case 1568936:
                        if (ai.equals("3209")) {
                            obj = 26;
                            break;
                        }
                        break;
                    case 1575716:
                        if (ai.equals("3920")) {
                            obj = 27;
                            break;
                        }
                        break;
                    case 1575717:
                        if (ai.equals("3921")) {
                            obj = 28;
                            break;
                        }
                        break;
                    case 1575718:
                        if (ai.equals("3922")) {
                            obj = 29;
                            break;
                        }
                        break;
                    case 1575719:
                        if (ai.equals("3923")) {
                            obj = 30;
                            break;
                        }
                        break;
                    case 1575747:
                        if (ai.equals("3930")) {
                            obj = 31;
                            break;
                        }
                        break;
                    case 1575748:
                        if (ai.equals("3931")) {
                            obj = 32;
                            break;
                        }
                        break;
                    case 1575749:
                        if (ai.equals("3932")) {
                            obj = 33;
                            break;
                        }
                        break;
                    case 1575750:
                        if (ai.equals("3933")) {
                            obj = 34;
                            break;
                        }
                        break;
                }
                switch (obj) {
                    case null:
                        sscc = value;
                        break;
                    case 1:
                        productID = value;
                        break;
                    case 2:
                        lotNumber = value;
                        break;
                    case 3:
                        productionDate = value;
                        break;
                    case 4:
                        packagingDate = value;
                        break;
                    case 5:
                        bestBeforeDate = value;
                        break;
                    case 6:
                        expirationDate = value;
                        break;
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                        weight = value;
                        weightType = ExpandedProductParsedResult.KILOGRAM;
                        weightIncrement = ai.substring(3);
                        break;
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                        weight = value;
                        weightType = ExpandedProductParsedResult.POUND;
                        weightIncrement = ai.substring(3);
                        break;
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                        price = value;
                        priceIncrement = ai.substring(3);
                        break;
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                        if (value.length() >= 4) {
                            price = value.substring(3);
                            priceCurrency = value.substring(0, 3);
                            priceIncrement = ai.substring(3);
                            break;
                        }
                        return null;
                    default:
                        uncommonAIs.put(ai, value);
                        break;
                }
            }
            return null;
        }
        return new ExpandedProductParsedResult(rawText, productID, sscc, lotNumber,
                productionDate, packagingDate, bestBeforeDate, expirationDate, weight,
                weightType, weightIncrement, price, priceIncrement, priceCurrency, uncommonAIs);
    }

    private static String findAIvalue(int i, String rawText) {
        if (rawText.charAt(i) != '(') {
            return null;
        }
        CharSequence rawTextAux = rawText.substring(i + 1);
        StringBuilder buf = new StringBuilder();
        for (int index = 0; index < rawTextAux.length(); index++) {
            char currentChar = rawTextAux.charAt(index);
            if (currentChar == ')') {
                return buf.toString();
            }
            if (currentChar < '0' || currentChar > '9') {
                return null;
            }
            buf.append(currentChar);
        }
        return buf.toString();
    }

    private static String findValue(int i, String rawText) {
        StringBuilder buf = new StringBuilder();
        String rawTextAux = rawText.substring(i);
        for (int index = 0; index < rawTextAux.length(); index++) {
            char c = rawTextAux.charAt(index);
            if (c == '(') {
                if (findAIvalue(index, rawTextAux) != null) {
                    break;
                }
                buf.append('(');
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }
}
