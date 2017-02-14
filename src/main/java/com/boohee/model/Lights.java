package com.boohee.model;

import com.boohee.one.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class Lights extends ModelBase {
    public int calcium;
    public int copper;
    public int fat;
    public int fiber_dietary;
    public int fluorine;
    public int folacin;
    public int health;
    public int iodine;
    public int iron;
    public int kalium;
    public int lactoflavin;
    public int magnesium;
    public int manganese;
    public int natrium;
    public int niacin;
    public int phosphor;
    public int probiotic;
    public int saturated_fat;
    public int selenium;
    public int sugar;
    public int thiamine;
    public int vitamin_a;
    public int vitamin_b12;
    public int vitamin_b6;
    public int vitamin_c;
    public int vitamin_d;
    public int vitamin_e;
    public int vitamin_k;
    public int zinc;

    public static int lightToString(String value) {
        if (value.equals("fiber_dietary")) {
            return R.string.ln;
        }
        if (value.equals("saturated_fat")) {
            return R.string.a2l;
        }
        if (value.equals("fat")) {
            return R.string.lh;
        }
        if (value.equals("sugar")) {
            return R.string.a7i;
        }
        if (value.equals("natrium")) {
            return R.string.ws;
        }
        if (value.equals("probiotic")) {
            return R.string.a08;
        }
        if (value.equals("vitamin_a")) {
            return R.string.acc;
        }
        if (value.equals("thiamine")) {
            return R.string.ace;
        }
        if (value.equals("lactoflavin")) {
            return R.string.acf;
        }
        if (value.equals("vitamin_b6")) {
            return R.string.acg;
        }
        if (value.equals("vitamin_b12")) {
            return R.string.acd;
        }
        if (value.equals("vitamin_c")) {
            return R.string.ach;
        }
        if (value.equals("vitamin_d")) {
            return R.string.aci;
        }
        if (value.equals("vitamin_k")) {
            return R.string.acu;
        }
        if (value.equals("vitamin_e")) {
            return R.string.acj;
        }
        if (value.equals("calcium")) {
            return R.string.el;
        }
        if (value.equals("magnesium")) {
            return R.string.rk;
        }
        if (value.equals("niacin")) {
            return R.string.x7;
        }
        if (value.equals("iron")) {
            return R.string.pl;
        }
        if (value.equals("manganese")) {
            return R.string.rn;
        }
        if (value.equals("zinc")) {
            return R.string.aea;
        }
        if (value.equals("copper")) {
            return R.string.gy;
        }
        if (value.equals("kalium")) {
            return R.string.pv;
        }
        if (value.equals("phosphor")) {
            return R.string.zd;
        }
        if (value.equals("selenium")) {
            return R.string.a42;
        }
        if (value.equals("folacin")) {
            return R.string.m7;
        }
        if (value.equals("iodine")) {
            return R.string.pk;
        }
        if (value.equals("fluorine")) {
            return R.string.m6;
        }
        return 0;
    }

    public static int getLightDrawableM(int id) {
        switch (id) {
            case 1:
                return R.drawable.lq;
            case 2:
                return R.drawable.lu;
            case 3:
                return R.drawable.ls;
            default:
                return 0;
        }
    }

    public static int getLightDrawableL(int id) {
        switch (id) {
            case 1:
                return R.drawable.lp;
            case 2:
                return R.drawable.lt;
            case 3:
                return R.drawable.lr;
            default:
                return 0;
        }
    }

    public static List<Light> getMainLightMap(JSONObject object) throws JSONException {
        List<Light> list = new ArrayList();
        List<Light> listStatic = Light.getmainLightsList();
        int i = 0;
        while (i < listStatic.size()) {
            Iterator<String> keys = object.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if ((key.equals("fat") || key.equals("saturated_fat") || key.equals("sugar") ||
                        key.equals("fiber_dietary") || key.equals("natrium")) && ((Light)
                        listStatic.get(i)).element.equals(key)) {
                    list.add(new Light(key, ((Integer) object.get(key)).intValue()));
                }
            }
            i++;
        }
        return list;
    }

    public static List<Light> getSubLightMap(JSONObject object) throws JSONException {
        List<Light> list = new ArrayList();
        List<Light> listStatic = Light.getsubLightsList();
        int i = 0;
        while (i < listStatic.size()) {
            Iterator<String> keys = object.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (!(key.equals("fat") || key.equals("saturated_fat") || key.equals("sugar") ||
                        key.equals("fiber_dietary") || key.equals("natrium") || !((Light)
                        listStatic.get(i)).element.equals(key))) {
                    list.add(new Light(key, ((Integer) object.get(key)).intValue()));
                }
            }
            i++;
        }
        return list;
    }
}
