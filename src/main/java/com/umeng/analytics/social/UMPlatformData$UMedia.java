package com.umeng.analytics.social;

import com.umeng.socialize.common.SocialSNSHelper;

public enum UMPlatformData$UMedia {
    SINA_WEIBO {
        public String toString() {
            return "sina";
        }
    },
    TENCENT_WEIBO {
        public String toString() {
            return "tencent";
        }
    },
    TENCENT_QZONE {
        public String toString() {
            return "qzone";
        }
    },
    TENCENT_QQ {
        public String toString() {
            return SocialSNSHelper.SOCIALIZE_QQ_KEY;
        }
    },
    WEIXIN_FRIENDS {
        public String toString() {
            return "wxsesion";
        }
    },
    WEIXIN_CIRCLE {
        public String toString() {
            return "wxtimeline";
        }
    },
    RENREN {
        public String toString() {
            return SocialSNSHelper.SOCIALIZE_RENREN_KEY;
        }
    },
    DOUBAN {
        public String toString() {
            return SocialSNSHelper.SOCIALIZE_DOUBAN_KEY;
        }
    }
}
