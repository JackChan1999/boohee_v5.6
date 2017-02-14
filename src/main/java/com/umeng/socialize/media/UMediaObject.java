package com.umeng.socialize.media;

import android.os.Parcelable;

import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

public interface UMediaObject extends Parcelable, Cloneable {

    public interface FetchMediaDataListener {
        void onComplete(byte[] bArr);

        void onStart();
    }

    public enum MediaType {
        IMAGE {
            public String toString() {
                return "0";
            }
        },
        VEDIO {
            public String toString() {
                return "1";
            }
        },
        MUSIC {
            public String toString() {
                return "2";
            }
        },
        TEXT {
            public String toString() {
                return "3";
            }
        },
        TEXT_IMAGE {
            public String toString() {
                return "4";
            }
        },
        WEBPAGE {
            public String toString() {
                return "5";
            }
        };

        public static MediaType convertToEmun(String str) {
            for (MediaType mediaType : values()) {
                if (mediaType.toString().equals(str)) {
                    return mediaType;
                }
            }
            return null;
        }
    }

    MediaType getMediaType();

    SHARE_MEDIA getTargetPlatform();

    boolean isMultiMedia();

    boolean isUrlMedia();

    void toByte(FetchMediaDataListener fetchMediaDataListener);

    byte[] toByte();

    String toUrl();

    Map<String, Object> toUrlExtraParams();
}
