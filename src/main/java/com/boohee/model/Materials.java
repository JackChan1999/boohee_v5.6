package com.boohee.model;

import java.util.ArrayList;

public class Materials extends ModelBase {
    public ArrayList<Material> major_materials;
    public ArrayList<Material> minor_materials;
    public ArrayList<Material> raw;
    public ArrayList<Material> seasoning;

    public class Material {
        public String desc;
        public String name;
        public float  weight;

        public Material(String name, float weight) {
            this.name = name;
            this.weight = weight;
        }

        public Material(Materials this$0, String name, String desc, float weight) {
            this(name, weight);
            this.desc = desc;
        }
    }

    public Materials(ArrayList<Material> raw) {
        this.raw = raw;
    }

    public Materials(ArrayList<Material> major, ArrayList<Material> minor, ArrayList<Material>
            seasoning) {
        this.major_materials = major;
        this.minor_materials = minor;
        this.seasoning = seasoning;
    }

    public String major() {
        if (this.raw != null && this.raw.size() > 0) {
            return getNames(this.raw);
        }
        if (this.major_materials == null || this.major_materials.size() <= 0) {
            return null;
        }
        return getNames(this.major_materials);
    }

    public String majorDesc() {
        if (this.raw != null && this.raw.size() > 0) {
            return getDesc(this.raw);
        }
        if (this.major_materials == null || this.major_materials.size() <= 0) {
            return null;
        }
        return getDesc(this.major_materials);
    }

    private String getNames(ArrayList<Material> materials) {
        StringBuilder name = new StringBuilder();
        int i = 0;
        while (i < materials.size()) {
            try {
                if (((Material) materials.get(i)).desc != null) {
                    name.append(((Material) materials.get(i)).name);
                    name.append(" + ");
                }
                i++;
            } catch (Exception e) {
                return "暂无主料";
            }
        }
        name.delete(name.length() - 3, name.length());
        return name.toString();
    }

    private String getDesc(ArrayList<Material> materials) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < materials.size()) {
            try {
                if (((Material) materials.get(i)).desc != null) {
                    sb.append(((Material) materials.get(i)).name);
                    sb.append(":  ");
                    sb.append(((Material) materials.get(i)).desc);
                    sb.append("\n");
                }
                i++;
            } catch (Exception e) {
                return "暂无评价";
            }
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }

    public int size() {
        int count = 0;
        if (this.raw != null && this.raw.size() > 0) {
            return 1;
        }
        if (this.major_materials != null && this.major_materials.size() > 0) {
            count = 1;
        }
        if (this.minor_materials != null && this.minor_materials.size() > 0) {
            count = 2;
        }
        if (this.seasoning == null || this.seasoning.size() <= 0) {
            return count;
        }
        return 3;
    }

    public Material get(int index) {
        return (Material) this.raw.get(index);
    }

    public String toString() {
        if (this.raw != null) {
            return ((Material) this.raw.get(0)).name;
        }
        if (this.major_materials != null) {
            return ((Material) this.major_materials.get(0)).name;
        }
        return "null";
    }
}
