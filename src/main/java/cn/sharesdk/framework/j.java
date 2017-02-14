package cn.sharesdk.framework;

import java.util.Comparator;

class j implements Comparator<Platform> {
    final /* synthetic */ i a;

    j(i iVar) {
        this.a = iVar;
    }

    public int a(Platform platform, Platform platform2) {
        return platform.getSortId() != platform2.getSortId() ? platform.getSortId() - platform2.getSortId() : platform.getPlatformId() - platform2.getPlatformId();
    }

    public /* synthetic */ int compare(Object obj, Object obj2) {
        return a((Platform) obj, (Platform) obj2);
    }
}
