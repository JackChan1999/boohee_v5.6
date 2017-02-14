package com.baidu.location;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.boohee.model.PartnerBlock;
import com.boohee.utils.Utils;
import org.json.JSONObject;

public final class BDLocation implements n, Parcelable {
    public static final Creator CREATOR = new Creator() {
        public BDLocation a(Parcel parcel) {
            return new BDLocation(parcel);
        }

        public BDLocation[] a(int i) {
            return new BDLocation[i];
        }

        public /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
            return a(parcel);
        }

        public /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return a(i);
        }
    };
    public static final int OPERATORS_TYPE_MOBILE = 1;
    public static final int OPERATORS_TYPE_TELECOMU = 3;
    public static final int OPERATORS_TYPE_UNICOM = 2;
    public static final int OPERATORS_TYPE_UNKONW = 0;
    public static final int TypeCacheLocation = 65;
    public static final int TypeCriteriaException = 62;
    public static final int TypeGpsLocation = 61;
    public static final int TypeNetWorkException = 63;
    public static final int TypeNetWorkLocation = 161;
    public static final int TypeNone = 0;
    public static final int TypeOffLineLocation = 66;
    public static final int TypeOffLineLocationFail = 67;
    public static final int TypeOffLineLocationNetworkFail = 68;
    public static final int TypeServerError = 167;
    private int g0;
    private String gF;
    private int gG;
    private double gH;
    private String gI;
    private boolean gJ;
    private boolean gK;
    private String gL;
    private float gM;
    private double gN;
    private boolean gO;
    private a gP;
    private boolean gQ;
    private String gR;
    private float gS;
    private boolean gT;
    private int gU;
    private double gV;
    private boolean gW;
    private float gX;
    private String gY;
    private String gZ;

    public class a {
        final /* synthetic */ BDLocation a;
        public String byte = null;
        public String do = null;
        public String for = null;
        public String if = null;
        public String int = null;
        public String new = null;
        public String try = null;

        public a(BDLocation bDLocation) {
            this.a = bDLocation;
        }
    }

    public BDLocation() {
        this.gU = 0;
        this.gY = null;
        this.gH = Double.MIN_VALUE;
        this.gV = Double.MIN_VALUE;
        this.gJ = false;
        this.gN = Double.MIN_VALUE;
        this.gK = false;
        this.gX = 0.0f;
        this.gQ = false;
        this.gS = 0.0f;
        this.gW = false;
        this.gG = -1;
        this.gM = -1.0f;
        this.gZ = null;
        this.gT = false;
        this.gF = null;
        this.gO = false;
        this.gP = new a(this);
        this.gI = null;
        this.gR = null;
        this.gL = "";
    }

    private BDLocation(Parcel parcel) {
        this.gU = 0;
        this.gY = null;
        this.gH = Double.MIN_VALUE;
        this.gV = Double.MIN_VALUE;
        this.gJ = false;
        this.gN = Double.MIN_VALUE;
        this.gK = false;
        this.gX = 0.0f;
        this.gQ = false;
        this.gS = 0.0f;
        this.gW = false;
        this.gG = -1;
        this.gM = -1.0f;
        this.gZ = null;
        this.gT = false;
        this.gF = null;
        this.gO = false;
        this.gP = new a(this);
        this.gI = null;
        this.gR = null;
        this.gL = "";
        this.gU = parcel.readInt();
        this.gY = parcel.readString();
        this.gH = parcel.readDouble();
        this.gV = parcel.readDouble();
        this.gN = parcel.readDouble();
        this.gX = parcel.readFloat();
        this.gS = parcel.readFloat();
        this.gG = parcel.readInt();
        this.gM = parcel.readFloat();
        this.gI = parcel.readString();
        this.gR = parcel.readString();
        this.gP.if = parcel.readString();
        this.gP.new = parcel.readString();
        this.gP.int = parcel.readString();
        this.gP.byte = parcel.readString();
        this.gP.do = parcel.readString();
        this.gP.for = parcel.readString();
        this.gP.try = parcel.readString();
        boolean[] zArr = new boolean[6];
        parcel.readBooleanArray(zArr);
        this.gJ = zArr[0];
        this.gK = zArr[1];
        this.gQ = zArr[2];
        this.gW = zArr[3];
        this.gT = zArr[4];
        this.gO = zArr[5];
        this.g0 = parcel.readInt();
        this.gL = parcel.readString();
    }

    public BDLocation(BDLocation bDLocation) {
        this.gU = 0;
        this.gY = null;
        this.gH = Double.MIN_VALUE;
        this.gV = Double.MIN_VALUE;
        this.gJ = false;
        this.gN = Double.MIN_VALUE;
        this.gK = false;
        this.gX = 0.0f;
        this.gQ = false;
        this.gS = 0.0f;
        this.gW = false;
        this.gG = -1;
        this.gM = -1.0f;
        this.gZ = null;
        this.gT = false;
        this.gF = null;
        this.gO = false;
        this.gP = new a(this);
        this.gI = null;
        this.gR = null;
        this.gL = "";
        this.gU = bDLocation.gU;
        this.gY = bDLocation.gY;
        this.gH = bDLocation.gH;
        this.gV = bDLocation.gV;
        this.gJ = bDLocation.gJ;
        bDLocation.gN = bDLocation.gN;
        this.gK = bDLocation.gK;
        this.gX = bDLocation.gX;
        this.gQ = bDLocation.gQ;
        this.gS = bDLocation.gS;
        this.gW = bDLocation.gW;
        this.gG = bDLocation.gG;
        this.gM = bDLocation.gM;
        this.gZ = bDLocation.gZ;
        this.gT = bDLocation.gT;
        this.gF = bDLocation.gF;
        this.gO = bDLocation.gO;
        this.gP = new a(this);
        this.gP.if = bDLocation.gP.if;
        this.gP.new = bDLocation.gP.new;
        this.gP.int = bDLocation.gP.int;
        this.gP.byte = bDLocation.gP.byte;
        this.gP.do = bDLocation.gP.do;
        this.gP.for = bDLocation.gP.for;
        this.gP.try = bDLocation.gP.try;
        this.gI = bDLocation.gI;
        this.gR = bDLocation.gR;
        this.g0 = bDLocation.g0;
        this.gL = bDLocation.gL;
    }

    protected BDLocation(String str) {
        this.gU = 0;
        this.gY = null;
        this.gH = Double.MIN_VALUE;
        this.gV = Double.MIN_VALUE;
        this.gJ = false;
        this.gN = Double.MIN_VALUE;
        this.gK = false;
        this.gX = 0.0f;
        this.gQ = false;
        this.gS = 0.0f;
        this.gW = false;
        this.gG = -1;
        this.gM = -1.0f;
        this.gZ = null;
        this.gT = false;
        this.gF = null;
        this.gO = false;
        this.gP = new a(this);
        this.gI = null;
        this.gR = null;
        this.gL = "";
        if (str != null && !str.equals("")) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                JSONObject jSONObject2 = jSONObject.getJSONObject("result");
                int parseInt = Integer.parseInt(jSONObject2.getString("error"));
                setLocType(parseInt);
                setTime(jSONObject2.getString("time"));
                if (parseInt == 61) {
                    jSONObject = jSONObject.getJSONObject(Utils.RESPONSE_CONTENT);
                    jSONObject2 = jSONObject.getJSONObject(PartnerBlock.TYPE_POINT);
                    setLatitude(Double.parseDouble(jSONObject2.getString("y")));
                    setLongitude(Double.parseDouble(jSONObject2.getString("x")));
                    setRadius(Float.parseFloat(jSONObject.getString(com.baidu.location.a.a.else)));
                    setSpeed(Float.parseFloat(jSONObject.getString("s")));
                    setDirection(Float.parseFloat(jSONObject.getString("d")));
                    setSatelliteNumber(Integer.parseInt(jSONObject.getString("n")));
                } else if (parseInt == 161) {
                    jSONObject2 = jSONObject.getJSONObject(Utils.RESPONSE_CONTENT);
                    jSONObject = jSONObject2.getJSONObject(PartnerBlock.TYPE_POINT);
                    setLatitude(Double.parseDouble(jSONObject.getString("y")));
                    setLongitude(Double.parseDouble(jSONObject.getString("x")));
                    setRadius(Float.parseFloat(jSONObject2.getString(com.baidu.location.a.a.else)));
                    if (jSONObject2.has("addr")) {
                        String string = jSONObject2.getString("addr");
                        this.gP.try = string;
                        String[] split = string.split(",");
                        this.gP.if = split[0];
                        this.gP.new = split[1];
                        this.gP.int = split[2];
                        this.gP.byte = split[3];
                        this.gP.do = split[4];
                        this.gP.for = split[5];
                        string = ((this.gP.if.contains("北京") && this.gP.new.contains("北京")) || ((this.gP.if.contains("上海") && this.gP.new.contains("上海")) || ((this.gP.if.contains("天津") && this.gP.new.contains("天津")) || (this.gP.if.contains("重庆") && this.gP.new.contains("重庆"))))) ? this.gP.if : this.gP.if + this.gP.new;
                        this.gP.try = string + this.gP.int + this.gP.byte + this.gP.do;
                        this.gT = true;
                    } else {
                        this.gT = false;
                        setAddrStr(null);
                    }
                    if (jSONObject2.has("floor")) {
                        this.gI = jSONObject2.getString("floor");
                        if (TextUtils.isEmpty(this.gI)) {
                            this.gI = null;
                        }
                    }
                    if (jSONObject2.has("loctp")) {
                        this.gR = jSONObject2.getString("loctp");
                        if (TextUtils.isEmpty(this.gR)) {
                            this.gR = null;
                        }
                    }
                } else if (parseInt == 66 || parseInt == 68) {
                    jSONObject = jSONObject.getJSONObject(Utils.RESPONSE_CONTENT);
                    jSONObject2 = jSONObject.getJSONObject(PartnerBlock.TYPE_POINT);
                    setLatitude(Double.parseDouble(jSONObject2.getString("y")));
                    setLongitude(Double.parseDouble(jSONObject2.getString("x")));
                    setRadius(Float.parseFloat(jSONObject.getString(com.baidu.location.a.a.else)));
                    if(Boolean.valueOf(Boolean.parseBoolean(jSONObject.getString("isCellChanged"))));
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.gU = 0;
                this.gT = false;
            }
        }
    }

    private String bn() {
        return this.gL;
    }

    private static String bo() {
        return Build.MODEL;
    }

    private void if(Boolean bool) {
        this.gO = bool.booleanValue();
    }

    protected String bm() {
        return null;
    }

    protected void byte(int i) {
        this.g0 = i;
    }

    public int describeContents() {
        return 0;
    }

    public String getAdUrl(String str) {
        String valueOf = String.valueOf(this.gH);
        String valueOf2 = String.valueOf(this.gV);
        String bn = bn();
        return "http://lba.baidu.com/" + "?a=" + CommonEncrypt.a("ak=" + str + com.alipay.sdk.sys.a.b + "lat=" + valueOf + com.alipay.sdk.sys.a.b + "lng=" + valueOf2 + com.alipay.sdk.sys.a.b + "cu=" + bn + com.alipay.sdk.sys.a.b + "mb=" + bo());
    }

    public String getAddrStr() {
        return this.gP.try;
    }

    public double getAltitude() {
        return this.gN;
    }

    public String getCity() {
        return this.gP.new;
    }

    public String getCityCode() {
        return this.gP.for;
    }

    public String getCoorType() {
        return this.gZ;
    }

    public float getDirection() {
        return this.gM;
    }

    public String getDistrict() {
        return this.gP.int;
    }

    public String getFloor() {
        return this.gI;
    }

    public double getLatitude() {
        return this.gH;
    }

    public int getLocType() {
        return this.gU;
    }

    public double getLongitude() {
        return this.gV;
    }

    public String getNetworkLocationType() {
        return this.gR;
    }

    public int getOperators() {
        return this.g0;
    }

    public String getProvince() {
        return this.gP.if;
    }

    public float getRadius() {
        return this.gS;
    }

    public int getSatelliteNumber() {
        this.gW = true;
        return this.gG;
    }

    public float getSpeed() {
        return this.gX;
    }

    public String getStreet() {
        return this.gP.byte;
    }

    public String getStreetNumber() {
        return this.gP.do;
    }

    public String getTime() {
        return this.gY;
    }

    public boolean hasAddr() {
        return this.gT;
    }

    public boolean hasAltitude() {
        return this.gJ;
    }

    public boolean hasRadius() {
        return this.gQ;
    }

    public boolean hasSateNumber() {
        return this.gW;
    }

    public boolean hasSpeed() {
        return this.gK;
    }

    public void internalSet(int i, String str) {
        if (str != null && i == 0) {
            this.gL = str;
        }
    }

    public boolean isCellChangeFlag() {
        return this.gO;
    }

    protected BDLocation p(String str) {
        return null;
    }

    public void setAddrStr(String str) {
        this.gF = str;
        if (str == null) {
            this.gT = false;
        } else {
            this.gT = true;
        }
    }

    public void setAltitude(double d) {
        this.gN = d;
        this.gJ = true;
    }

    public void setCoorType(String str) {
        this.gZ = str;
    }

    public void setDirection(float f) {
        this.gM = f;
    }

    public void setLatitude(double d) {
        this.gH = d;
    }

    public void setLocType(int i) {
        this.gU = i;
    }

    public void setLongitude(double d) {
        this.gV = d;
    }

    public void setRadius(float f) {
        this.gS = f;
        this.gQ = true;
    }

    public void setSatelliteNumber(int i) {
        this.gG = i;
    }

    public void setSpeed(float f) {
        this.gX = f;
        this.gK = true;
    }

    public void setTime(String str) {
        this.gY = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.gU);
        parcel.writeString(this.gY);
        parcel.writeDouble(this.gH);
        parcel.writeDouble(this.gV);
        parcel.writeDouble(this.gN);
        parcel.writeFloat(this.gX);
        parcel.writeFloat(this.gS);
        parcel.writeInt(this.gG);
        parcel.writeFloat(this.gM);
        parcel.writeString(this.gI);
        parcel.writeString(this.gR);
        parcel.writeString(this.gP.if);
        parcel.writeString(this.gP.new);
        parcel.writeString(this.gP.int);
        parcel.writeString(this.gP.byte);
        parcel.writeString(this.gP.do);
        parcel.writeString(this.gP.for);
        parcel.writeString(this.gP.try);
        parcel.writeBooleanArray(new boolean[]{this.gJ, this.gK, this.gQ, this.gW, this.gT, this.gO});
        parcel.writeInt(this.g0);
        parcel.writeString(this.gL);
    }
}
