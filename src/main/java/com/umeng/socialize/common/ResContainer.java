package com.umeng.socialize.common;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.tencent.tinker.loader.shareutil.ShareConstants;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class ResContainer {
    private static String mPackageName = "";
    private Context                        mContext;
    private Map<String, SocializeResource> mResources;

    public enum ResType {
        LAYOUT {
            public String toString() {
                return "layout";
            }
        },
        ID {
            public String toString() {
                return "id";
            }
        },
        DRAWABLE {
            public String toString() {
                return "drawable";
            }
        },
        STYLE {
            public String toString() {
                return "style";
            }
        },
        STRING {
            public String toString() {
                return "string";
            }
        },
        COLOR {
            public String toString() {
                return "color";
            }
        },
        DIMEN {
            public String toString() {
                return "dimen";
            }
        },
        RAW {
            public String toString() {
                return ShareConstants.DEXMODE_RAW;
            }
        },
        ANIM {
            public String toString() {
                return "anim";
            }
        },
        STYLEABLE {
            public String toString() {
                return "styleable";
            }
        };
    }

    public static class SocializeResource {
        public int mId;
        public boolean mIsCompleted = false;
        public String  mName;
        public ResType mType;

        public SocializeResource(ResType resType, String str) {
            this.mType = resType;
            this.mName = str;
        }
    }

    public ResContainer(Context context, Map<String, SocializeResource> map) {
        this.mResources = map;
        this.mContext = context;
    }

    public static void setPackageName(String str) {
        mPackageName = str;
    }

    public static int getResourceId(Context context, ResType resType, String str) {
        Resources resources = context.getResources();
        if (TextUtils.isEmpty(mPackageName)) {
            mPackageName = context.getPackageName();
        }
        int identifier = resources.getIdentifier(str, resType.toString(), mPackageName);
        if (identifier > 0) {
            return identifier;
        }
        throw new RuntimeException("获取资源ID失败:(packageName=" + mPackageName + " type=" + resType +
                " name=" + str);
    }

    public static String getString(Context context, String str) {
        return context.getString(getResourceId(context, ResType.STRING, str));
    }

    public synchronized Map<String, SocializeResource> batch() {
        Map<String, SocializeResource> map;
        if (this.mResources == null) {
            map = this.mResources;
        } else {
            for (String str : this.mResources.keySet()) {
                SocializeResource socializeResource = (SocializeResource) this.mResources.get(str);
                socializeResource.mId = getResourceId(this.mContext, socializeResource.mType,
                        socializeResource.mName);
                socializeResource.mIsCompleted = true;
            }
            map = this.mResources;
        }
        return map;
    }

    public static int[] getStyleableArrts(Context context, String str) {
        return getResourceDeclareStyleableIntArray(context, str);
    }

    private static final int[] getResourceDeclareStyleableIntArray(Context context, String str) {
        try {
            for (Field field : Class.forName(context.getPackageName() + ".R$styleable").getFields
                    ()) {
                if (field.getName().equals(str)) {
                    return (int[]) field.get(null);
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return null;
    }
}
