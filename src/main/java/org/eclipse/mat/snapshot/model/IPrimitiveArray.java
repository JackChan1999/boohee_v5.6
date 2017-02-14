package org.eclipse.mat.snapshot.model;

public interface IPrimitiveArray extends IArray {
    public static final Class<?>[] COMPONENT_TYPE = new Class[]{null, null, null, null, Boolean.TYPE, Character.TYPE, Float.TYPE, Double.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE};
    public static final int[] ELEMENT_SIZE = new int[]{-1, -1, -1, -1, 1, 2, 4, 8, 1, 2, 4, 8};
    public static final byte[] SIGNATURES = new byte[]{(byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 90, (byte) 67, (byte) 70, (byte) 68, (byte) 66, (byte) 83, (byte) 73, (byte) 74};
    public static final String[] TYPE = new String[]{null, null, null, null, "boolean[]", "char[]", "float[]", "double[]", "byte[]", "short[]", "int[]", "long[]"};

    Class<?> getComponentType();

    int getType();

    Object getValueArray();

    Object getValueArray(int i, int i2);

    Object getValueAt(int i);
}
