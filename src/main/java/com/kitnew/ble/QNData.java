package com.kitnew.ble;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QNData {
    public static final int TYPE_BMI              = 3;
    public static final int TYPE_BMR              = 12;
    public static final int TYPE_BODYAGE          = 17;
    public static final int TYPE_BODYFAT          = 4;
    public static final int TYPE_BODY_SHAPE       = 13;
    public static final int TYPE_BONE             = 10;
    public static final int TYPE_FATTY_LIVER_RISK = 16;
    public static final int TYPE_FAT_FREE_WEIGHT  = 14;
    public static final int TYPE_PROTEIN          = 11;
    public static final int TYPE_SINEW            = 18;
    public static final int TYPE_SKELETAL_MUSCLE  = 9;
    public static final int TYPE_SUBFAT           = 5;
    public static final int TYPE_VISFAT           = 6;
    public static final int TYPE_WATER            = 7;
    public static final int TYPE_WEIGHT           = 2;
    public static final int TYPE_WHR              = 15;
    private static int bodyShape;
    private static int bodyage;
    private static int fatFreeWeight;
    private static int fattyLiverRisk;
    private static int sinew;
    private static int whr;
    Date birthday;
    Date createTime;
    final List<QNItemData> datas = new ArrayList();
    int    gender;
    int    height;
    String mac;
    int    resistance;
    int    resistance500;
    String scaleName;
    String userId;

    static void setBodyShape(int bodyShape) {
        bodyShape = bodyShape;
    }

    static void setFatFreeWeight(int fatFreeWeight) {
        fatFreeWeight = fatFreeWeight;
    }

    static void setBodyage(int bodyage) {
        bodyage = bodyage;
    }

    static void setSinew(int sinew) {
        sinew = sinew;
    }

    static void setWhr(int whr) {
        whr = whr;
    }

    static void setFattyLiverRisk(int fattyLiverRisk) {
        fattyLiverRisk = fattyLiverRisk;
    }

    public static boolean isShowBodyShape() {
        return bodyShape == 1;
    }

    public static boolean isShowFfw() {
        return fatFreeWeight == 1;
    }

    public static boolean isShowBodyAge() {
        return bodyage == 1;
    }

    public static boolean isShowSinew() {
        return sinew == 1;
    }

    public static boolean isShowWhr() {
        return whr == 1;
    }

    public static boolean isShowFattyLiveRisk() {
        return fattyLiverRisk == 1;
    }

    public static String getNameWithType(int type) {
        switch (type) {
            case 2:
                return "体重";
            case 3:
                return "BMI";
            case 4:
                return "体脂率";
            case 5:
                return "皮下脂肪率";
            case 6:
                return "内脏脂肪等级";
            case 7:
                return "体水分";
            case 9:
                return "骨骼肌率";
            case 10:
                return "骨量";
            case 11:
                return "蛋白质";
            case 12:
                return "基础代谢量";
            case 13:
                return "体型";
            case 14:
                return "去脂体重";
            case 15:
                return "腰臀比";
            case 16:
                return "脂肪肝风险";
            case 17:
                return "体年龄";
            case 18:
                return "肌肉量";
            default:
                return null;
        }
    }

    public QNData(List<QNItemData> datas) {
        this.datas.addAll(datas);
    }

    public int size() {
        return this.datas.size();
    }

    public void addItemData(QNItemData data) {
        this.datas.add(data);
    }

    public float getWeight() {
        return getFloatValue(2);
    }

    public List<QNItemData> getAll() {
        return this.datas;
    }

    public float getFloatValue(int type) {
        for (QNItemData itemData : this.datas) {
            if (itemData.type == type) {
                return itemData.value;
            }
        }
        return 0.0f;
    }

    public int getIntValue(int type) {
        for (QNItemData itemData : this.datas) {
            if (itemData.type == type) {
                return (int) itemData.value;
            }
        }
        return 0;
    }

    public String getUserId() {
        return this.userId;
    }

    void setUserData(QNUser user) {
        this.userId = user.id;
        this.birthday = user.birthday;
        this.gender = user.gender;
        this.height = user.height;
    }

    void setDeviceData(QNBleDevice bleDevice) {
        this.mac = bleDevice.mac;
        this.scaleName = bleDevice.deviceName;
    }

    void setResistance(int resistance) {
        this.resistance = resistance;
    }

    void setResistance500(int resistance500) {
        this.resistance500 = resistance500;
    }

    void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    QNUser getUser() {
        QNUser qnUser = new QNUser(this.userId);
        qnUser.height = this.height;
        qnUser.gender = this.gender;
        qnUser.birthday = this.birthday;
        qnUser.weight = getWeight();
        qnUser.resistance = this.resistance;
        return qnUser;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    org.json.JSONObject toJson() {
        /*
        r10 = this;
        r5 = new org.json.JSONObject;
        r5.<init>();
        r7 = "business_account";
        r8 = r10.userId;	 Catch:{ JSONException -> 0x0086 }
        r5.put(r7, r8);	 Catch:{ JSONException -> 0x0086 }
        r7 = "height";
        r8 = r10.height;	 Catch:{ JSONException -> 0x0086 }
        r5.put(r7, r8);	 Catch:{ JSONException -> 0x0086 }
        r0 = new java.text.SimpleDateFormat;	 Catch:{ JSONException -> 0x0086 }
        r7 = "yyyy-MM-dd";
        r0.<init>(r7);	 Catch:{ JSONException -> 0x0086 }
        r7 = "birthday";
        r8 = r10.birthday;	 Catch:{ JSONException -> 0x0086 }
        r8 = r0.format(r8);	 Catch:{ JSONException -> 0x0086 }
        r5.put(r7, r8);	 Catch:{ JSONException -> 0x0086 }
        r7 = "gender";
        r8 = r10.gender;	 Catch:{ JSONException -> 0x0086 }
        r5.put(r7, r8);	 Catch:{ JSONException -> 0x0086 }
        r7 = "mac";
        r8 = r10.mac;	 Catch:{ JSONException -> 0x0086 }
        r5.put(r7, r8);	 Catch:{ JSONException -> 0x0086 }
        r7 = "scale_name";
        r8 = r10.scaleName;	 Catch:{ JSONException -> 0x0086 }
        r5.put(r7, r8);	 Catch:{ JSONException -> 0x0086 }
        r7 = "resistance";
        r8 = r10.resistance;	 Catch:{ JSONException -> 0x0086 }
        r5.put(r7, r8);	 Catch:{ JSONException -> 0x0086 }
        r7 = "second_resistance";
        r8 = r10.resistance500;	 Catch:{ JSONException -> 0x0086 }
        r5.put(r7, r8);	 Catch:{ JSONException -> 0x0086 }
        r1 = new java.text.SimpleDateFormat;	 Catch:{ JSONException -> 0x0086 }
        r7 = "yyyy-MM-dd";
        r1.<init>(r7);	 Catch:{ JSONException -> 0x0086 }
        r7 = "local_updated_at";
        r8 = r10.createTime;	 Catch:{ JSONException -> 0x0086 }
        r8 = r1.format(r8);	 Catch:{ JSONException -> 0x0086 }
        r5.put(r7, r8);	 Catch:{ JSONException -> 0x0086 }
        r7 = r10.datas;	 Catch:{ JSONException -> 0x0086 }
        r3 = r7.iterator();	 Catch:{ JSONException -> 0x0086 }
    L_0x006b:
        r7 = r3.hasNext();	 Catch:{ JSONException -> 0x0086 }
        if (r7 == 0) goto L_0x008a;
    L_0x0071:
        r4 = r3.next();	 Catch:{ JSONException -> 0x0086 }
        r4 = (com.kitnew.ble.QNItemData) r4;	 Catch:{ JSONException -> 0x0086 }
        r6 = 0;
        r7 = r4.type;	 Catch:{ JSONException -> 0x0086 }
        switch(r7) {
            case 2: goto L_0x008b;
            case 3: goto L_0x008f;
            case 4: goto L_0x0093;
            case 5: goto L_0x0097;
            case 6: goto L_0x009b;
            case 7: goto L_0x009f;
            case 8: goto L_0x007d;
            case 9: goto L_0x00a3;
            case 10: goto L_0x00a7;
            case 11: goto L_0x00ab;
            case 12: goto L_0x00af;
            case 13: goto L_0x00b3;
            case 14: goto L_0x00b6;
            case 15: goto L_0x00b9;
            case 16: goto L_0x00bc;
            case 17: goto L_0x00bf;
            case 18: goto L_0x00c2;
            default: goto L_0x007d;
        };	 Catch:{ JSONException -> 0x0086 }
    L_0x007d:
        if (r6 == 0) goto L_0x006b;
    L_0x007f:
        r7 = r4.value;	 Catch:{ JSONException -> 0x0086 }
        r8 = (double) r7;	 Catch:{ JSONException -> 0x0086 }
        r5.put(r6, r8);	 Catch:{ JSONException -> 0x0086 }
        goto L_0x006b;
    L_0x0086:
        r2 = move-exception;
        r2.printStackTrace();
    L_0x008a:
        return r5;
    L_0x008b:
        r6 = "weight";
        goto L_0x007d;
    L_0x008f:
        r6 = "bmi";
        goto L_0x007d;
    L_0x0093:
        r6 = "bodyfat";
        goto L_0x007d;
    L_0x0097:
        r6 = "subfat";
        goto L_0x007d;
    L_0x009b:
        r6 = "visfat";
        goto L_0x007d;
    L_0x009f:
        r6 = "water";
        goto L_0x007d;
    L_0x00a3:
        r6 = "muscle";
        goto L_0x007d;
    L_0x00a7:
        r6 = "bone";
        goto L_0x007d;
    L_0x00ab:
        r6 = "protein";
        goto L_0x007d;
    L_0x00af:
        r6 = "bmr";
        goto L_0x007d;
    L_0x00b3:
        r6 = "body_shape";
    L_0x00b6:
        r6 = "fat_free_weight";
    L_0x00b9:
        r6 = "whr";
    L_0x00bc:
        r6 = "fatty_liver_risk";
    L_0x00bf:
        r6 = "bodyage";
    L_0x00c2:
        r6 = "sinew";
        goto L_0x007d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.kitnew.ble.QNData.toJson():org.json.JSONObject");
    }
}
