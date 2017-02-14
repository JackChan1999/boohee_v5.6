package com.hanyou.leyusdk;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashMap;

public class Doc extends HashMap implements Serializable {
    public Doc(int size) {
        super(size);
    }

    public void put(String key, String value) {
        if (key != null) {
            super.put(key, value);
        }
    }

    public void putIn(String key, int i) {
        if (key != null) {
            super.put(key, String.valueOf(i));
        }
    }

    public void putO(String key, Object obj) {
        super.put(key, obj);
    }

    public void putB(String key, boolean b) {
        if (key != null) {
            super.put(key, String.valueOf(b));
        }
    }

    public void putI(String key, int i) {
        Integer integer = new Integer(i);
        if (key != null) {
            super.put(key, integer);
        }
    }

    public String get(String key) {
        Object o = super.get(key);
        if (o == null) {
            return null;
        }
        String output = o.toString();
        if (output == null) {
            return null;
        }
        return output;
    }

    public String get(String key, String defaultValue) {
        Object o = super.get(key);
        if (o == null) {
            return defaultValue;
        }
        String output = o.toString();
        if (output == null) {
            output = defaultValue;
        }
        return output;
    }

    public Object getO(String key) {
        if (this == null) {
            return null;
        }
        return super.get(key);
    }

    public Date getLD(String key) {
        return new Date(getL(key));
    }

    public Date getD(String key) {
        if (this == null) {
            return null;
        }
        String output = get(key);
        if (output == null || output.equals("")) {
            return null;
        }
        try {
            return Date.valueOf(output);
        } catch (Exception e) {
            return null;
        }
    }

    public long getL(String key) {
        if (this == null) {
            return 0;
        }
        String output = get(key);
        if (output == null) {
            return 0;
        }
        try {
            if (output.equals("")) {
                return 0;
            }
            return Long.parseLong(output);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getIn(String key) {
        if (this == null) {
            return 0;
        }
        String output = get(key);
        if (output == null) {
            return 0;
        }
        try {
            if (output.equals("")) {
                return 0;
            }
            return Integer.parseInt(output);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Integer getI(String key) {
        Integer integer = new Integer(0);
        if (this == null) {
            return integer;
        }
        String output = get(key);
        if (output != null) {
            try {
                if (!output.equals("")) {
                    return new Integer(Integer.parseInt(output));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return integer;
            }
        }
        return new Integer(0);
    }

    public boolean getB(String key) {
        if (this == null) {
            return false;
        }
        String output = get(key);
        if (output == null) {
            return false;
        }
        output = output.toLowerCase();
        if (output.equals("1") || output.equals("y") || output.equals("yes") || output.equals
                ("true") || output.equals("on")) {
            return true;
        }
        return false;
    }
}
