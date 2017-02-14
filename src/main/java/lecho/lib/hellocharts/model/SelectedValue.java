package lecho.lib.hellocharts.model;

public class SelectedValue {
    private int firstIndex;
    private int secondIndex;
    private int selectedType;
    private SelectedValueType type = SelectedValueType.NONE;

    public enum SelectedType {
        POINT,
        IMG,
        POP
    }

    public enum SelectedValueType {
        NONE,
        LINE,
        COLUMN
    }

    public SelectedValue() {
        clear();
    }

    public SelectedValue(int firstIndex, int secondIndex, SelectedValueType type) {
        set(firstIndex, secondIndex, type);
    }

    public void set(int firstIndex, int secondIndex, SelectedValueType type) {
        this.firstIndex = firstIndex;
        this.secondIndex = secondIndex;
        if (type != null) {
            this.type = type;
        } else {
            this.type = SelectedValueType.NONE;
        }
    }

    public void set(int firstIndex, int secondIndex, SelectedValueType type, int selectedType) {
        this.firstIndex = firstIndex;
        this.secondIndex = secondIndex;
        if (type != null) {
            this.type = type;
        } else {
            this.type = SelectedValueType.NONE;
        }
        this.selectedType = selectedType;
    }

    public void set(SelectedValue selectedValue) {
        this.firstIndex = selectedValue.firstIndex;
        this.secondIndex = selectedValue.secondIndex;
        this.type = selectedValue.type;
        this.selectedType = selectedValue.selectedType;
    }

    public void clear() {
        set(Integer.MIN_VALUE, Integer.MIN_VALUE, SelectedValueType.NONE);
    }

    public boolean isSet() {
        if (this.firstIndex < 0 || this.secondIndex < 0) {
            return false;
        }
        return true;
    }

    public int getFirstIndex() {
        return this.firstIndex;
    }

    public void setFirstIndex(int firstIndex) {
        this.firstIndex = firstIndex;
    }

    public int getSecondIndex() {
        return this.secondIndex;
    }

    public void setSecondIndex(int secondIndex) {
        this.secondIndex = secondIndex;
    }

    public SelectedValueType getType() {
        return this.type;
    }

    public void setType(SelectedValueType type) {
        this.type = type;
    }

    public int getSelectedType() {
        return this.selectedType;
    }

    public void setSelectedType(int selectedType) {
        this.selectedType = selectedType;
    }

    public int hashCode() {
        return ((((this.firstIndex + 31) * 31) + this.secondIndex) * 31) + (this.type == null ? 0 : this.type.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SelectedValue other = (SelectedValue) obj;
        if (this.firstIndex != other.firstIndex) {
            return false;
        }
        if (this.secondIndex != other.secondIndex) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "SelectedValue [firstIndex=" + this.firstIndex + ", secondIndex=" + this.secondIndex + ", type=" + this.type + "]";
    }
}
