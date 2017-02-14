package com.squareup.leakcanary;

import android.os.Build;
import android.os.Build.VERSION;

import com.squareup.leakcanary.ExcludedRefs.Builder;
import com.squareup.leakcanary.internal.LeakCanaryInternals;

import java.util.EnumSet;
import java.util.Iterator;

public enum AndroidExcludedRefs {
    ;

    final boolean applies;

    abstract void add(Builder builder);

    static {
        boolean z;
        String str = "ACTIVITY_CLIENT_RECORD__NEXT_IDLE";
        if (VERSION.SDK_INT < 19 || VERSION.SDK_INT > 21) {
            z = false;
        } else {
            z = true;
        }
        ACTIVITY_CLIENT_RECORD__NEXT_IDLE = new AndroidExcludedRefs(str, 0, z) {
            void add(Builder excluded) {
                excluded.instanceField("android.app.ActivityThread$ActivityClientRecord",
                        "nextIdle");
            }
        };
        str = "SPAN_CONTROLLER";
        if (VERSION.SDK_INT <= 19) {
            z = true;
        } else {
            z = false;
        }
        SPAN_CONTROLLER = new AndroidExcludedRefs(str, 1, z) {
            void add(Builder excluded) {
                excluded.instanceField("android.widget.Editor$EasyEditSpanController", "this$0");
                excluded.instanceField("android.widget.Editor$SpanController", "this$0");
            }
        };
        str = "MEDIA_SESSION_LEGACY_HELPER__SINSTANCE";
        if (VERSION.SDK_INT == 21) {
            z = true;
        } else {
            z = false;
        }
        MEDIA_SESSION_LEGACY_HELPER__SINSTANCE = new AndroidExcludedRefs(str, 2, z) {
            void add(Builder excluded) {
                excluded.staticField("android.media.session.MediaSessionLegacyHelper", "sInstance");
            }
        };
        str = "TEXT_LINE__SCACHED";
        if (VERSION.SDK_INT < 22) {
            z = true;
        } else {
            z = false;
        }
        TEXT_LINE__SCACHED = new AndroidExcludedRefs(str, 3, z) {
            void add(Builder excluded) {
                excluded.staticField("android.text.TextLine", "sCached");
            }
        };
        str = "BLOCKING_QUEUE";
        if (VERSION.SDK_INT < 21) {
            z = true;
        } else {
            z = false;
        }
        BLOCKING_QUEUE = new AndroidExcludedRefs(str, 4, z) {
            void add(Builder excluded) {
                excluded.instanceField("android.os.Message", "obj");
                excluded.instanceField("android.os.Message", "next");
                excluded.instanceField("android.os.Message", "target");
            }
        };
        str = "INPUT_METHOD_MANAGER__SERVED_VIEW";
        if (VERSION.SDK_INT < 16 || VERSION.SDK_INT > 22) {
            z = false;
        } else {
            z = true;
        }
        INPUT_METHOD_MANAGER__SERVED_VIEW = new AndroidExcludedRefs(str, 5, z) {
            void add(Builder excluded) {
                excluded.instanceField("android.view.inputmethod.InputMethodManager",
                        "mNextServedView");
                excluded.instanceField("android.view.inputmethod.InputMethodManager",
                        "mServedView");
                excluded.instanceField("android.view.inputmethod.InputMethodManager",
                        "mServedInputConnection");
            }
        };
        str = "INPUT_METHOD_MANAGER__ROOT_VIEW";
        z = VERSION.SDK_INT >= 15 && VERSION.SDK_INT <= 22;
        INPUT_METHOD_MANAGER__ROOT_VIEW = new AndroidExcludedRefs(str, 6, z) {
            void add(Builder excluded) {
                excluded.instanceField("android.view.inputmethod.InputMethodManager",
                        "mCurRootView");
            }
        };
        str = "LAYOUT_TRANSITION";
        if (VERSION.SDK_INT < 14 || VERSION.SDK_INT > 22) {
            z = false;
        } else {
            z = true;
        }
        LAYOUT_TRANSITION = new AndroidExcludedRefs(str, 7, z) {
            void add(Builder excluded) {
                excluded.instanceField("android.animation.LayoutTransition$1", "val$parent");
            }
        };
        str = "SPELL_CHECKER_SESSION";
        if (VERSION.SDK_INT >= 16 || VERSION.SDK_INT <= 22) {
            z = true;
        } else {
            z = false;
        }
        SPELL_CHECKER_SESSION = new AndroidExcludedRefs(str, 8, z) {
            void add(Builder excluded) {
                excluded.instanceField("android.view.textservice.SpellCheckerSession$1", "this$0");
            }
        };
        str = "ACTIVITY_CHOOSE_MODEL";
        if (VERSION.SDK_INT <= 14 || VERSION.SDK_INT > 22) {
            z = false;
        } else {
            z = true;
        }
        ACTIVITY_CHOOSE_MODEL = new AndroidExcludedRefs(str, 9, z) {
            void add(Builder excluded) {
                excluded.staticField("android.support.v7.internal.widget.ActivityChooserModel",
                        "mActivityChoserModelPolicy");
                excluded.staticField("android.widget.ActivityChooserModel",
                        "mActivityChoserModelPolicy");
            }
        };
        SPEECH_RECOGNIZER = new AndroidExcludedRefs("SPEECH_RECOGNIZER", 10, VERSION.SDK_INT < 21) {
            void add(Builder excluded) {
                excluded.instanceField("android.speech.SpeechRecognizer$InternalListener",
                        "this$0");
            }
        };
        str = "ACCOUNT_MANAGER";
        if (VERSION.SDK_INT <= 5 || VERSION.SDK_INT > 22) {
            z = false;
        } else {
            z = true;
        }
        ACCOUNT_MANAGER = new AndroidExcludedRefs(str, 11, z) {
            void add(Builder excluded) {
                excluded.instanceField("android.accounts.AccountManager$AmsTask$Response",
                        "this$1");
            }
        };
        str = "DEVICE_POLICY_MANAGER__SETTINGS_OBSERVER";
        if (LeakCanaryInternals.MOTOROLA.equals(Build.MANUFACTURER) && VERSION.SDK_INT == 19) {
            z = true;
        } else {
            z = false;
        }
        DEVICE_POLICY_MANAGER__SETTINGS_OBSERVER = new AndroidExcludedRefs(str, 12, z) {
            void add(Builder excluded) {
                if (LeakCanaryInternals.MOTOROLA.equals(Build.MANUFACTURER) && VERSION.SDK_INT ==
                        19) {
                    excluded.instanceField("android.app.admin" +
                            ".DevicePolicyManager$SettingsObserver", "this$0");
                }
            }
        };
        str = "SPEN_GESTURE_MANAGER";
        z = LeakCanaryInternals.SAMSUNG.equals(Build.MANUFACTURER) && VERSION.SDK_INT == 19;
        SPEN_GESTURE_MANAGER = new AndroidExcludedRefs(str, 13, z) {
            void add(Builder excluded) {
                excluded.staticField("com.samsung.android.smartclip.SpenGestureManager",
                        "mContext");
            }
        };
        str = "CLIPBOARD_UI_MANAGER__SINSTANCE";
        if (!LeakCanaryInternals.SAMSUNG.equals(Build.MANUFACTURER) || VERSION.SDK_INT < 19 ||
                VERSION.SDK_INT > 21) {
            z = false;
        } else {
            z = true;
        }
        CLIPBOARD_UI_MANAGER__SINSTANCE = new AndroidExcludedRefs(str, 14, z) {
            void add(Builder excluded) {
                excluded.staticField("android.sec.clipboard.ClipboardUIManager", "sInstance");
            }
        };
        str = "BUBBLE_POPUP_HELPER__SHELPER";
        if (!LeakCanaryInternals.LG.equals(Build.MANUFACTURER) || VERSION.SDK_INT < 19 || VERSION
                .SDK_INT > 21) {
            z = false;
        } else {
            z = true;
        }
        BUBBLE_POPUP_HELPER__SHELPER = new AndroidExcludedRefs(str, 15, z) {
            void add(Builder excluded) {
                excluded.staticField("android.widget.BubblePopupHelper", "sHelper");
            }
        };
        str = "AW_RESOURCE__SRESOURCES";
        if (LeakCanaryInternals.SAMSUNG.equals(Build.MANUFACTURER) && VERSION.SDK_INT == 19) {
            z = true;
        } else {
            z = false;
        }
        AW_RESOURCE__SRESOURCES = new AndroidExcludedRefs(str, 16, z) {
            void add(Builder excluded) {
                excluded.staticField("com.android.org.chromium.android_webview.AwResource",
                        "sResources");
            }
        };
        str = "MAPPER_CLIENT";
        if (LeakCanaryInternals.NVIDIA.equals(Build.MANUFACTURER) && VERSION.SDK_INT == 19) {
            z = true;
        } else {
            z = false;
        }
        MAPPER_CLIENT = new AndroidExcludedRefs(str, 17, z) {
            void add(Builder excluded) {
                excluded.instanceField("com.nvidia.ControllerMapper.MapperClient$ServiceClient",
                        "this$0");
            }
        };
        str = "TEXT_VIEW__MLAST_HOVERED_VIEW";
        if (LeakCanaryInternals.SAMSUNG.equals(Build.MANUFACTURER) && VERSION.SDK_INT == 19) {
            z = true;
        } else {
            z = false;
        }
        TEXT_VIEW__MLAST_HOVERED_VIEW = new AndroidExcludedRefs(str, 18, z) {
            void add(Builder excluded) {
                excluded.staticField("android.widget.TextView", "mLastHoveredView");
            }
        };
        str = "PERSONA_MANAGER";
        if (LeakCanaryInternals.SAMSUNG.equals(Build.MANUFACTURER) && VERSION.SDK_INT == 19) {
            z = true;
        } else {
            z = false;
        }
        PERSONA_MANAGER = new AndroidExcludedRefs(str, 19, z) {
            void add(Builder excluded) {
                excluded.instanceField("android.os.PersonaManager", "mContext");
            }
        };
        str = "RESOURCES__MCONTEXT";
        if (LeakCanaryInternals.SAMSUNG.equals(Build.MANUFACTURER) && VERSION.SDK_INT == 19) {
            z = true;
        } else {
            z = false;
        }
        RESOURCES__MCONTEXT = new AndroidExcludedRefs(str, 20, z) {
            void add(Builder excluded) {
                excluded.instanceField("android.content.res.Resources", "mContext");
            }
        };
        str = "VIEW_CONFIGURATION__MCONTEXT";
        if (LeakCanaryInternals.SAMSUNG.equals(Build.MANUFACTURER) && VERSION.SDK_INT == 19) {
            z = true;
        } else {
            z = false;
        }
        VIEW_CONFIGURATION__MCONTEXT = new AndroidExcludedRefs(str, 21, z) {
            void add(Builder excluded) {
                excluded.instanceField("android.view.ViewConfiguration", "mContext");
            }
        };
        str = "AUDIO_MANAGER__MCONTEXT_STATIC";
        if (LeakCanaryInternals.SAMSUNG.equals(Build.MANUFACTURER) && VERSION.SDK_INT == 19) {
            z = true;
        } else {
            z = false;
        }
        AUDIO_MANAGER__MCONTEXT_STATIC = new AndroidExcludedRefs(str, 22, z) {
            void add(Builder excluded) {
                excluded.staticField("android.media.AudioManager", "mContext_static");
            }
        };
        FINALIZER_WATCHDOG_DAEMON = new AndroidExcludedRefs("FINALIZER_WATCHDOG_DAEMON", 23) {
            void add(Builder excluded) {
                excluded.thread("FinalizerWatchdogDaemon");
            }
        };
        MAIN = new AndroidExcludedRefs("MAIN", 24) {
            void add(Builder excluded) {
                excluded.thread("main");
            }
        };
        LEAK_CANARY_THREAD = new AndroidExcludedRefs("LEAK_CANARY_THREAD", 25) {
            void add(Builder excluded) {
                excluded.thread("LeakCanary-Heap-Dump");
            }
        };
        EVENT_RECEIVER__MMESSAGE_QUEUE = new AndroidExcludedRefs
                ("EVENT_RECEIVER__MMESSAGE_QUEUE", 26) {
            void add(Builder excluded) {
                excluded.instanceField("android.view.Choreographer.FrameDisplayEventReceiver",
                        "mMessageQueue");
            }
        };
        $VALUES = new AndroidExcludedRefs[]{ACTIVITY_CLIENT_RECORD__NEXT_IDLE, SPAN_CONTROLLER,
                MEDIA_SESSION_LEGACY_HELPER__SINSTANCE, TEXT_LINE__SCACHED, BLOCKING_QUEUE,
                INPUT_METHOD_MANAGER__SERVED_VIEW, INPUT_METHOD_MANAGER__ROOT_VIEW,
                LAYOUT_TRANSITION, SPELL_CHECKER_SESSION, ACTIVITY_CHOOSE_MODEL,
                SPEECH_RECOGNIZER, ACCOUNT_MANAGER, DEVICE_POLICY_MANAGER__SETTINGS_OBSERVER,
                SPEN_GESTURE_MANAGER, CLIPBOARD_UI_MANAGER__SINSTANCE,
                BUBBLE_POPUP_HELPER__SHELPER, AW_RESOURCE__SRESOURCES, MAPPER_CLIENT,
                TEXT_VIEW__MLAST_HOVERED_VIEW, PERSONA_MANAGER, RESOURCES__MCONTEXT,
                VIEW_CONFIGURATION__MCONTEXT, AUDIO_MANAGER__MCONTEXT_STATIC,
                FINALIZER_WATCHDOG_DAEMON, MAIN, LEAK_CANARY_THREAD,
                EVENT_RECEIVER__MMESSAGE_QUEUE};
    }

    public static Builder createAndroidDefaults() {
        return createBuilder(EnumSet.of(FINALIZER_WATCHDOG_DAEMON, MAIN, LEAK_CANARY_THREAD));
    }

    public static Builder createAppDefaults() {
        return createBuilder(EnumSet.allOf(AndroidExcludedRefs.class));
    }

    public static Builder createBuilder(EnumSet<AndroidExcludedRefs> refs) {
        Builder excluded = new Builder();
        Iterator i$ = refs.iterator();
        while (i$.hasNext()) {
            AndroidExcludedRefs ref = (AndroidExcludedRefs) i$.next();
            if (ref.applies) {
                ref.add(excluded);
            }
        }
        return excluded;
    }

    private AndroidExcludedRefs(boolean applies) {
        this.applies = applies;
    }
}
