package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;

public class DoubleSerializer implements ObjectSerializer {
    public static final DoubleSerializer instance = new DoubleSerializer();
    private DecimalFormat decimalFormat;

    public DoubleSerializer() {
        this.decimalFormat = null;
    }

    public DoubleSerializer(DecimalFormat decimalFormat) {
        this.decimalFormat = null;
        this.decimalFormat = decimalFormat;
    }

    public DoubleSerializer(String decimalFormat) {
        this(new DecimalFormat(decimalFormat));
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (object != null) {
            double doubleValue = ((Double) object).doubleValue();
            if (Double.isNaN(doubleValue)) {
                out.writeNull();
            } else if (Double.isInfinite(doubleValue)) {
                out.writeNull();
            } else {
                CharSequence doubleText;
                if (this.decimalFormat == null) {
                    doubleText = Double.toString(doubleValue);
                    if (doubleText.endsWith(".0")) {
                        doubleText = doubleText.substring(0, doubleText.length() - 2);
                    }
                } else {
                    doubleText = this.decimalFormat.format(doubleValue);
                }
                out.append(doubleText);
                if (serializer.isEnabled(SerializerFeature.WriteClassName)) {
                    out.write('D');
                }
            }
        } else if (serializer.isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
            out.write('0');
        } else {
            out.writeNull();
        }
    }
}
