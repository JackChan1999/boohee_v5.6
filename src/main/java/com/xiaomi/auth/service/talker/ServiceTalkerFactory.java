package com.xiaomi.auth.service.talker;

public class ServiceTalkerFactory {
    private static /* synthetic */ int[]
            $SWITCH_TABLE$com$xiaomi$auth$service$talker$ServiceTalkerFactory$talkerType;

    public enum talkerType {
        GetToken,
        UserInfo,
        InvalidateToken
    }

    static /* synthetic */ int[]
    $SWITCH_TABLE$com$xiaomi$auth$service$talker$ServiceTalkerFactory$talkerType() {
        int[] iArr = $SWITCH_TABLE$com$xiaomi$auth$service$talker$ServiceTalkerFactory$talkerType;
        if (iArr == null) {
            iArr = new int[talkerType.values().length];
            try {
                iArr[talkerType.GetToken.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[talkerType.InvalidateToken.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[talkerType.UserInfo.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$xiaomi$auth$service$talker$ServiceTalkerFactory$talkerType = iArr;
        }
        return iArr;
    }

    public static ServiceTalker getTalker(talkerType type) {
        switch ($SWITCH_TABLE$com$xiaomi$auth$service$talker$ServiceTalkerFactory$talkerType()
                [type.ordinal()]) {
            case 1:
                return new TalkerGetToken();
            case 2:
                return new TalkerUserInfo();
            case 3:
                return new TalkerInvalidateToken();
            default:
                throw new IllegalArgumentException("unexpted type");
        }
    }
}
