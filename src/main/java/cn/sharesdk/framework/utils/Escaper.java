package cn.sharesdk.framework.utils;

public interface Escaper {
    Appendable escape(Appendable appendable);

    String escape(String str);
}
