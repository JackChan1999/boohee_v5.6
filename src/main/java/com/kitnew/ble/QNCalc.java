package com.kitnew.ble;

import com.kitnew.ble.utils.NumberUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QNCalc {
    private static final List<DeviceData> deviceDataList = new ArrayList();
    private int    age;
    private double bmi;
    private double bmr;
    private int    bodyage;
    private double bodyfat;
    private double bone;
    private byte[] data;
    private double egg;
    private int    gender;
    private int    height;
    private int    hip;
    private double lbm;
    private double muscle;
    private int    resistance;
    private int    resistance500;
    private double scale;
    private int    scaleType;
    private double score;
    private double skeletalMuscle;
    private double subfat;
    private int    visfat;
    private int    waistline;
    private double water;
    private double weight;

    public native String getDeviceJson();

    public native void init(int i, int i2, int i3, int i4, int i5, int i6, double d, byte[] bArr);

    public native void initDC(int i, int i2, int i3, int i4, int i5, int i6, double d, byte[] bArr);

    static {
        System.loadLibrary("yolanda_calc");
    }

    public static List<DeviceData> getDeviceDataList() {
        synchronized (deviceDataList) {
            if (deviceDataList.size() == 0) {
                try {
                    JSONArray jsonArray = new JSONArray(new QNCalc().getDeviceJson());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        DeviceData deviceData = new DeviceData();
                        deviceData.scaleName = jsonObject.getString("scale_name");
                        deviceData.internalModel = jsonObject.getString("internal_model");
                        deviceData.method = jsonObject.getInt("method");
                        deviceData.model = jsonObject.getString("model");
                        deviceDataList.add(deviceData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return deviceDataList;
    }

    public float calcScore(QNUser user, float bodyFat, float weight) {
        float weightScore = WeightCalc.getScore(weight, user.gender, user.height);
        float bodyfatScore = BodyfatCalc.getScore(bodyFat, user.gender);
        float bmiScore = BmiCalc.getScore(weight, (float) user.height);
        return NumberUtils.getOnePrecision(calcAvgValue(weightScore, bodyfatScore, bmiScore));
    }

    public static float calcAvgValue(float... values) {
        float sum = 0.0f;
        for (float f : values) {
            sum += f;
        }
        return sum / ((float) values.length);
    }

    private int calcAge(Date birthday) {
        return new Date().getYear() - birthday.getYear();
    }

    public int calcBodyAge(QNUser user, Date birthday, float bodyFat, float weight) {
        int bodyAge;
        int curAge = calcAge(birthday);
        float score = calcScore(user, bodyFat, weight);
        if (score < 60.0f) {
            bodyAge = curAge + 8;
        } else if (score < 70.0f) {
            bodyAge = curAge + 5;
        } else if (score < 75.0f) {
            bodyAge = curAge + 2;
        } else if (score < 80.0f) {
            bodyAge = curAge + 1;
        } else if (score < 85.0f) {
            bodyAge = curAge;
        } else if (score < 90.0f) {
            bodyAge = curAge - 1;
        } else if (score < 95.0f) {
            bodyAge = curAge - 3;
        } else if (score < 98.0f) {
            bodyAge = curAge - 5;
        } else {
            bodyAge = curAge - 8;
        }
        if (bodyAge <= 18) {
            return 18;
        }
        return bodyAge;
    }

    public String calcBodyShapeType(int gender) {
        int[] bodyFatValues = gender == 1 ? new int[]{11, 21, 26} : new int[]{21, 31, 36};
        float[] bmiValues = new float[]{18.5f, 25.0f, 30.0f};
        String[] contents = this.bmi < ((double) bmiValues[0]) ? new String[]{"偏瘦型", "运动不足型",
                "运动不足型", "隐形肥胖型"} : this.bmi < ((double) bmiValues[1]) ? new String[]{"偏瘦肌肉型",
                "标准型", "隐形肥胖型", "隐形肥胖型"} : this.bmi < ((double) bmiValues[2]) ? new
                String[]{"标准肌肉型", "标准肌肉型", "偏胖型", "肥胖型"} : new String[]{"非常肌肉型", "非常肌肉型", "偏胖型",
                "肥胖型"};
        return contents[getLevelIndex(bodyFatValues, this.bodyfat)];
    }

    private static int getLevelIndex(int[] values, double fat) {
        if (fat < ((double) values[0])) {
            return 0;
        }
        if (fat <= ((double) values[1])) {
            return 1;
        }
        if (fat <= ((double) values[2])) {
            return 2;
        }
        return 3;
    }

    public double getSkeletalMuscle() {
        return this.skeletalMuscle;
    }

    public double getScale() {
        return this.scale;
    }

    public byte[] getData() {
        return this.data;
    }

    public double getWeight() {
        return this.weight;
    }

    public double getBodyfat() {
        return this.bodyfat;
    }

    public double getSubfat() {
        return this.subfat;
    }

    public int getVisfat() {
        return this.visfat;
    }

    public double getWater() {
        return this.water;
    }

    public double getBmr() {
        return this.bmr;
    }

    public int getBodyage() {
        return this.bodyage;
    }

    public double getMuscle() {
        return this.muscle;
    }

    public double getBmi() {
        return this.bmi;
    }

    public double getBone() {
        return this.bone;
    }

    public double getScore() {
        return this.score;
    }

    public int getScaleType() {
        return this.scaleType;
    }

    public int getResistance() {
        return this.resistance;
    }

    public int getResistance500() {
        return this.resistance500;
    }

    public int getGender() {
        return this.gender;
    }

    public int getAge() {
        return this.age;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWaistline() {
        return this.waistline;
    }

    public int getHip() {
        return this.hip;
    }

    public double getLbm() {
        return this.lbm;
    }

    public double getEgg() {
        return this.egg;
    }
}
